package com.stream.file.storage.service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileServiceImpl implements FileService {

    @Value("${root.location}")
    private String rootFilesLocation;

    private void checkFileExistence(String filePath) throws IOException {
        if ( Files.exists(Paths.get(filePath))) {
            throw new FileAlreadyExistsException("File with the same name already exists");
        }
    }

    private void createDirectoryIfNotExists(String directoryPath) throws IOException {
        Path path = Paths.get(directoryPath);
        if (!Files.exists(path)) {
                Files.createDirectories(path);
        }
    }


    @Override
    public ResponseEntity<InputStreamResource> downloadFile(String fullPath) {
        try {
            File file = new File(systemBasedUrl(fullPath));
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + extractFileName(fullPath));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (FileNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    private String extractFilePath(String uri)
    {
        String[] segments = uri.split(File.separator);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < segments.length - 1; i++) {
            result.append(segments[i]);
            if (i < segments.length - 2) {
                result.append(File.separator);
            }
        }
        return result.toString();
    }

    private String systemBasedUrl(String uri)
    {
        uri = rootFilesLocation+uri;
        String[] segments = uri.split("/");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < segments.length ; i++) {
            result.append(segments[i]);
            if (i < segments.length - 1) {
                result.append(File.separator);
            }
        }
        return result.toString();
    }

    @Override
    public void saveFile(MultipartFile file ,String fullPath) throws IOException {
        String fullSystemPath = systemBasedUrl(fullPath);
        createDirectoryIfNotExists(extractFilePath(fullSystemPath));
        checkFileExistence(fullSystemPath);
        Files.copy(file.getInputStream(), Paths.get(fullSystemPath));
    }

    private String extractFileName(String uri)
    {
        String[] segments = uri.split("/");
        String lastName = segments[segments.length - 1];
        return lastName;
    }



}
