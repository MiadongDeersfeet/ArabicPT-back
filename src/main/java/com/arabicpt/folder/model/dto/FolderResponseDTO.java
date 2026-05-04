package com.arabicpt.folder.model.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class FolderResponseDTO {
    private Long folderId;
    private String folderName;
    private String description;
    private Integer displayOrder;
    private String isDefault;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
