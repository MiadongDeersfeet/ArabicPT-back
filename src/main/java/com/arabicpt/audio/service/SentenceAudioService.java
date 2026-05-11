package com.arabicpt.audio.service;

import com.arabicpt.audio.model.dto.SentenceAudioResponseDTO;

public interface SentenceAudioService {

    SentenceAudioResponseDTO generateSentenceAudio(Long sentenceId);

    SentenceAudioResponseDTO findActiveSentenceAudio(Long sentenceId);
}
