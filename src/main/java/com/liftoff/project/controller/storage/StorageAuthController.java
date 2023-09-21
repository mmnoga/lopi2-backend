package com.liftoff.project.controller.storage;

import com.liftoff.project.configuration.security.annotations.HasAdminRole;
import com.liftoff.project.controller.storage.response.FileInfoResponseDTO;
import com.liftoff.project.exception.TechnicalException;
import com.liftoff.project.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/storage")
@RequiredArgsConstructor
@Tag(name = "Storage <Admin>")
public class StorageAuthController {

    private final StorageService storageService;

    @PostMapping(consumes = {"multipart/form-data"})
    @Operation(summary = "Upload a file",
            security = @SecurityRequirement(name = "bearerAuth"))
    @HasAdminRole
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = storageService.uploadFile(file);
            return new ResponseEntity<>(fileUrl, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload file: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    @Operation(summary = "Get a list of files",
            security = @SecurityRequirement(name = "bearerAuth"))
    @HasAdminRole
    public List<FileInfoResponseDTO> getFileList() {
        return storageService.getFileList();
    }

    @DeleteMapping("/{fileName:.+}")
    @Operation(summary = "Delete a file by its name",
            security = @SecurityRequirement(name = "bearerAuth"))
    @HasAdminRole
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) throws TechnicalException {
        try {
            storageService.deleteFile(fileName);
            return new ResponseEntity<>("File deleted successfully", HttpStatus.OK);
        } catch (TechnicalException e) {
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        }
    }

}