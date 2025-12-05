package com.unifan.avabackv2.dto;

import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class MindMapRequest {

    private Long id;

    private String url;

    private byte[] fileContent;


}
