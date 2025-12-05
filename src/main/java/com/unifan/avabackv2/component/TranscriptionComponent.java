package com.unifan.avabackv2.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TranscriptionComponent {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    public String transcribeAudio(byte[] audioContent) {
        // Placeholder for OpenAI Whisper API call
        return "Transcription result";
    }
}
