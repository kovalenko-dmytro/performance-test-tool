package com.gmail.apachdima.asfosis.file.storage.service;

import com.gmail.apachdima.asfosis.common.dto.file.DownloadFileResponseDTO;
import com.gmail.apachdima.asfosis.common.dto.file.FileResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;

public interface FileStorageService {

    void upload(MultipartFile file, Locale locale);
    Page<FileResponseDTO> findFiles(Pageable pageable, Locale locale);
    void deleteByStorageId(String storageId, Locale locale);
    DownloadFileResponseDTO download(String storageId, Locale locale);
}
