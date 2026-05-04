package com.arabicpt.sentenceset.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SentenceSetCreateRequestDTO {

    private Long folderId;

    @NotBlank(message = "setName은 필수입니다.")
    private String setName;

    private String description;
}
