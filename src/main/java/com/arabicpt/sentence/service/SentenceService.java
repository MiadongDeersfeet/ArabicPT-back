package com.arabicpt.sentence.service;

import com.arabicpt.sentence.model.dto.SentenceCreateRequestDTO;
import com.arabicpt.sentence.model.dto.SentenceResponseDTO;
import com.arabicpt.sentence.model.dto.SentenceUpdateRequestDTO;
import java.util.List;

public interface SentenceService {

    List<SentenceResponseDTO> findSentencesBySetId(Long setId);

    SentenceResponseDTO createSentenceInSet(Long setId, SentenceCreateRequestDTO request);

    SentenceResponseDTO findSentence(Long sentenceId);

    SentenceResponseDTO updateSentence(Long sentenceId, SentenceUpdateRequestDTO request);

    void deleteSentence(Long sentenceId);
}
