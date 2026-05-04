package com.arabicpt.folder.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FolderCreateRequestDTO {
    @NotBlank(message = "folderName은 필수입니다.")
    private String folderName;

    private String description;
}
