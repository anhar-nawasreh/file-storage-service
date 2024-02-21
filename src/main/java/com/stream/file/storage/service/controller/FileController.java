package com.stream.file.storage.service.controller;

import com.stream.file.storage.service.model.UploadFileResponseDTO;
import com.stream.file.storage.service.model.UploadedFileDTO;
import com.stream.file.storage.service.service.FileService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.InetAddress;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    final private FileService fileService;
    @PostMapping("/**")
    public ResponseEntity<UploadFileResponseDTO> uploadFile( @ModelAttribute MultipartFile file , HttpServletRequest request) {
        try {

            System.out.println(request.getRequestURI().split("files")[1]);
            fileService.saveFile(file, request.getRequestURI().split("files")[1]);
            Link selfLink = Link.of( InetAddress.getLocalHost()+":"+request.getLocalPort()+ request.getRequestURI().split("files")[1],
                    "create-resource");
            Link fileLocationLink = Link.of( InetAddress.getLocalHost()+":"+request.getLocalPort()+ request.getRequestURI().split("files")[1],
                    "get-resource");
                UploadFileResponseDTO responseBody = new UploadFileResponseDTO(
                    "File uploaded successfully",
                    selfLink.getHref(),
                    fileLocationLink.getHref()
            );

            return ResponseEntity.ok().body(responseBody);
        } catch (FileAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new UploadFileResponseDTO("File with the same name already exists",null,null));
        } catch (IOException e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UploadFileResponseDTO("Failed to upload file",null,null));
        }

    }

    @GetMapping("/**")
    public ResponseEntity<InputStreamResource> downloadFile(HttpServletRequest request ) {
        return fileService.downloadFile(request.getRequestURI().split("files")[1]);

    }





}