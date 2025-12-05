package com.unifan.avabackv2.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "mind_map")
@Data
public class MindMapEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String url;

    @Lob
    @Column
    private byte[] fileContent;

    @Column
    private String transcription;

    @Column
    private String diagramPath;
}
