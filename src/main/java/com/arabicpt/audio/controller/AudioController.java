package com.arabicpt.audio.controller;

import com.arabicpt.audio.model.dto.SentenceAudioCreateRequestDTO;
import com.arabicpt.audio.model.dto.SentenceAudioResponseDTO;
import com.arabicpt.audio.service.AudioService;
import com.arabicpt.common.response.ApiResponseDTO;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sentences/{sentenceId}/audio")
public class AudioController {

    private final AudioService audioService;

    public AudioController(AudioService audioService) {
        this.audioService = audioService;
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<SentenceAudioResponseDTO>> createSentenceAudio(
        @PathVariable Long sentenceId,
        @RequestBody SentenceAudioCreateRequestDTO request
    ) {
        SentenceAudioResponseDTO created = audioService.createSentenceAudio(sentenceId, request);
        return ResponseEntity.ok(ApiResponseDTO.success(created));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<SentenceAudioResponseDTO>>> getSentenceAudioList(
        @PathVariable Long sentenceId
    ) {
        List<SentenceAudioResponseDTO> list = audioService.findSentenceAudioList(sentenceId);
        return ResponseEntity.ok(ApiResponseDTO.success(list));
    }
}
