package com.liftoff.project.mapper;

import com.google.cloud.storage.Blob;
import com.liftoff.project.controller.storage.response.FileInfoResponseDTO;

public interface FileMapper {

    /**
     * Maps a Blob object to a FileInfoResponseDTO object.
     *
     * @param blob The Blob object to be mapped.
     * @return A FileInfoResponseDTO object representing the information extracted from the Blob.
     */
    FileInfoResponseDTO mapBlobToFileInfoResponse(Blob blob);

}
