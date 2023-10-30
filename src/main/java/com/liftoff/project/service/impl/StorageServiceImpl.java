package com.liftoff.project.service.impl;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.liftoff.project.controller.storage.response.FileInfoResponseDTO;
import com.liftoff.project.exception.TechnicalException;
import com.liftoff.project.mapper.FileMapper;
import com.liftoff.project.service.StorageService;
import jakarta.activation.MimetypesFileTypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
public class StorageServiceImpl implements StorageService {

    private final String bucketName;
    private final String folderName;
    private final long maxFileSize;
    private final Storage storage;
    private final FileMapper fileMapper;

    @Autowired
    public StorageServiceImpl(
            @Value("${firebase.storage.bucketName}") String bucketName,
            @Value("${firebase.storage.folderName}") String folderName,
            @Value("${firebase.storage.maxFileSize}") long maxFileSize,
            FileMapper fileMapper) throws IOException {
        this.bucketName = bucketName;
        this.folderName = folderName;
        this.maxFileSize = maxFileSize;
        this.fileMapper = fileMapper;
        storage = StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.
                        fromStream(getClass().getResourceAsStream("/firebase-key.json")))
                .build()
                .getService();
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        long fileSize = file.getSize();

        if (fileSize > maxFileSize) {
            throw new TechnicalException(
                    "File size exceeds the maximum allowed limit " + maxFileSize + " bytes.");
        }

        assert originalFileName != null;
        String uniqueFileName = generateUniqueFileName(originalFileName);
        String filePath = folderName + "/" + uniqueFileName;

        InputStream fileStream = file.getInputStream();

        BlobId blobId = BlobId.of(bucketName, filePath);

        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
        String contentType = mimeTypesMap.getContentType(originalFileName);

        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(contentType)
                .build();

        Blob blob = storage.create(blobInfo, fileStream);


        return blob.getMediaLink();
    }

    @Override
    public List<FileInfoResponseDTO> getFileList() {
        return StreamSupport.stream(
                        storage.get(bucketName)
                                .list()
                                .iterateAll()
                                .spliterator(), false)
                .map(fileMapper::mapBlobToFileInfoResponse)
                .toList();
    }

    @Override
    public boolean deleteFile(String fileName) throws TechnicalException {
        String filePath = folderName + "/" + fileName;

        BlobId blobId = BlobId.of(bucketName, filePath);
        boolean deleted = storage.delete(blobId);

        if (!deleted) {
            throw new TechnicalException("File not found");
        }

        return true;
    }

    private String generateUniqueFileName(String originalFileName) {
        UUID uuid = UUID.randomUUID();
        String extension = "";
        int dotIndex = originalFileName.lastIndexOf(".");
        if (dotIndex != -1) {
            extension = originalFileName.substring(dotIndex);
        }
        return uuid.toString() + extension;
    }
}