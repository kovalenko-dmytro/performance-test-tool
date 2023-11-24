package com.gmail.apachdima.asfosis.file.storage.repository;

import com.gmail.apachdima.asfosis.file.storage.model.FileStorage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileStorageRepository extends JpaRepository<FileStorage, String> {

    Optional<FileStorage> findByStorageId(String storageId);
}
