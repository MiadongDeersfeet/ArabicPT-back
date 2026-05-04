package com.arabicpt.sentence.service;

import com.arabicpt.common.exception.BusinessException;
import com.arabicpt.common.response.ResultCode;
import com.arabicpt.sentence.mapper.SentenceMapper;
import com.arabicpt.sentence.model.dto.SentenceCreateRequestDTO;
import com.arabicpt.sentence.model.dto.SentenceResponseDTO;
import com.arabicpt.sentence.model.dto.SentenceUpdateRequestDTO;
import com.arabicpt.sentence.model.vo.SentenceVO;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SentenceServiceImpl implements SentenceService {

    private final Long memberId;
    private final SentenceMapper sentenceMapper;

    private static final String DEFAULT_FRONT_LANG = "ko-KR";
    private static final String DEFAULT_BACK_LANG = "ar-SA";

    public SentenceServiceImpl(SentenceMapper sentenceMapper) {
        this.memberId = 1L;
        this.sentenceMapper = sentenceMapper;
    }

    @Override
    public List<SentenceResponseDTO> findSentencesBySetId(Long setId) {
        validateActiveSet(setId);
        return sentenceMapper.selectSentenceListBySetId(setId, memberId);
    }

    @Override
    @Transactional
    public SentenceResponseDTO createSentenceInSet(Long setId, SentenceCreateRequestDTO request) {
        validateActiveSet(setId);

        SentenceVO sentence = SentenceVO.builder()
            .memberId(memberId)
            .setId(setId)
            .frontLang(defaultIfBlank(request.getFrontLang(), DEFAULT_FRONT_LANG))
            .frontText(request.getFrontText().trim())
            .backLang(defaultIfBlank(request.getBackLang(), DEFAULT_BACK_LANG))
            .backText(request.getBackText().trim())
            .memo(trimToNull(request.getMemo()))
            .displayOrder(0)
            .isActive("Y")
            .build();

        int inserted = sentenceMapper.insertSentence(sentence);
        if (inserted != 1 || sentence.getSentenceId() == null) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "문장 생성에 실패했습니다.");
        }

        SentenceResponseDTO created = sentenceMapper.selectSentenceById(sentence.getSentenceId(), memberId);
        if (created == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "생성된 문장을 찾을 수 없습니다.");
        }
        return created;
    }

    @Override
    public SentenceResponseDTO findSentence(Long sentenceId) {
        SentenceResponseDTO sentence = sentenceMapper.selectSentenceById(sentenceId, memberId);
        if (sentence == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "문장을 찾을 수 없습니다.");
        }
        return sentence;
    }

    @Override
    @Transactional
    public SentenceResponseDTO updateSentence(Long sentenceId, SentenceUpdateRequestDTO request) {
        SentenceResponseDTO current = sentenceMapper.selectSentenceById(sentenceId, memberId);
        if (current == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "문장을 찾을 수 없습니다.");
        }

        SentenceVO.SentenceVOBuilder voBuilder = SentenceVO.builder()
            .sentenceId(sentenceId)
            .memberId(memberId);

        if (request.getFrontLang() != null) {
            voBuilder.frontLang(request.getFrontLang());
        }
        if (request.getFrontText() != null) {
            String t = request.getFrontText().trim();
            if (t.isEmpty()) {
                throw new BusinessException(ResultCode.INVALID_REQUEST, "앞면 문장은 비울 수 없습니다.");
            }
            voBuilder.frontText(t);
        }
        if (request.getBackLang() != null) {
            voBuilder.backLang(request.getBackLang());
        }
        if (request.getBackText() != null) {
            String t = request.getBackText().trim();
            if (t.isEmpty()) {
                throw new BusinessException(ResultCode.INVALID_REQUEST, "뒷면 문장은 비울 수 없습니다.");
            }
            voBuilder.backText(t);
        }
        if (request.getMemo() != null) {
            voBuilder.memo(request.getMemo().trim());
        }
        if (request.getDisplayOrder() != null) {
            voBuilder.displayOrder(request.getDisplayOrder());
        }

        sentenceMapper.updateSentence(voBuilder.build());

        SentenceResponseDTO updated = sentenceMapper.selectSentenceById(sentenceId, memberId);
        if (updated == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "수정된 문장을 찾을 수 없습니다.");
        }
        return updated;
    }

    @Override
    @Transactional
    public void deleteSentence(Long sentenceId) {
        SentenceResponseDTO current = sentenceMapper.selectSentenceById(sentenceId, memberId);
        if (current == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "문장을 찾을 수 없습니다.");
        }

        int affected = sentenceMapper.softDeleteSentence(sentenceId, memberId);
        if (affected != 1) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "문장 삭제에 실패했습니다.");
        }
    }

    private void validateActiveSet(Long setId) {
        int count = sentenceMapper.countActiveSetById(setId, memberId);
        if (count == 0) {
            throw new BusinessException(ResultCode.NOT_FOUND, "문장 세트를 찾을 수 없습니다.");
        }
    }

    private String defaultIfBlank(String value, String fallback) {
        return (value == null || value.isBlank()) ? fallback : value;
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
