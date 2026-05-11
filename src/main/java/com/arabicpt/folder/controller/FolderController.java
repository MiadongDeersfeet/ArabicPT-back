:ㅉㅂ
package com.arabicpt.folder.controller;

import com.arabicpt.common.response.ApiResponseDTO;
import com.arabicpt.folder.model.dto.FolderCreateRequestDTO;
import com.arabicpt.folder.model.dto.FolderResponseDTO;
import com.arabicpt.folder.model.dto.FolderUpdateRequestDTO;
import com.arabicpt.folder.service.FolderService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/folders")
public class FolderController {
    private final FolderService folderService;

    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<FolderResponseDTO>>> getFolders() {
        List<FolderResponseDTO> folders = folderService.findFolders();
        return ResponseEntity.ok(ApiResponseDTO.success(folders));
    }

    @GetMapping("/{folderId}")
    public ResponseEntity<ApiResponseDTO<FolderResponseDTO>> getFolder(@PathVariable Long folderId) {
        FolderResponseDTO folder = folderService.findFolder(folderId);
        return ResponseEntity.ok(ApiResponseDTO.success(folder));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<FolderResponseDTO>> createFolder(
        @Valid @RequestBody FolderCreateRequestDTO request
    ) {
        FolderResponseDTO folder = folderService.createFolder(request);
        return ResponseEntity.ok(ApiResponseDTO.success(folder));
    }

    @PatchMapping("/{folderId}")
    public ResponseEntity<ApiResponseDTO<FolderResponseDTO>> updateFolder(
        @PathVariable Long folderId,
        @RequestBody FolderUpdateRequestDTO request
    ) {
        FolderResponseDTO folder = folderService.updateFolder(folderId, request);
        return ResponseEntity.ok(ApiResponseDTO.success(folder));
    }

    @DeleteMapping("/{folderId}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteFolder(@PathVariable Long folderId) {
        folderService.deleteFolder(folderId);
        return ResponseEntity.ok(ApiResponseDTO.success());
    }
}
