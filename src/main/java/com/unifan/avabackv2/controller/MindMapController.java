package com.unifan.avabackv2.controller;

import com.unifan.avabackv2.dto.MindMapRequest;
import com.unifan.avabackv2.dto.MindMapResponse;
import com.unifan.avabackv2.service.MindMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/mindmap")
public class MindMapController {

    @Autowired
    private MindMapService mindMapService;

    @PostMapping("/generate")
    public ResponseEntity<MindMapResponse> generateMindMap(@RequestParam(value = "file", required = false) MultipartFile file,
                                                           @RequestParam(value = "url", required = false) String url) throws IOException {
        MindMapRequest request = MindMapRequest.builder()
                .url(url)
                .fileContent(file != null ? file.getBytes() : null)
                .build();
        mindMapService.processVideo(request.getUrl(), request.getFileContent());
        return ResponseEntity.ok(MindMapResponse.builder()
                .status("Processing")
                .build());
    }
}