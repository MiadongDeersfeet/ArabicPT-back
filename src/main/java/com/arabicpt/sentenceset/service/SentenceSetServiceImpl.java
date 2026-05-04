package com.arabicpt.sentenceset.service;

import com.arabicpt.common.exception.BusinessException;
import com.arabicpt.common.response.ResultCode;
import com.arabicpt.folder.mapper.FolderMapper;
import com.arabicpt.folder.model.vo.FolderVO;
import com.arabicpt.sentence.mapper.SentenceMapper;
import com.arabicpt.sentenceset.mapper.SentenceSetMapper;
import com.arabicpt.sentenceset.model.dto.SentenceSetCreateRequestDTO;
import com.arabicpt.sentenceset.model.dto.SentenceSetResponseDTO;
import com.arabicpt.sentenceset.model.dto.SentenceSetUpdateRequestDTO;
import com.arabicpt.sentenceset.model.vo.SentenceSetVO;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SentenceSetServiceImpl implements SentenceSetService {

    private final Long memberId;
    private final SentenceSetMapper sentenceSetMapper;
    private final FolderMapper folderMapper;
    private final SentenceMapper sentenceMapper;

    public SentenceSetServiceImpl(
        SentenceSetMapper sentenceSetMapper,
        FolderMapper folderMapper,
        SentenceMapper sentenceMapper
    ) {
        this.memberId = 1L;
        this.sentenceSetMapper = sentenceSetMapper;
        this.folderMapper = folderMapper;
        this.sentenceMapper = sentenceMapper;
    }

    @Override
    @Transactional
    public SentenceSetResponseDTO createSentenceSet(SentenceSetCreateRequestDTO request) {
        Long folderId = request.getFolderId();
        if (folderId != null) {
            validateFolderOwnership(folderId);
        }

        SentenceSetVO vo = SentenceSetVO.builder()
            .memberId(memberId)
            .folderId(folderId)
            .setName(request.getSetName().trim())
            .description(trimToNull(request.getDescription()))
            .displayOrder(0)
            .isActive("Y")
            .build();

        int inserted = sentenceSetMapper.insertSentenceSet(vo);
        if (inserted != 1 || vo.getSetId() == null) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "문장 세트 생성에 실패했습니다.");
        }

        SentenceSetResponseDTO created = sentenceSetMapper.selectSentenceSetById(vo.getSetId(), memberId);
        if (created == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "생성된 문장 세트를 찾을 수 없습니다.");
        }
        return created;
    }

    @Override
    public List<SentenceSetResponseDTO> findSentenceSets(Long folderId) {
        return sentenceSetMapper.selectSentenceSetList(memberId, folderId);
    }

    @Override
    public SentenceSetResponseDTO findSentenceSet(Long setId) {
        SentenceSetResponseDTO set = sentenceSetMapper.selectSentenceSetById(setId, memberId);
        if (set == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "문장 세트를 찾을 수 없습니다.");
        }
        return set;
    }

    @Override
    @Transactional
    public SentenceSetResponseDTO updateSentenceSet(Long setId, SentenceSetUpdateRequestDTO request) {
        ensureSetExists(setId);

        boolean hasUpdate =
            request.getSetName() != null
                || request.getDescription() != null
                || Boolean.TRUE.equals(request.getClearFolder())
                || request.getFolderId() != null;

        if (!hasUpdate) {
            return findSentenceSet(setId);
        }

        var builder = SentenceSetVO.builder()
            .setId(setId)
            .memberId(memberId);

        if (request.getSetName() != null) {
            String trimmed = request.getSetName().trim();
            if (trimmed.isEmpty()) {
                throw new BusinessException(ResultCode.INVALID_REQUEST, "세트 이름은 비울 수 없습니다.");
            }
            builder.setName(trimmed);
        }
        if (request.getDescription() != null) {
            builder.description(request.getDescription().trim());
        }

        if (Boolean.TRUE.equals(request.getClearFolder())) {
            builder.detachFromFolder(true);
        } else if (request.getFolderId() != null) {
            validateFolderOwnership(request.getFolderId());
            builder.folderId(request.getFolderId());
        }

        SentenceSetVO vo = builder.build();

        int affected = sentenceSetMapper.updateSentenceSet(vo);
        if (affected != 1) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "문장 세트 수정에 실패했습니다.");
        }

        SentenceSetResponseDTO updated = sentenceSetMapper.selectSentenceSetById(setId, memberId);
        if (updated == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "수정된 문장 세트를 찾을 수 없습니다.");
        }
        return updated;
    }

    @Override
    @Transactional
    public void deleteSentenceSet(Long setId) {
        ensureSetExists(setId);

        sentenceMapper.softDeleteSentencesBySetId(setId, memberId);

        int sets = sentenceSetMapper.softDeleteSentenceSet(setId, memberId);
        if (sets != 1) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "문장 세트 삭제에 실패했습니다.");
        }
    }

    private void ensureSetExists(Long setId) {
        int count = sentenceSetMapper.countActiveById(setId, memberId);
        if (count == 0) {
            throw new BusinessException(ResultCode.NOT_FOUND, "문장 세트를 찾을 수 없습니다.");
        }
    }

    private void validateFolderOwnership(Long folderId) {
        FolderVO folder = folderMapper.selectFolderById(folderId, memberId);
        if (folder == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "폴더를 찾을 수 없습니다.");
        }
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
