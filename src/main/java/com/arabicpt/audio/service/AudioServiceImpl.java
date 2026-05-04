package com.arabicpt.audio.service;

import com.arabicpt.audio.mapper.AudioMapper;
import com.arabicpt.audio.model.dto.SentenceAudioCreateRequestDTO;
import com.arabicpt.audio.model.dto.SentenceAudioResponseDTO;
import com.arabicpt.audio.model.vo.SentenceAudioVO;
import com.arabicpt.common.exception.BusinessException;
import com.arabicpt.common.response.ResultCode;
import com.arabicpt.sentence.mapper.SentenceMapper;
import com.arabicpt.sentence.model.dto.SentenceResponseDTO;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AudioServiceImpl implements AudioService {

    private static final String DEFAULT_LANGUAGE_CODE = "ar-SA";
    private static final String DEFAULT_AUDIO_TYPE = "SENTENCE";
    private static final String DEFAULT_SPEAKING_RATE = "NORMAL";
    private static final String DEFAULT_PROVIDER = "ELEVENLABS";
    private static final String DEFAULT_FORMAT = "MP3";
    /** DB TB_SENTENCE_AUDIO.AUDIO_URL NOT NULL — 실제 URL 연동 전까지 스텁 값 */
    private static final String STUB_AUDIO_URL = "pending";

    private final Long memberId;
    private final AudioMapper audioMapper;
    private final SentenceMapper sentenceMapper;

    public AudioServiceImpl(AudioMapper audioMapper, SentenceMapper sentenceMapper) {
        this.memberId = 1L;
        this.audioMapper = audioMapper;
        this.sentenceMapper = sentenceMapper;
    }

    @Override
    @Transactional
    public SentenceAudioResponseDTO createSentenceAudio(Long sentenceId, SentenceAudioCreateRequestDTO request) {
        SentenceResponseDTO sentence = sentenceMapper.selectSentenceById(sentenceId, memberId);
        if (sentence == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "문장을 찾을 수 없습니다.");
        }

        SentenceAudioVO vo = SentenceAudioVO.builder()
            .sentenceId(sentenceId)
            .languageCode(blankToDefault(request.getLanguageCode(), DEFAULT_LANGUAGE_CODE))
            .voiceName(trimToNull(request.getVoiceName()))
            .audioType(blankToDefault(request.getAudioType(), DEFAULT_AUDIO_TYPE))
            .speakingRate(blankToDefault(request.getSpeakingRate(), DEFAULT_SPEAKING_RATE))
            .audioUrl(blankToDefault(request.getAudioUrl(), STUB_AUDIO_URL))
            .storageKey(trimToNull(request.getStorageKey()))
            .provider(blankToDefault(request.getProvider(), DEFAULT_PROVIDER))
            .format(blankToDefault(request.getFormat(), DEFAULT_FORMAT))
            .isActive("Y")
            .build();

        int inserted = audioMapper.insertSentenceAudio(vo);
        if (inserted != 1 || vo.getAudioId() == null) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "오디오 정보 저장에 실패했습니다.");
        }

        List<SentenceAudioResponseDTO> list = audioMapper.selectSentenceAudioList(sentenceId, memberId);
        return list.stream()
            .filter(row -> vo.getAudioId().equals(row.getAudioId()))
            .findFirst()
            .orElseThrow(() -> new BusinessException(ResultCode.NOT_FOUND, "저장된 오디오를 찾을 수 없습니다."));
    }

    @Override
    public List<SentenceAudioResponseDTO> findSentenceAudioList(Long sentenceId) {
        SentenceResponseDTO sentence = sentenceMapper.selectSentenceById(sentenceId, memberId);
        if (sentence == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "문장을 찾을 수 없습니다.");
        }
        return audioMapper.selectSentenceAudioList(sentenceId, memberId);
    }

    private String blankToDefault(String value, String fallback) {
        return (value == null || value.isBlank()) ? fallback : value.trim();
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
