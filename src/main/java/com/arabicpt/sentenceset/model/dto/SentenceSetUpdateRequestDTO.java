package com.arabicpt.sentenceset.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SentenceSetUpdateRequestDTO {

    private String setName;
    private String description;

    /** 변경 시 지정. null이면 폴더 필드는 변경하지 않음(단, clearFolder가 true면 무시). */
    private Long folderId;

    /** true이면 해당 세트를 폴더에서 뗌(FOLDER_ID = NULL). folderId보다 우선. */
    private Boolean clearFolder;
}
