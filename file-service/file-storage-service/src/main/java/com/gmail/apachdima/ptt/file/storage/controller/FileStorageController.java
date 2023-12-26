package com.gmail.apachdima.ptt.file.storage.controller;

import com.gmail.apachdima.ptt.common.dto.file.DownloadFileResponseDTO;
import com.gmail.apachdima.ptt.common.dto.file.FileResponseDTO;
import com.gmail.apachdima.ptt.file.storage.service.FileStorageService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFiles(
        @Parameter(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
        @RequestPart("files") MultipartFile[] files,
        @RequestParam(value = "locale", required = false, defaultValue = "en") Locale locale
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fileStorageService.upload(files, locale));
    }

    @GetMapping()
    public ResponseEntity<Page<FileResponseDTO>> findFiles(
        Pageable pageable,
        @RequestParam(value = "locale", required = false, defaultValue = "en") Locale locale
        ) {
        return ResponseEntity.ok().body(fileStorageService.findFiles(pageable, locale));
    }

    @DeleteMapping(value = "/{fileId}")
    public ResponseEntity<?> deleteById(
        @PathVariable("fileId") String fileId,
        @RequestParam(value = "locale", required = false, defaultValue = "en") Locale locale
    ) {
        fileStorageService.deleteById(fileId, locale);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(value = "/{fileId}")
    @ResponseBody
    public ResponseEntity<byte[]> download(
        @PathVariable("fileId") String fileId,
        @RequestParam(value = "locale", required = false, defaultValue = "en") Locale locale
    ) {
        DownloadFileResponseDTO response = fileStorageService.download(fileId, locale);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + response.fileName() + "\"")
            .body(response.resource());
    }
}
