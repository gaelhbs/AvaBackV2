package com.unifan.avabackv2.mapper;

import com.unifan.avabackv2.dto.MindMapRequest;
import com.unifan.avabackv2.entity.MindMapEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MindMapMapper {
    MindMapRequest toRequest(MindMapEntity entity);
    MindMapEntity toEntity(MindMapRequest request);
}
