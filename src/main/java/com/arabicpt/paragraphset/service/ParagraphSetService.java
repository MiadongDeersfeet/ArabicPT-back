package com.arabicpt.paragraphset.service;

import com.arabicpt.paragraphset.model.dto.ParagraphSetCreateRequestDTO;
import com.arabicpt.paragraphset.model.dto.ParagraphSetResponseDTO;
import com.arabicpt.paragraphset.model.dto.ParagraphSetUpdateRequestDTO;
import java.util.List;

public interface ParagraphSetService {

    ParagraphSetResponseDTO createParagraphSet(ParagraphSetCreateRequestDTO request);

    List<ParagraphSetResponseDTO> findParagraphSets(Long folderId);

    ParagraphSetResponseDTO findParagraphSet(Long paragraphSetId);

    ParagraphSetResponseDTO updateParagraphSet(Long paragraphSetId, ParagraphSetUpdateRequestDTO request);

    void deleteParagraphSet(Long paragraphSetId);
}
