package com.arabicpt.audio.service;

import com.arabicpt.audio.model.dto.ParagraphAudioResponseDTO;

public interface ParagraphAudioService {

    ParagraphAudioResponseDTO generateParagraphAudio(Long paragraphId);

    ParagraphAudioResponseDTO findActiveParagraphAudio(Long paragraphId);
}
