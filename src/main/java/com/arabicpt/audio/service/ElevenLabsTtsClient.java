package com.arabicpt.audio.service;

import com.arabicpt.common.exception.BusinessException;
import com.arabicpt.common.response.ResultCode;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Component
public class ElevenLabsTtsClient {

    private static final Logger log = LoggerFactory.getLogger(ElevenLabsTtsClient.class);

    private final RestClient restClient;
    private final String apiKey;
    private final String voiceId;
    private final String modelId;
    private final String languageCode;
    private final String outputFormat;

    public ElevenLabsTtsClient(
        @Value("${elevenlabs.base-url}") String baseUrl,
        @Value("${elevenlabs.api-key:}") String apiKey,
        @Value("${elevenlabs.default-voice-id:}") String voiceId,
        @Value("${elevenlabs.default-model-id}") String modelId,
        @Value("${elevenlabs.default-language-code}") String languageCode,
        @Value("${elevenlabs.output-format}") String outputFormat
    ) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
        this.apiKey = apiKey;
        this.voiceId = voiceId;
        this.modelId = modelId;
        this.languageCode = languageCode;
        this.outputFormat = outputFormat;
    }

    public byte[] generateSpeech(String text) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "ELEVENLABS_API_KEY가 설정되지 않았습니다.");
        }
        if (voiceId == null || voiceId.isBlank() || voiceId.contains("나중에_넣")) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "elevenlabs.default-voice-id를 설정해 주세요.");
        }

        try {
            return restClient.post()
                .uri(
                    "/v1/text-to-speech/{voiceId}?output_format={outputFormat}",
                    voiceId,
                    outputFormat
                )
                .header("xi-api-key", apiKey)
                .header("Content-Type", "application/json")
                .body(Map.of(
                    "text", text,
                    "model_id", modelId,
                    "language_code", languageCode
                ))
                .retrieve()
                .body(byte[].class);
        } catch (RestClientResponseException e) {
            log.error(
                "ElevenLabs API 호출 실패 status={}, body={}",
                e.getStatusCode(),
                e.getResponseBodyAsString(),
                e
            );
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "음성 생성 API 호출에 실패했습니다.");
        } catch (Exception e) {
            log.error("ElevenLabs 음성 생성 중 예외 발생", e);
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "음성 생성 중 오류가 발생했습니다.");
        }
    }
}
