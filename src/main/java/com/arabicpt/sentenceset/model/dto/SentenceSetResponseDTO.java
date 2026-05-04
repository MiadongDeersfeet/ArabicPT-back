package com.arabicpt.sentenceset.model.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SentenceSetResponseDTO {
    private Long setId;
    private Long folderId;
    private String folderName;
    private String setName;
    private String description;
    private Integer displayOrder;
    private String isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
