package com.liftoff.project.service;

import com.liftoff.project.controller.storage.response.FileInfoResponseDTO;
import com.liftoff.project.exception.TechnicalException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface StorageService {
    /**
     * Uploads a file to the storage bucket.
     *
     * @param file The {@link MultipartFile} to be uploaded.
     * @return The URL of the uploaded file in the storage bucket.
     * @throws IOException        If an I/O error occurs while uploading the file.
     * @throws TechnicalException If the size of the uploaded file exceeds the maximum allowed limit.
     */
    String uploadFile(MultipartFile file) throws IOException;

    /**
     * Retrieves a list of file information from the Firebase Cloud Storage.
     *
     * @return A list of FileInfoResponseDTO objects containing information about each file.
     */
    List<FileInfoResponseDTO> getFileList();

    /**
     * Deletes a file from the storage bucket by its filename.
     *
     * @param fileName the name of the file to be deleted
     * @return {@code true} if the file was successfully deleted
     * @throws TechnicalException if the specified file is not found
     */
    boolean deleteFile(String fileName) throws TechnicalException;

}
