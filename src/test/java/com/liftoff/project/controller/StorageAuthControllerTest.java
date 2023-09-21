package com.liftoff.project.controller;

import com.liftoff.project.controller.storage.StorageAuthController;
import com.liftoff.project.controller.storage.response.FileInfoResponseDTO;
import com.liftoff.project.exception.TechnicalException;
import com.liftoff.project.service.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class StorageAuthControllerTest {
    @Mock
    private StorageService storageService;
    private StorageAuthController storageAuthController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        storageAuthController = new StorageAuthController(storageService);
    }

    @Test
    void shouldReturnLinkForUploadedFile() throws IOException {
        MultipartFile file = new MockMultipartFile("test-file.txt", "Hello, World!".getBytes());

        when(storageService.uploadFile(file)).thenReturn("http://lopi2-backend.com/test-file.txt");

        ResponseEntity<String> response = storageAuthController.uploadFile(file);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("http://lopi2-backend.com/test-file.txt", response.getBody());

        verify(storageService, times(1)).uploadFile(file);
        verifyNoMoreInteractions(storageService);
    }

    @Test
    void shouldReturnErrorOnUploadFileFailure() throws IOException {
        MultipartFile file = new MockMultipartFile("test-file.txt", "Hello, World!".getBytes());

        when(storageService.uploadFile(file)).thenThrow(new IOException("Upload failed"));

        ResponseEntity<String> response = storageAuthController.uploadFile(file);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to upload file: Upload failed", response.getBody());

        verify(storageService, times(1)).uploadFile(file);
        verifyNoMoreInteractions(storageService);
    }

    @Test
    void shouldReturnListOfUploadedFiles() {
        FileInfoResponseDTO fileInfo = FileInfoResponseDTO.builder()
                .fileName("test-file.txt")
                .filePath("http://lopi2-backend.com/test-file.txt")
                .build();

        List<FileInfoResponseDTO> fileList = Collections.singletonList(fileInfo);

        when(storageService.getFileList()).thenReturn(fileList);

        List<FileInfoResponseDTO> response = storageAuthController.getFileList();

        assertEquals(fileList, response);

        verify(storageService, times(1)).getFileList();
        verifyNoMoreInteractions(storageService);
    }

    @Test
    void shouldDeleteFileByItsName() throws TechnicalException {
        String fileName = "to-delete-test-file.txt";

        ResponseEntity<String> response = storageAuthController.deleteFile(fileName);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("File deleted successfully", response.getBody());

        verify(storageService, times(1)).deleteFile(fileName);
        verifyNoMoreInteractions(storageService);
    }

    @Test
    void shouldResponseFileNotFoundOnDeleteNoExistingFile() throws TechnicalException {
        String fileName = "non-existent-file.txt";

        doThrow(new TechnicalException("File not found")).when(storageService).deleteFile(fileName);

        ResponseEntity<String> response = storageAuthController.deleteFile(fileName);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("File not found", response.getBody());

        verify(storageService, times(1)).deleteFile(fileName);
        verifyNoMoreInteractions(storageService);
    }

}