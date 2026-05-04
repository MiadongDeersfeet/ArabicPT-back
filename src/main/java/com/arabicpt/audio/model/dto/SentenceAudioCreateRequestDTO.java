package com.arabicpt.audio.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SentenceAudioCreateRequestDTO {
    private String languageCode;
    private String voiceName;
    private String audioType;
    private String speakingRate;
    private String audioUrl;
    private String storageKey;
    private String provider;
    private String format;
}
