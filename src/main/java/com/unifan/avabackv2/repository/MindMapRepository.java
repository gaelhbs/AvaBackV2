package com.unifan.avabackv2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unifan.avabackv2.dto.MindMapRequest;

@Repository
public interface MindMapRepository extends JpaRepository<MindMapRequest, Long> {
    // Custom query methods can be added here if needed
}
