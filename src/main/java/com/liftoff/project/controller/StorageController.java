package com.liftoff.project.controller;

import com.liftoff.project.controller.response.FileInfoResponseDTO;
import com.liftoff.project.exception.storage.FileNotFoundException;
import com.liftoff.project.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/storage")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;


    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = storageService.uploadFile(file);
            return new ResponseEntity<>(fileUrl, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload file: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/files")
    public List<FileInfoResponseDTO> getFileList() {
        return storageService.getFileList();
    }

    @DeleteMapping("/delete/{fileName:.+}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) throws FileNotFoundException {
        try {
            storageService.deleteFile(fileName);
            return new ResponseEntity<>("File deleted successfully", HttpStatus.OK);
        } catch (FileNotFoundException e) {
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        }
    }

}
