package com.gmail.apachdima.ptt.file.storage.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "files")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class StoredFile {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "file_", columnDefinition = "bpchar", unique = true, nullable = false)
    private String fileId;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "file_size", nullable = false)
    private long size;

    @Column(name = "bytes", nullable = false)
    private byte[] bytes;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;
}
