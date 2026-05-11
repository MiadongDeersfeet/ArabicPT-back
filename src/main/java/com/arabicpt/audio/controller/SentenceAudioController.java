package com.arabicpt.audio.controller;

import com.arabicpt.audio.model.dto.SentenceAudioResponseDTO;
import com.arabicpt.audio.service.SentenceAudioService;
import com.arabicpt.common.response.ApiResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sentences/{sentenceId}/audio")
public class SentenceAudioController {

    private final SentenceAudioService sentenceAudioService;

    public SentenceAudioController(SentenceAudioService sentenceAudioService) {
        this.sentenceAudioService = sentenceAudioService;
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<SentenceAudioResponseDTO>> generateSentenceAudio(
        @PathVariable Long sentenceId
    ) {
        SentenceAudioResponseDTO created = sentenceAudioService.generateSentenceAudio(sentenceId);
        return ResponseEntity.ok(ApiResponseDTO.success(created));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<SentenceAudioResponseDTO>> getActiveSentenceAudio(
        @PathVariable Long sentenceId
    ) {
        SentenceAudioResponseDTO data = sentenceAudioService.findActiveSentenceAudio(sentenceId);
        return ResponseEntity.ok(ApiResponseDTO.success(data));
    }
}
