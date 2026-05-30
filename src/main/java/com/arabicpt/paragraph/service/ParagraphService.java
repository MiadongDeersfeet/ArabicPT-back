package com.arabicpt.paragraph.service;

import com.arabicpt.paragraph.model.dto.ParagraphCreateRequestDTO;
import com.arabicpt.paragraph.model.dto.ParagraphResponseDTO;
import com.arabicpt.paragraph.model.dto.ParagraphUpdateRequestDTO;
import java.util.List;

public interface ParagraphService {

    List<ParagraphResponseDTO> findParagraphsBySetId(Long paragraphSetId);

    ParagraphResponseDTO createParagraphInSet(Long paragraphSetId, ParagraphCreateRequestDTO request);

    ParagraphResponseDTO findParagraph(Long paragraphId);

    ParagraphResponseDTO updateParagraph(Long paragraphId, ParagraphUpdateRequestDTO request);

    void deleteParagraph(Long paragraphId);
}
