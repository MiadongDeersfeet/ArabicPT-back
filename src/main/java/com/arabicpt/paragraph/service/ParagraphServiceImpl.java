package com.arabicpt.paragraph.service;

import com.arabicpt.audio.mapper.ParagraphAudioMapper;
import com.arabicpt.common.exception.BusinessException;
import com.arabicpt.common.response.ResultCode;
import com.arabicpt.paragraph.mapper.ParagraphMapper;
import com.arabicpt.paragraph.model.dto.ParagraphCreateRequestDTO;
import com.arabicpt.paragraph.model.dto.ParagraphResponseDTO;
import com.arabicpt.paragraph.model.dto.ParagraphUpdateRequestDTO;
import com.arabicpt.paragraph.model.vo.ParagraphVO;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ParagraphServiceImpl implements ParagraphService {

    private final Long memberId;
    private final ParagraphMapper paragraphMapper;
    private final ParagraphAudioMapper paragraphAudioMapper;

    private static final String DEFAULT_FRONT_LANG = "ko-KR";
    private static final String DEFAULT_BACK_LANG = "ar-SA";

    public ParagraphServiceImpl(ParagraphMapper paragraphMapper, ParagraphAudioMapper paragraphAudioMapper) {
        this.memberId = 1L;
        this.paragraphMapper = paragraphMapper;
        this.paragraphAudioMapper = paragraphAudioMapper;
    }

    @Override
    public List<ParagraphResponseDTO> findParagraphsBySetId(Long paragraphSetId) {
        validateActiveSet(paragraphSetId);
        return paragraphMapper.selectParagraphListBySetId(paragraphSetId, memberId);
    }

    @Override
    @Transactional
    public ParagraphResponseDTO createParagraphInSet(Long paragraphSetId, ParagraphCreateRequestDTO request) {
        validateActiveSet(paragraphSetId);

        String frontText = request.getFrontText().trim();
        String backText = request.getBackText().trim();
        if (frontText.isEmpty()) {
            throw new BusinessException(ResultCode.INVALID_REQUEST, "한국어 문단은 비울 수 없습니다.");
        }
        if (backText.isEmpty()) {
            throw new BusinessException(ResultCode.INVALID_REQUEST, "아랍어 문단은 비울 수 없습니다.");
        }

        ParagraphVO paragraph = ParagraphVO.builder()
            .memberId(memberId)
            .paragraphSetId(paragraphSetId)
            .frontLang(defaultIfBlank(request.getFrontLang(), DEFAULT_FRONT_LANG))
            .frontText(frontText)
            .backLang(defaultIfBlank(request.getBackLang(), DEFAULT_BACK_LANG))
            .backText(backText)
            .memo(trimToNull(request.getMemo()))
            .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
            .isActive("Y")
            .build();

        int inserted = paragraphMapper.insertParagraph(paragraph);
        if (inserted != 1 || paragraph.getParagraphId() == null) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "문단 생성에 실패했습니다.");
        }

        ParagraphResponseDTO created = paragraphMapper.selectParagraphById(paragraph.getParagraphId(), memberId);
        if (created == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "생성된 문단을 찾을 수 없습니다.");
        }
        return created;
    }

    @Override
    public ParagraphResponseDTO findParagraph(Long paragraphId) {
        ParagraphResponseDTO paragraph = paragraphMapper.selectParagraphById(paragraphId, memberId);
        if (paragraph == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "문단을 찾을 수 없습니다.");
        }
        return paragraph;
    }

    @Override
    @Transactional
    public ParagraphResponseDTO updateParagraph(Long paragraphId, ParagraphUpdateRequestDTO request) {
        ParagraphResponseDTO current = paragraphMapper.selectParagraphById(paragraphId, memberId);
        if (current == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "문단을 찾을 수 없습니다.");
        }

        ParagraphVO.ParagraphVOBuilder voBuilder = ParagraphVO.builder()
            .paragraphId(paragraphId)
            .memberId(memberId);

        if (request.getFrontLang() != null) {
            voBuilder.frontLang(request.getFrontLang());
        }
        if (request.getFrontText() != null) {
            String t = request.getFrontText().trim();
            if (t.isEmpty()) {
                throw new BusinessException(ResultCode.INVALID_REQUEST, "한국어 문단은 비울 수 없습니다.");
            }
            voBuilder.frontText(t);
        }
        if (request.getBackLang() != null) {
            voBuilder.backLang(request.getBackLang());
        }
        if (request.getBackText() != null) {
            String t = request.getBackText().trim();
            if (t.isEmpty()) {
                throw new BusinessException(ResultCode.INVALID_REQUEST, "아랍어 문단은 비울 수 없습니다.");
            }
            voBuilder.backText(t);
        }
        if (request.getMemo() != null) {
            voBuilder.memo(request.getMemo().trim());
        }
        if (request.getDisplayOrder() != null) {
            voBuilder.displayOrder(request.getDisplayOrder());
        }

        paragraphMapper.updateParagraph(voBuilder.build());
        paragraphAudioMapper.deactivateActiveByParagraphId(paragraphId, memberId);

        ParagraphResponseDTO updated = paragraphMapper.selectParagraphById(paragraphId, memberId);
        if (updated == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "수정된 문단을 찾을 수 없습니다.");
        }
        return updated;
    }

    @Override
    @Transactional
    public void deleteParagraph(Long paragraphId) {
        ParagraphResponseDTO current = paragraphMapper.selectParagraphById(paragraphId, memberId);
        if (current == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "문단을 찾을 수 없습니다.");
        }

        int affected = paragraphMapper.softDeleteParagraph(paragraphId, memberId);
        if (affected != 1) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "문단 삭제에 실패했습니다.");
        }
    }

    private void validateActiveSet(Long paragraphSetId) {
        int count = paragraphMapper.countActiveSetById(paragraphSetId, memberId);
        if (count == 0) {
            throw new BusinessException(ResultCode.NOT_FOUND, "문단 세트를 찾을 수 없습니다.");
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
