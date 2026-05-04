package com.arabicpt.sentence.controller;

import com.arabicpt.common.response.ApiResponseDTO;
import com.arabicpt.sentence.model.dto.SentenceResponseDTO;
import com.arabicpt.sentence.model.dto.SentenceUpdateRequestDTO;
import com.arabicpt.sentence.service.SentenceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sentences")
public class SentenceController {
    private final SentenceService sentenceService;

    public SentenceController(SentenceService sentenceService) {
        this.sentenceService = sentenceService;
    }

    @GetMapping("/{sentenceId}")
    public ResponseEntity<ApiResponseDTO<SentenceResponseDTO>> getSentence(@PathVariable Long sentenceId) {
        SentenceResponseDTO sentence = sentenceService.findSentence(sentenceId);
        return ResponseEntity.ok(ApiResponseDTO.success(sentence));
    }

    @PatchMapping("/{sentenceId}")
    public ResponseEntity<ApiResponseDTO<SentenceResponseDTO>> updateSentence(
        @PathVariable Long sentenceId,
        @RequestBody SentenceUpdateRequestDTO request
    ) {
        SentenceResponseDTO updated = sentenceService.updateSentence(sentenceId, request);
        return ResponseEntity.ok(ApiResponseDTO.success(updated));
    }

    @DeleteMapping("/{sentenceId}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteSentence(@PathVariable Long sentenceId) {
        sentenceService.deleteSentence(sentenceId);
        return ResponseEntity.ok(ApiResponseDTO.success());
    }
}
