package com.arabicpt.paragraph.model.dto;

import com.arabicpt.audio.model.dto.ParagraphAudioResponseDTO;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParagraphResponseDTO {
    private Long paragraphId;
    private Long paragraphSetId;
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
    private ParagraphAudioResponseDTO audio;
}
