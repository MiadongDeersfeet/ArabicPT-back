package com.arabicpt.folder.service;

import com.arabicpt.folder.model.dto.FolderCreateRequestDTO;
import com.arabicpt.folder.model.dto.FolderResponseDTO;
import com.arabicpt.folder.model.dto.FolderUpdateRequestDTO;
import com.arabicpt.folder.model.vo.FolderVO;
import java.util.List;

public interface FolderService {

    /**
     * 폴더 조회
     */
    List<FolderResponseDTO> findFolders();

    /**
     * 폴더 생성
     */
    FolderResponseDTO createFolder(FolderCreateRequestDTO request);

    /**
     * 기본 폴더 조회 또는 생성
     */
    FolderVO findOrCreateDefaultFolder(Long memberId);

    FolderResponseDTO findFolder(Long folderId);

    FolderResponseDTO updateFolder(Long folderId, FolderUpdateRequestDTO request);

    void deleteFolder(Long folderId);
}
