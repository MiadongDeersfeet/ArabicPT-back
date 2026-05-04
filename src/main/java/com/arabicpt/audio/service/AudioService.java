package com.arabicpt.audio.service;

import com.arabicpt.audio.model.dto.SentenceAudioCreateRequestDTO;
import com.arabicpt.audio.model.dto.SentenceAudioResponseDTO;
import java.util.List;

public interface AudioService {

    SentenceAudioResponseDTO createSentenceAudio(Long sentenceId, SentenceAudioCreateRequestDTO request);

    List<SentenceAudioResponseDTO> findSentenceAudioList(Long sentenceId);
}
