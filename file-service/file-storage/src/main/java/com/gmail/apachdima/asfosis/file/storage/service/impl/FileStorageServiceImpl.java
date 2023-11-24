package com.gmail.apachdima.asfosis.file.storage.service.impl;

import com.gmail.apachdima.asfosis.common.constant.message.Error;
import com.gmail.apachdima.asfosis.common.constant.model.Model;
import com.gmail.apachdima.asfosis.common.dto.file.DownloadFileResponseDTO;
import com.gmail.apachdima.asfosis.common.dto.file.FileResponseDTO;
import com.gmail.apachdima.asfosis.common.exception.AFSApplicationException;
import com.gmail.apachdima.asfosis.common.exception.EntityNotFoundException;
import com.gmail.apachdima.asfosis.file.storage.mapper.FileStorageMapper;
import com.gmail.apachdima.asfosis.file.storage.model.FileStorage;
import com.gmail.apachdima.asfosis.file.storage.provider.FileStorageProvider;
import com.gmail.apachdima.asfosis.file.storage.repository.FileStorageRepository;
import com.gmail.apachdima.asfosis.file.storage.service.FileStorageService;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private final FileStorageProvider<Drive> googleDriveFileStorageProvider;
    private final FileStorageRepository fileStorageRepository;
    private final FileStorageMapper fileStorageMapper;
    private final MessageSource messageSource;

    @Override
    public void upload(MultipartFile file, Locale locale) {
        File fileMetadata = new File();
        fileMetadata.setName(file.getOriginalFilename());
        Drive service = googleDriveFileStorageProvider.provideService(Locale.ENGLISH);

        InputStreamContent mediaContent;
        File uploadedFile;
        try {
            mediaContent = new InputStreamContent(file.getContentType(), new BufferedInputStream(file.getInputStream()));
            mediaContent.setLength(file.getSize());
            uploadedFile = service.files()
                .create(fileMetadata, mediaContent)
                .setFields("id,name,mimeType,size")
                .execute();
        } catch (IOException e) {
            throw new AFSApplicationException(
                messageSource.getMessage(
                    Error.FILE_STORAGE_UNABLE_UPLOAD.getKey(), new Object[]{file.getName(), e.getMessage()}, locale));
        }
        fileStorageRepository
            .save(FileStorage.builder()
                .storageId(uploadedFile.getId())
                .fileName(uploadedFile.getName())
                .contentType(uploadedFile.getMimeType())
                .size(uploadedFile.getSize())
                .created(LocalDateTime.now())
                .build());
    }

    @Override
    public Page<FileResponseDTO> findFiles(Pageable pageable, Locale locale) {
        Page<FileStorage> savedFiles = fileStorageRepository.findAll(pageable);
        if (savedFiles.isEmpty()) {
            return Page.empty();
        }
        return savedFiles.map(fileStorageMapper::toFileResponseDTO);
    }

    @Override
    public void deleteByStorageId(String storageId, Locale locale) {
        FileStorage savedFile = getById(storageId, locale);
        Drive service = googleDriveFileStorageProvider.provideService(Locale.ENGLISH);
        try {
            service.files().delete(savedFile.getStorageId()).execute();
        } catch (IOException e) {
            throw new AFSApplicationException(
                messageSource.getMessage(
                    Error.FILE_STORAGE_UNABLE_READ.getKey(), new Object[]{storageId, e.getMessage()}, locale));
        }
        fileStorageRepository.delete(savedFile);
    }

    @Override
    public DownloadFileResponseDTO download(String storageId, Locale locale) {
        FileStorage savedFile = getById(storageId, locale);
        Drive service = googleDriveFileStorageProvider.provideService(Locale.ENGLISH);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            service.files().get(storageId).executeMediaAndDownloadTo(outputStream);
        } catch (IOException e) {
            throw new AFSApplicationException(
                messageSource.getMessage(
                    Error.FILE_STORAGE_UNABLE_READ.getKey(), new Object[]{storageId, e.getMessage()}, locale));
        }
        return DownloadFileResponseDTO.builder()
            .fileNme(savedFile.getFileName())
            .resource(outputStream.toByteArray())
            .build();
    }

    private FileStorage getById(String storageId, Locale locale) {
        Object[] params = new Object[]{Model.FILE_STORAGE.getName(), Model.Field.ID.getFieldName(), storageId};
        return fileStorageRepository
            .findByStorageId(storageId)
            .orElseThrow(() ->
                new EntityNotFoundException(messageSource.getMessage(Error.ENTITY_NOT_FOUND.getKey(), params, locale)));
    }
}
