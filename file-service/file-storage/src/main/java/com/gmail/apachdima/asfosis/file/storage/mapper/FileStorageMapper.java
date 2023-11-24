package com.gmail.apachdima.asfosis.file.storage.mapper;

import com.gmail.apachdima.asfosis.common.dto.file.FileResponseDTO;
import com.gmail.apachdima.asfosis.file.storage.model.FileStorage;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface FileStorageMapper {

    FileResponseDTO toFileResponseDTO(FileStorage file);
}
