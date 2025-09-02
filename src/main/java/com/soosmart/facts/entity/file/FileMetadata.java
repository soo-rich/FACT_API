package com.soosmart.facts.entity.file;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "file_metadata")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FileMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String originalFileName;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private Long fileSize;

    @Column(nullable = false)
    private String storageUrl;

    @Column(name = "uploadedBy")
    private String uploadedBy;

    @CreationTimestamp
    @Column(updatable = false, name = "createdat")
    private Instant createdat;
    @UpdateTimestamp
    private Instant update_at;

    @Builder.Default
    private Boolean supprimer = false;

    @Column(name = "storageProvider")
    private String storageProvider;

}
