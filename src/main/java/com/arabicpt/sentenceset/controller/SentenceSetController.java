package com.arabicpt.sentenceset.controller;

import com.arabicpt.common.response.ApiResponseDTO;
import com.arabicpt.sentence.model.dto.SentenceCreateRequestDTO;
import com.arabicpt.sentence.model.dto.SentenceResponseDTO;
import com.arabicpt.sentence.service.SentenceService;
import com.arabicpt.sentenceset.model.dto.SentenceSetCreateRequestDTO;
import com.arabicpt.sentenceset.model.dto.SentenceSetResponseDTO;
import com.arabicpt.sentenceset.model.dto.SentenceSetUpdateRequestDTO;
import com.arabicpt.sentenceset.service.SentenceSetService;
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
@RequestMapping("/api/sets")
public class SentenceSetController {

    private final SentenceSetService sentenceSetService;
    private final SentenceService sentenceService;

    public SentenceSetController(SentenceSetService sentenceSetService, SentenceService sentenceService) {
        this.sentenceSetService = sentenceSetService;
        this.sentenceService = sentenceService;
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<SentenceSetResponseDTO>> createSentenceSet(
        @Valid @RequestBody SentenceSetCreateRequestDTO request
    ) {
        SentenceSetResponseDTO created = sentenceSetService.createSentenceSet(request);
        return ResponseEntity.ok(ApiResponseDTO.success(created));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<SentenceSetResponseDTO>>> getSentenceSets(
        @RequestParam(required = false) Long folderId
    ) {
        List<SentenceSetResponseDTO> sets = sentenceSetService.findSentenceSets(folderId);
        return ResponseEntity.ok(ApiResponseDTO.success(sets));
    }

    @GetMapping("/{setId}/sentences")
    public ResponseEntity<ApiResponseDTO<List<SentenceResponseDTO>>> getSentencesBySet(
        @PathVariable Long setId
    ) {
        List<SentenceResponseDTO> sentences = sentenceService.findSentencesBySetId(setId);
        return ResponseEntity.ok(ApiResponseDTO.success(sentences));
    }

    @PostMapping("/{setId}/sentences")
    public ResponseEntity<ApiResponseDTO<SentenceResponseDTO>> createSentenceInSet(
        @PathVariable Long setId,
        @Valid @RequestBody SentenceCreateRequestDTO request
    ) {
        SentenceResponseDTO created = sentenceService.createSentenceInSet(setId, request);
        return ResponseEntity.ok(ApiResponseDTO.success(created));
    }

    @GetMapping("/{setId}")
    public ResponseEntity<ApiResponseDTO<SentenceSetResponseDTO>> getSentenceSet(@PathVariable Long setId) {
        SentenceSetResponseDTO set = sentenceSetService.findSentenceSet(setId);
        return ResponseEntity.ok(ApiResponseDTO.success(set));
    }

    @PatchMapping("/{setId}")
    public ResponseEntity<ApiResponseDTO<SentenceSetResponseDTO>> updateSentenceSet(
        @PathVariable Long setId,
        @RequestBody SentenceSetUpdateRequestDTO request
    ) {
        SentenceSetResponseDTO updated = sentenceSetService.updateSentenceSet(setId, request);
        return ResponseEntity.ok(ApiResponseDTO.success(updated));
    }

    @DeleteMapping("/{setId}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteSentenceSet(@PathVariable Long setId) {
        sentenceSetService.deleteSentenceSet(setId);
        return ResponseEntity.ok(ApiResponseDTO.success());
    }
}
