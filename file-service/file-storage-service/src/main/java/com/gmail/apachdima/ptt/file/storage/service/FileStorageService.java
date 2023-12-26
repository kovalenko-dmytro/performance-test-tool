package com.gmail.apachdima.ptt.file.storage.service;

import com.gmail.apachdima.ptt.common.dto.file.DownloadFileResponseDTO;
import com.gmail.apachdima.ptt.common.dto.file.FileResponseDTO;
import com.gmail.apachdima.ptt.file.storage.model.StoredFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Locale;

public interface FileStorageService {

    List<FileResponseDTO> upload(MultipartFile[] files, Locale locale);
    Page<FileResponseDTO> findFiles(Pageable pageable, Locale locale);
    void deleteById(String fileId, Locale locale);
    DownloadFileResponseDTO download(String fileId, Locale locale);
    StoredFile getStoredFileModel(String fileId, Locale locale);
}
