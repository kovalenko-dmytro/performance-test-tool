package com.gmail.apachdima.ptt.file.storage.repository;

import com.gmail.apachdima.ptt.file.storage.model.StoredFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface FileStorageRepository extends JpaRepository<StoredFile, String> {
}
