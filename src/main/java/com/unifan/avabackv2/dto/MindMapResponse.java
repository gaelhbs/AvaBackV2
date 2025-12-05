package com.unifan.avabackv2.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class MindMapResponse {
    private String status;
    private String downloadUrl;
}
