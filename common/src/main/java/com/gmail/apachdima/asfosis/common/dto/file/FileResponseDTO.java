package com.gmail.apachdima.asfosis.common.dto.file;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FileResponseDTO(
    String storageId,
    String fileName,
    String contentType,
    long size,
    LocalDateTime created
) {


}
