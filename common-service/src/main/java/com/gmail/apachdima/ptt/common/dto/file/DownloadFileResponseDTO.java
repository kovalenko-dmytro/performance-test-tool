package com.gmail.apachdima.ptt.common.dto.file;

import lombok.Builder;

@Builder
public record DownloadFileResponseDTO(
    String fileName,
    byte[] resource
) {


}
