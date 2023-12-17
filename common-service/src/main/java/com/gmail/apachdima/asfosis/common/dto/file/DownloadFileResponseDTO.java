package com.gmail.apachdima.asfosis.common.dto.file;

import lombok.Builder;

@Builder
public record DownloadFileResponseDTO(
    String fileNme,
    byte[] resource
) {


}
