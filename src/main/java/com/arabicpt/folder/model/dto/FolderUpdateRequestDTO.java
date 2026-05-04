package com.arabicpt.folder.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FolderUpdateRequestDTO {
    private String folderName;
    private String description;
}
