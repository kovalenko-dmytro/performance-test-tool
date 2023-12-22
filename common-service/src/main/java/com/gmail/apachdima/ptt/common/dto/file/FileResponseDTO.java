package com.gmail.apachdima.ptt.common.dto.file;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FileResponseDTO(
    String fileId,
    String fileName,
    String contentType,
    long size,
    LocalDateTime created
) {


}
