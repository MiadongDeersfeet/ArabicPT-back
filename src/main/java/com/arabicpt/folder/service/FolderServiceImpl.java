package com.arabicpt.folder.service;

import com.arabicpt.common.exception.BusinessException;
import com.arabicpt.common.response.ResultCode;
import com.arabicpt.folder.mapper.FolderMapper;
import com.arabicpt.folder.model.dto.FolderCreateRequestDTO;
import com.arabicpt.folder.model.dto.FolderResponseDTO;
import com.arabicpt.folder.model.dto.FolderUpdateRequestDTO;
import com.arabicpt.folder.model.vo.FolderVO;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FolderServiceImpl implements FolderService {

    private final Long memberId;
    private final FolderMapper folderMapper;

    private static final String DEFAULT_FOLDER_NAME = "미분류";

    public FolderServiceImpl(FolderMapper folderMapper) {
        this.memberId = 1L;
        this.folderMapper = folderMapper;
    }

    @Override
    public List<FolderResponseDTO> findFolders() {
        return folderMapper.selectFolderList(memberId).stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FolderResponseDTO createFolder(FolderCreateRequestDTO request) {
        String normalizedName = request.getFolderName().trim();
        if (normalizedName.isEmpty()) {
            throw new BusinessException(ResultCode.INVALID_REQUEST, "폴더 이름은 비울 수 없습니다.");
        }
        int duplicateCount = folderMapper.countFolderName(memberId, normalizedName);
        if (duplicateCount > 0) {
            throw new BusinessException(ResultCode.DUPLICATED, "같은 이름의 폴더가 이미 존재합니다.");
        }

        FolderVO newFolder = FolderVO.builder()
            .memberId(memberId)
            .folderName(normalizedName)
            .description(trimToNull(request.getDescription()))
            .displayOrder(0)
            .isDefault("N")
            .isActive("Y")
            .build();

        int inserted = folderMapper.insertFolder(newFolder);
        if (inserted != 1 || newFolder.getFolderId() == null) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "폴더 생성에 실패했습니다.");
        }

        FolderVO createdFolder = folderMapper.selectFolderById(newFolder.getFolderId(), memberId);
        if (createdFolder == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "생성된 폴더를 찾을 수 없습니다.");
        }

        return toResponseDTO(createdFolder);
    }

    @Override
    @Transactional
    public FolderVO findOrCreateDefaultFolder(Long memberIdParam) {
        FolderVO defaultFolder = folderMapper.selectDefaultFolder(memberIdParam);
        if (defaultFolder != null) {
            return defaultFolder;
        }

        FolderVO newDefaultFolder = FolderVO.builder()
            .memberId(memberIdParam)
            .folderName(DEFAULT_FOLDER_NAME)
            .description(null)
            .displayOrder(0)
            .isDefault("Y")
            .isActive("Y")
            .build();

        folderMapper.insertFolder(newDefaultFolder);

        FolderVO savedDefaultFolder = folderMapper.selectDefaultFolder(memberIdParam);
        if (savedDefaultFolder == null) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "기본 폴더 생성에 실패했습니다.");
        }
        return savedDefaultFolder;
    }

    @Override
    public FolderResponseDTO findFolder(Long folderId) {
        FolderVO folder = folderMapper.selectFolderById(folderId, memberId);
        if (folder == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "폴더를 찾을 수 없습니다.");
        }
        return toResponseDTO(folder);
    }

    @Override
    @Transactional
    public FolderResponseDTO updateFolder(Long folderId, FolderUpdateRequestDTO request) {
        FolderVO current = folderMapper.selectFolderById(folderId, memberId);
        if (current == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "폴더를 찾을 수 없습니다.");
        }

        if (request.getFolderName() == null && request.getDescription() == null) {
            return toResponseDTO(current);
        }

        FolderVO.FolderVOBuilder voBuilder = FolderVO.builder()
            .folderId(folderId)
            .memberId(memberId);

        if (request.getFolderName() != null) {
            String trimmedName = request.getFolderName().trim();
            if (trimmedName.isEmpty()) {
                throw new BusinessException(ResultCode.INVALID_REQUEST, "폴더 이름은 비울 수 없습니다.");
            }
            if (!trimmedName.equals(current.getFolderName())) {
                int duplicateCount = folderMapper.countFolderName(memberId, trimmedName);
                if (duplicateCount > 0) {
                    throw new BusinessException(ResultCode.DUPLICATED, "같은 이름의 폴더가 이미 존재합니다.");
                }
            }
            voBuilder.folderName(trimmedName);
        }

        if (request.getDescription() != null) {
            voBuilder.description(request.getDescription().trim());
        }

        FolderVO update = voBuilder.build();

        int affected = folderMapper.updateFolder(update);
        if (affected != 1) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "폴더 수정에 실패했습니다.");
        }

        FolderVO updated = folderMapper.selectFolderById(folderId, memberId);
        if (updated == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "수정된 폴더를 찾을 수 없습니다.");
        }
        return toResponseDTO(updated);
    }

    @Override
    @Transactional
    public void deleteFolder(Long folderId) {
        FolderVO current = folderMapper.selectFolderById(folderId, memberId);
        if (current == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "폴더를 찾을 수 없습니다.");
        }

        folderMapper.detachSentenceSetsFromFolder(folderId, memberId);

        int affected = folderMapper.softDeleteFolder(folderId, memberId);
        if (affected != 1) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "폴더 삭제에 실패했습니다.");
        }
    }

    private FolderResponseDTO toResponseDTO(FolderVO folder) {
        return FolderResponseDTO.builder()
            .folderId(folder.getFolderId())
            .folderName(folder.getFolderName())
            .description(folder.getDescription())
            .displayOrder(folder.getDisplayOrder())
            .isDefault(folder.getIsDefault())
            .createdAt(folder.getCreatedAt())
            .updatedAt(folder.getUpdatedAt())
            .build();
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
