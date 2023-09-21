package com.liftoff.project.mapper.impl;

import com.google.cloud.storage.Blob;
import com.liftoff.project.controller.storage.response.FileInfoResponseDTO;
import com.liftoff.project.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileMapperImpl implements FileMapper {

    private final String folderName;

    public FileMapperImpl(@Value("${firebase.storage.folderName}") String folderName) {
        this.folderName = folderName;
    }

    @Override
    public FileInfoResponseDTO mapBlobToFileInfoResponse(Blob blob) {
        String fileName = blob.getName().replace(folderName + "/", "");
        String uploadDate = blob.getCreateTime().toString();
        long fileSize = blob.getSize();
        String filePath = blob.getMediaLink();

        return FileInfoResponseDTO.builder()
                .fileName(fileName)
                .uploadDate(uploadDate)
                .fileSize(fileSize)
                .filePath(filePath)
                .build();
    }

}
