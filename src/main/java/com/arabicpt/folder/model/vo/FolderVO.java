package com.arabicpt.folder.model.vo;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FolderVO {
    private Long folderId;
    private Long memberId;
    private String folderName;
    private String description;
    private Integer displayOrder;
    private String isDefault;
    private String isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
