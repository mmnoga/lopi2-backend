package com.liftoff.project.controller.storage.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileInfoResponseDTO {

    private String fileName;
    private long fileSize;
    private String filePath;
    private String uploadDate;

}
