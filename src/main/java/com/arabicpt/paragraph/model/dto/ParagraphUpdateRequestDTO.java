package com.arabicpt.paragraph.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ParagraphUpdateRequestDTO {

    private String frontLang;
    private String frontText;
    private String backLang;
    private String backText;
    private String memo;
    private Integer displayOrder;
}
