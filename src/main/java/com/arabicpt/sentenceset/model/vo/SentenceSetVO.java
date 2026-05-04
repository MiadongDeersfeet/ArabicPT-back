package com.arabicpt.sentenceset.model.vo;

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
public class SentenceSetVO {
    private Long setId;
    private Long memberId;
    private Long folderId;
    private String setName;
    private String description;
    private Integer displayOrder;
    private String isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** UPDATE 전용: true면 FOLDER_ID를 NULL로 설정 */
    private Boolean detachFromFolder;
}
