package com.arabicpt.paragraphset.model.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParagraphSetResponseDTO {
    private Long paragraphSetId;
    private Long folderId;
    private String folderName;
    private String setName;
    private String description;
    private Integer displayOrder;
    private String isActive;
    private Integer paragraphCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
