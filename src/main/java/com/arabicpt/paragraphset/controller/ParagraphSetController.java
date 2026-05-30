package com.arabicpt.paragraphset.controller;

import com.arabicpt.common.response.ApiResponseDTO;
import com.arabicpt.paragraph.model.dto.ParagraphCreateRequestDTO;
import com.arabicpt.paragraph.model.dto.ParagraphResponseDTO;
import com.arabicpt.paragraph.service.ParagraphService;
import com.arabicpt.paragraphset.model.dto.ParagraphSetCreateRequestDTO;
import com.arabicpt.paragraphset.model.dto.ParagraphSetResponseDTO;
import com.arabicpt.paragraphset.model.dto.ParagraphSetUpdateRequestDTO;
import com.arabicpt.paragraphset.service.ParagraphSetService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/paragraph-sets")
public class ParagraphSetController {

    private final ParagraphSetService paragraphSetService;
    private final ParagraphService paragraphService;

    public ParagraphSetController(ParagraphSetService paragraphSetService, ParagraphService paragraphService) {
        this.paragraphSetService = paragraphSetService;
        this.paragraphService = paragraphService;
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<ParagraphSetResponseDTO>> createParagraphSet(
        @Valid @RequestBody ParagraphSetCreateRequestDTO request
    ) {
        ParagraphSetResponseDTO created = paragraphSetService.createParagraphSet(request);
        return ResponseEntity.ok(ApiResponseDTO.success(created));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<ParagraphSetResponseDTO>>> getParagraphSets(
        @RequestParam(required = false) Long folderId
    ) {
        List<ParagraphSetResponseDTO> sets = paragraphSetService.findParagraphSets(folderId);
        return ResponseEntity.ok(ApiResponseDTO.success(sets));
    }

    @GetMapping("/{paragraphSetId}/paragraphs")
    public ResponseEntity<ApiResponseDTO<List<ParagraphResponseDTO>>> getParagraphsBySet(
        @PathVariable Long paragraphSetId
    ) {
        List<ParagraphResponseDTO> paragraphs = paragraphService.findParagraphsBySetId(paragraphSetId);
        return ResponseEntity.ok(ApiResponseDTO.success(paragraphs));
    }

    @PostMapping("/{paragraphSetId}/paragraphs")
    public ResponseEntity<ApiResponseDTO<ParagraphResponseDTO>> createParagraphInSet(
        @PathVariable Long paragraphSetId,
        @Valid @RequestBody ParagraphCreateRequestDTO request
    ) {
        ParagraphResponseDTO created = paragraphService.createParagraphInSet(paragraphSetId, request);
        return ResponseEntity.ok(ApiResponseDTO.success(created));
    }

    @GetMapping("/{paragraphSetId}")
    public ResponseEntity<ApiResponseDTO<ParagraphSetResponseDTO>> getParagraphSet(
        @PathVariable Long paragraphSetId
    ) {
        ParagraphSetResponseDTO set = paragraphSetService.findParagraphSet(paragraphSetId);
        return ResponseEntity.ok(ApiResponseDTO.success(set));
    }

    @PatchMapping("/{paragraphSetId}")
    public ResponseEntity<ApiResponseDTO<ParagraphSetResponseDTO>> updateParagraphSet(
        @PathVariable Long paragraphSetId,
        @RequestBody ParagraphSetUpdateRequestDTO request
    ) {
        ParagraphSetResponseDTO updated = paragraphSetService.updateParagraphSet(paragraphSetId, request);
        return ResponseEntity.ok(ApiResponseDTO.success(updated));
    }

    @DeleteMapping("/{paragraphSetId}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteParagraphSet(@PathVariable Long paragraphSetId) {
        paragraphSetService.deleteParagraphSet(paragraphSetId);
        return ResponseEntity.ok(ApiResponseDTO.success());
    }
}
