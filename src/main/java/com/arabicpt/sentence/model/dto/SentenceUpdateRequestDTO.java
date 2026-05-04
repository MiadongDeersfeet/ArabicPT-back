package com.arabicpt.sentence.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SentenceUpdateRequestDTO {
    private String frontLang;
    private String frontText;
    private String backLang;
    private String backText;
    private String memo;
    private Integer displayOrder;
}
