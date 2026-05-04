package com.arabicpt.sentenceset.service;

import com.arabicpt.sentenceset.model.dto.SentenceSetCreateRequestDTO;
import com.arabicpt.sentenceset.model.dto.SentenceSetResponseDTO;
import com.arabicpt.sentenceset.model.dto.SentenceSetUpdateRequestDTO;
import java.util.List;

public interface SentenceSetService {

    SentenceSetResponseDTO createSentenceSet(SentenceSetCreateRequestDTO request);

    List<SentenceSetResponseDTO> findSentenceSets(Long folderId);

    SentenceSetResponseDTO findSentenceSet(Long setId);

    SentenceSetResponseDTO updateSentenceSet(Long setId, SentenceSetUpdateRequestDTO request);

    void deleteSentenceSet(Long setId);
}
