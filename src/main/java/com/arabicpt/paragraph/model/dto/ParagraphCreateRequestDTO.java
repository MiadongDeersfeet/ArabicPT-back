package com.arabicpt.paragraph.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ParagraphCreateRequestDTO {

    private String frontLang;

    @NotBlank(message = "frontText는 필수입니다.")
    private String frontText;

    private String backLang;

    @NotBlank(message = "backText는 필수입니다.")
    private String backText;

    private String memo;

    private Integer displayOrder;
}
