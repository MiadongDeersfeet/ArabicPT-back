package com.arabicpt.audio.service;

import com.arabicpt.audio.mapper.ParagraphAudioMapper;
import com.arabicpt.audio.model.dto.ParagraphAudioResponseDTO;
import com.arabicpt.audio.model.vo.ParagraphAudioVO;
import com.arabicpt.common.exception.BusinessException;
import com.arabicpt.common.response.ResultCode;
import com.arabicpt.paragraph.mapper.ParagraphMapper;
import com.arabicpt.paragraph.model.dto.ParagraphResponseDTO;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ParagraphAudioServiceImpl implements ParagraphAudioService {

    private static final Long MVP_MEMBER_ID = 1L;
    private static final String DEFAULT_AUDIO_TYPE = "PARAGRAPH";
    private static final String DEFAULT_SPEAKING_RATE = "NORMAL";
    private static final String DEFAULT_PROVIDER = "ELEVENLABS";
    private static final String DEFAULT_FORMAT = "MP3";
    private static final String ACTIVE_FLAG = "Y";
    private static final String TIMESTAMP_PATTERN = "yyyyMMddHHmmss";

    private final ParagraphMapper paragraphMapper;
    private final ParagraphAudioMapper paragraphAudioMapper;
    private final ElevenLabsTtsClient elevenLabsTtsClient;
    private final String uploadDir;
    private final String publicPath;
    private final String defaultLanguageCode;
    private final String defaultVoiceName;

    public ParagraphAudioServiceImpl(
        ParagraphMapper paragraphMapper,
        ParagraphAudioMapper paragraphAudioMapper,
        ElevenLabsTtsClient elevenLabsTtsClient,
        @Value("${app.audio.upload-dir}") String uploadDir,
        @Value("${app.audio.public-path}") String publicPath,
        @Value("${elevenlabs.default-language-code}") String defaultLanguageCode,
        @Value("${elevenlabs.default-voice-name}") String defaultVoiceName
    ) {
        this.paragraphMapper = paragraphMapper;
        this.paragraphAudioMapper = paragraphAudioMapper;
        this.elevenLabsTtsClient = elevenLabsTtsClient;
        this.uploadDir = uploadDir;
        this.publicPath = publicPath;
        this.defaultLanguageCode = defaultLanguageCode;
        this.defaultVoiceName = defaultVoiceName;
    }

    @Override
    @Transactional
    public ParagraphAudioResponseDTO generateParagraphAudio(Long paragraphId) {
        ParagraphResponseDTO paragraph = paragraphMapper.selectParagraphById(paragraphId, MVP_MEMBER_ID);
        if (paragraph == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "문단을 찾을 수 없습니다.");
        }

        String arabicText = paragraph.getBackText();
        if (arabicText == null || arabicText.isBlank()) {
            throw new BusinessException(ResultCode.INVALID_REQUEST, "아랍어 문단이 없어 음성을 생성할 수 없습니다.");
        }

        byte[] audioBytes = elevenLabsTtsClient.generateSpeech(arabicText.trim());
        if (audioBytes == null || audioBytes.length == 0) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "음성 파일 생성 결과가 비어 있습니다.");
        }

        Path savedFilePath = saveAudioFile(paragraphId, audioBytes);
        String fileName = savedFilePath.getFileName().toString();
        String normalizedPublicPath = normalizePublicPath(publicPath);
        String audioUrl = normalizedPublicPath + "/paragraphs/" + fileName;
        String storageKey = savedFilePath.toString().replace("\\", "/");

        try {
            paragraphAudioMapper.deactivateActiveByParagraphId(paragraphId, MVP_MEMBER_ID);

            ParagraphAudioVO vo = ParagraphAudioVO.builder()
                .paragraphId(paragraphId)
                .languageCode(defaultLanguageCode)
                .voiceName(defaultVoiceName)
                .audioType(DEFAULT_AUDIO_TYPE)
                .speakingRate(DEFAULT_SPEAKING_RATE)
                .audioUrl(audioUrl)
                .storageKey(storageKey)
                .provider(DEFAULT_PROVIDER)
                .format(DEFAULT_FORMAT)
                .isActive(ACTIVE_FLAG)
                .build();

            int inserted = paragraphAudioMapper.insertParagraphAudio(vo);
            if (inserted != 1 || vo.getAudioId() == null) {
                throw new BusinessException(ResultCode.INTERNAL_ERROR, "오디오 정보 저장에 실패했습니다.");
            }

            ParagraphAudioResponseDTO response = paragraphAudioMapper.selectByAudioId(vo.getAudioId(), MVP_MEMBER_ID);
            if (response == null) {
                throw new BusinessException(ResultCode.INTERNAL_ERROR, "생성된 오디오 정보를 조회하지 못했습니다.");
            }
            return response;
        } catch (RuntimeException ex) {
            tryDeleteFile(savedFilePath);
            throw ex;
        }
    }

    @Override
    public ParagraphAudioResponseDTO findActiveParagraphAudio(Long paragraphId) {
        ParagraphResponseDTO paragraph = paragraphMapper.selectParagraphById(paragraphId, MVP_MEMBER_ID);
        if (paragraph == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "문단을 찾을 수 없습니다.");
        }
        return paragraphAudioMapper.selectActiveByParagraphId(paragraphId, MVP_MEMBER_ID);
    }

    private Path saveAudioFile(Long paragraphId, byte[] audioBytes) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIMESTAMP_PATTERN));
        String filename = paragraphId + "_ar_normal_" + timestamp + ".mp3";

        try {
            Path rootDir = Paths.get(uploadDir).toAbsolutePath().normalize();
            Path paragraphAudioDir = rootDir.resolve("paragraphs");
            Files.createDirectories(paragraphAudioDir);

            Path targetFile = paragraphAudioDir.resolve(filename).normalize();
            Files.write(targetFile, audioBytes);
            return targetFile;
        } catch (IOException e) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "오디오 파일 저장에 실패했습니다.");
        }
    }

    private void tryDeleteFile(Path path) {
        if (path == null) return;
        try {
            Files.deleteIfExists(path);
        } catch (IOException ignored) {
            // 롤백 실패 시에도 원래 예외를 우선 반환합니다.
        }
    }

    private String normalizePublicPath(String value) {
        String normalized = (value == null || value.isBlank()) ? "/audio" : value.trim();
        if (!normalized.startsWith("/")) {
            normalized = "/" + normalized;
        }
        return normalized.endsWith("/") ? normalized.substring(0, normalized.length() - 1) : normalized;
    }
}
