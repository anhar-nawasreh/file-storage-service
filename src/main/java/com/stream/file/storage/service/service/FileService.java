package com.stream.file.storage.service.service;

import com.stream.file.storage.service.model.UploadedFileDTO;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {


    void saveFile(MultipartFile  file, String fullPath) throws IOException;

    ResponseEntity<InputStreamResource> downloadFile(String fullPath);


}
