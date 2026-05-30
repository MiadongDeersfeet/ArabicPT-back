package com.arabicpt.paragraphset.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ParagraphSetCreateRequestDTO {

    private Long folderId;

    @NotBlank(message = "setName은 필수입니다.")
    private String setName;

    private String description;
}
