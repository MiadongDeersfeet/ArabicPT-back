package com.arabicpt.audio.controller;

import com.arabicpt.audio.model.dto.ParagraphAudioResponseDTO;
import com.arabicpt.audio.service.ParagraphAudioService;
import com.arabicpt.common.response.ApiResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/paragraphs/{paragraphId}/audio")
public class ParagraphAudioController {

    private final ParagraphAudioService paragraphAudioService;

    public ParagraphAudioController(ParagraphAudioService paragraphAudioService) {
        this.paragraphAudioService = paragraphAudioService;
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<ParagraphAudioResponseDTO>> generateParagraphAudio(
        @PathVariable Long paragraphId
    ) {
        ParagraphAudioResponseDTO created = paragraphAudioService.generateParagraphAudio(paragraphId);
        return ResponseEntity.ok(ApiResponseDTO.success(created));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<ParagraphAudioResponseDTO>> getActiveParagraphAudio(
        @PathVariable Long paragraphId
    ) {
        ParagraphAudioResponseDTO data = paragraphAudioService.findActiveParagraphAudio(paragraphId);
        return ResponseEntity.ok(ApiResponseDTO.success(data));
    }
}
