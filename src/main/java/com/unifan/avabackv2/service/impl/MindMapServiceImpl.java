package com.unifan.avabackv2.service.impl;

import com.unifan.avabackv2.component.TranscriptionComponent;
import com.unifan.avabackv2.component.VideoProcessingComponent;
import com.unifan.avabackv2.dto.MindMapRequest;
import com.unifan.avabackv2.entity.MindMapEntity;
import com.unifan.avabackv2.mapper.MindMapMapper;
import com.unifan.avabackv2.repository.MindMapRepository;
import com.unifan.avabackv2.service.MindMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class MindMapServiceImpl implements MindMapService {

    @Autowired
    private VideoProcessingComponent videoProcessingComponent;

    @Autowired
    private TranscriptionComponent transcriptionComponent;

    @Autowired
    private MindMapRepository mindMapRepository;

    @Autowired
    private MindMapMapper mindMapMapper;

    private final WebClient webClient = WebClient.create();

    @Override
    public void processVideo(String url, byte[] fileContent) {
        byte[] audioContent;
        if (url != null) {
            videoProcessingComponent.downloadVideo(url);
            Path videoPath = Paths.get("/tmp/video.mp4");
            audioContent = videoProcessingComponent.extractAudio(videoPath.toFile());
        } else if (fileContent != null) {
            Path tempVideoPath = Paths.get("/tmp/uploaded_video.mp4");
            try {
                Files.write(tempVideoPath, fileContent);
            } catch (IOException e) {
                throw new RuntimeException("Failed to write uploaded video to temporary file", e);
            }
            audioContent = videoProcessingComponent.extractAudio(tempVideoPath.toFile());
        } else {
            throw new IllegalArgumentException("Either URL or file content must be provided.");
        }

        String transcription = transcriptionComponent.transcribeAudio(audioContent);
        String jsonResponse = sendToMakeWebhook(transcription);
        generateMermaidDiagram(jsonResponse);

        // Map request to entity and save to the database
        MindMapRequest request = MindMapRequest.builder()
                .url(url)
                .fileContent(fileContent)
                .build();
        MindMapEntity entity = mindMapMapper.toEntity(request);
        entity.setTranscription(transcription);
        entity.setDiagramPath("/tmp/diagram.png");
        mindMapRepository.save(request);
    }

    private String sendToMakeWebhook(String transcription) {
        return webClient.post()
                .uri("${make.webhook.url}")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"transcription\":\"" + transcription + "\"}")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    private void generateMermaidDiagram(String jsonResponse) {
        try {
            // Convert JSON to Mermaid syntax (placeholder logic)
            String mermaidSyntax = "graph TD;" + jsonResponse;

            // Generate PNG using Mermaid CLI (placeholder logic)
            File mermaidFile = new File("/tmp/diagram.mmd");
            try (FileOutputStream fos = new FileOutputStream(mermaidFile)) {
                fos.write(mermaidSyntax.getBytes());
            }

            Process process = new ProcessBuilder("mmdc", "-i", "/tmp/diagram.mmd", "-o", "/tmp/diagram.png").start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to generate Mermaid diagram", e);
        }
    }
}
