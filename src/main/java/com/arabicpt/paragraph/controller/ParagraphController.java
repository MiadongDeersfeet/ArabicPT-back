package com.arabicpt.paragraph.controller;

import com.arabicpt.common.response.ApiResponseDTO;
import com.arabicpt.paragraph.model.dto.ParagraphResponseDTO;
import com.arabicpt.paragraph.model.dto.ParagraphUpdateRequestDTO;
import com.arabicpt.paragraph.service.ParagraphService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/paragraphs")
public class ParagraphController {

    private final ParagraphService paragraphService;

    public ParagraphController(ParagraphService paragraphService) {
        this.paragraphService = paragraphService;
    }

    @GetMapping("/{paragraphId}")
    public ResponseEntity<ApiResponseDTO<ParagraphResponseDTO>> getParagraph(@PathVariable Long paragraphId) {
        ParagraphResponseDTO paragraph = paragraphService.findParagraph(paragraphId);
        return ResponseEntity.ok(ApiResponseDTO.success(paragraph));
    }

    @PatchMapping("/{paragraphId}")
    public ResponseEntity<ApiResponseDTO<ParagraphResponseDTO>> updateParagraph(
        @PathVariable Long paragraphId,
        @RequestBody ParagraphUpdateRequestDTO request
    ) {
        ParagraphResponseDTO updated = paragraphService.updateParagraph(paragraphId, request);
        return ResponseEntity.ok(ApiResponseDTO.success(updated));
    }

    @DeleteMapping("/{paragraphId}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteParagraph(@PathVariable Long paragraphId) {
        paragraphService.deleteParagraph(paragraphId);
        return ResponseEntity.ok(ApiResponseDTO.success());
    }
}
