package com.arabicpt.sentence.model.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SentenceResponseDTO {
    private Long sentenceId;
    private Long setId;
    private String setName;
    private String frontLang;
    private String frontText;
    private String backLang;
    private String backText;
    private String memo;
    private Integer displayOrder;
    private String isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
