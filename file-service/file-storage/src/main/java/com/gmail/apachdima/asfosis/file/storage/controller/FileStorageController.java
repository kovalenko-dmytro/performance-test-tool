package com.gmail.apachdima.asfosis.file.storage.controller;

import com.gmail.apachdima.asfosis.common.dto.file.DownloadFileResponseDTO;
import com.gmail.apachdima.asfosis.common.dto.file.FileResponseDTO;
import com.gmail.apachdima.asfosis.file.storage.service.FileStorageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;

@Tag(name = "File storage REST API")
@RestController
@RequestMapping(value = "/api/v1/files/storage")
@RequiredArgsConstructor
public class FileStorageController {

    private final FileStorageService fileStorageService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> uploadFile(
        @RequestParam("file") MultipartFile file,
        @RequestParam(value = "locale", required = false, defaultValue = "en") Locale locale
    ) {
        fileStorageService.upload(file, locale);
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity<Page<FileResponseDTO>> findFiles(
        Pageable pageable,
        @RequestParam(value = "locale", required = false, defaultValue = "en") Locale locale
        ) {
        return ResponseEntity.ok().body(fileStorageService.findFiles(pageable, locale));
    }

    @DeleteMapping(value = "/{storageId}")
    public ResponseEntity<FileResponseDTO> deleteByStorageId(
        @PathVariable("storageId") String storageId,
        @RequestParam(value = "locale", required = false, defaultValue = "en") Locale locale
    ) {
        fileStorageService.deleteByStorageId(storageId, locale);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{storageId}")
    @ResponseBody
    public ResponseEntity<byte[]> download(
        @PathVariable("storageId") String storageId,
        @RequestParam(value = "locale", required = false, defaultValue = "en") Locale locale
    ) {
        DownloadFileResponseDTO response = fileStorageService.download(storageId, locale);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + response.fileNme() + "\"")
            .body(response.resource());
    }
}
