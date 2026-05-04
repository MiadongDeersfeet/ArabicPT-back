package com.arabicpt.sentence.model.vo;

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
public class SentenceVO {
    private Long sentenceId;
    private Long memberId;
    private Long setId;
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
