package com.arabicpt.audio.model.vo;

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
public class SentenceAudioVO {
    private Long audioId;
    private Long sentenceId;
    private String languageCode;
    private String voiceName;
    private String audioType;
    private String speakingRate;
    private String audioUrl;
    private String storageKey;
    private String provider;
    private String format;
    private String isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
