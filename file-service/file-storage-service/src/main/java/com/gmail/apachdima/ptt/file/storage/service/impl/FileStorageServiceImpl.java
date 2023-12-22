package com.gmail.apachdima.ptt.file.storage.service.impl;

import com.gmail.apachdima.ptt.common.constant.message.Error;
import com.gmail.apachdima.ptt.common.constant.model.Model;
import com.gmail.apachdima.ptt.common.dto.file.DownloadFileResponseDTO;
import com.gmail.apachdima.ptt.common.dto.file.FileResponseDTO;
import com.gmail.apachdima.ptt.common.exception.EntityNotFoundException;
import com.gmail.apachdima.ptt.common.exception.PTTApplicationException;
import com.gmail.apachdima.ptt.file.storage.mapper.FileStorageMapper;
import com.gmail.apachdima.ptt.file.storage.model.StoredFile;
import com.gmail.apachdima.ptt.file.storage.repository.FileStorageRepository;
import com.gmail.apachdima.ptt.file.storage.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private final FileStorageRepository fileStorageRepository;
    private final FileStorageMapper fileStorageMapper;
    private final MessageSource messageSource;

    @Override
    public void upload(MultipartFile[] files, Locale locale) {
        List<StoredFile> storedFiles = new LinkedList<>();
        StoredFile storedFile;
        for (MultipartFile file : files) {
            try {
                storedFile = StoredFile.builder()
                    .fileName(file.getOriginalFilename())
                    .contentType(file.getContentType())
                    .size(file.getSize())
                    .bytes(file.getBytes())
                    .created(LocalDateTime.now())
                    .build();
            } catch (IOException e) {
                throw new PTTApplicationException(
                    messageSource.getMessage(
                        Error.FILE_STORAGE_UNABLE_UPLOAD.getKey(),
                        new Object[]{file.getOriginalFilename(), e.getMessage()}, locale));
            }
            storedFiles.add(storedFile);
        }
        fileStorageRepository.saveAll(storedFiles);
    }

    @Override
    public Page<FileResponseDTO> findFiles(Pageable pageable, Locale locale) {
        Page<StoredFile> savedFiles = fileStorageRepository.findAll(pageable);
        return savedFiles.isEmpty()
            ? Page.empty()
            : savedFiles.map(fileStorageMapper::toFileResponseDTO);
    }

    @Override
    public void deleteById(String fileId, Locale locale) {
        StoredFile storedFile = getById(fileId, locale);
        fileStorageRepository.delete(storedFile);
    }

    @Override
    public DownloadFileResponseDTO download(String fileId, Locale locale) {
        StoredFile storedFile = getById(fileId, locale);
        return DownloadFileResponseDTO.builder()
            .fileName(storedFile.getFileName())
            .resource(storedFile.getBytes())
            .build();
    }

    private StoredFile getById(String fileId, Locale locale) {
        Object[] params = new Object[]{Model.STORED_FILE.getName(), Model.Field.ID.getFieldName(), fileId};
        return fileStorageRepository
            .findById(fileId)
            .orElseThrow(() ->
                new EntityNotFoundException(messageSource.getMessage(Error.ENTITY_NOT_FOUND.getKey(), params, locale)));
    }
}
