package com.arabicpt.paragraphset.service;

import com.arabicpt.common.exception.BusinessException;
import com.arabicpt.common.response.ResultCode;
import com.arabicpt.folder.mapper.FolderMapper;
import com.arabicpt.folder.model.vo.FolderVO;
import com.arabicpt.paragraph.mapper.ParagraphMapper;
import com.arabicpt.paragraphset.mapper.ParagraphSetMapper;
import com.arabicpt.paragraphset.model.dto.ParagraphSetCreateRequestDTO;
import com.arabicpt.paragraphset.model.dto.ParagraphSetResponseDTO;
import com.arabicpt.paragraphset.model.dto.ParagraphSetUpdateRequestDTO;
import com.arabicpt.paragraphset.model.vo.ParagraphSetVO;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ParagraphSetServiceImpl implements ParagraphSetService {

    private final Long memberId;
    private final ParagraphSetMapper paragraphSetMapper;
    private final FolderMapper folderMapper;
    private final ParagraphMapper paragraphMapper;

    public ParagraphSetServiceImpl(
        ParagraphSetMapper paragraphSetMapper,
        FolderMapper folderMapper,
        ParagraphMapper paragraphMapper
    ) {
        this.memberId = 1L;
        this.paragraphSetMapper = paragraphSetMapper;
        this.folderMapper = folderMapper;
        this.paragraphMapper = paragraphMapper;
    }

    @Override
    @Transactional
    public ParagraphSetResponseDTO createParagraphSet(ParagraphSetCreateRequestDTO request) {
        Long folderId = request.getFolderId();
        if (folderId != null) {
            validateFolderOwnership(folderId);
        }

        ParagraphSetVO vo = ParagraphSetVO.builder()
            .memberId(memberId)
            .folderId(folderId)
            .setName(request.getSetName().trim())
            .description(trimToNull(request.getDescription()))
            .displayOrder(0)
            .isActive("Y")
            .build();

        int inserted = paragraphSetMapper.insertParagraphSet(vo);
        if (inserted != 1 || vo.getParagraphSetId() == null) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "문단 세트 생성에 실패했습니다.");
        }

        ParagraphSetResponseDTO created = paragraphSetMapper.selectParagraphSetById(vo.getParagraphSetId(), memberId);
        if (created == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "생성된 문단 세트를 찾을 수 없습니다.");
        }
        return created;
    }

    @Override
    public List<ParagraphSetResponseDTO> findParagraphSets(Long folderId) {
        return paragraphSetMapper.selectParagraphSetList(memberId, folderId);
    }

    @Override
    public ParagraphSetResponseDTO findParagraphSet(Long paragraphSetId) {
        ParagraphSetResponseDTO set = paragraphSetMapper.selectParagraphSetById(paragraphSetId, memberId);
        if (set == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "문단 세트를 찾을 수 없습니다.");
        }
        return set;
    }

    @Override
    @Transactional
    public ParagraphSetResponseDTO updateParagraphSet(Long paragraphSetId, ParagraphSetUpdateRequestDTO request) {
        ensureSetExists(paragraphSetId);

        boolean hasUpdate =
            request.getSetName() != null
                || request.getDescription() != null
                || Boolean.TRUE.equals(request.getClearFolder())
                || request.getFolderId() != null;

        if (!hasUpdate) {
            return findParagraphSet(paragraphSetId);
        }

        var builder = ParagraphSetVO.builder()
            .paragraphSetId(paragraphSetId)
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

        int affected = paragraphSetMapper.updateParagraphSet(builder.build());
        if (affected != 1) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "문단 세트 수정에 실패했습니다.");
        }

        ParagraphSetResponseDTO updated = paragraphSetMapper.selectParagraphSetById(paragraphSetId, memberId);
        if (updated == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "수정된 문단 세트를 찾을 수 없습니다.");
        }
        return updated;
    }

    @Override
    @Transactional
    public void deleteParagraphSet(Long paragraphSetId) {
        ensureSetExists(paragraphSetId);

        paragraphMapper.softDeleteParagraphsBySetId(paragraphSetId, memberId);

        int sets = paragraphSetMapper.softDeleteParagraphSet(paragraphSetId, memberId);
        if (sets != 1) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "문단 세트 삭제에 실패했습니다.");
        }
    }

    private void ensureSetExists(Long paragraphSetId) {
        int count = paragraphSetMapper.countActiveById(paragraphSetId, memberId);
        if (count == 0) {
            throw new BusinessException(ResultCode.NOT_FOUND, "문단 세트를 찾을 수 없습니다.");
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
