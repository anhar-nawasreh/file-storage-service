package com.stream.file.storage.service.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

@Getter
@Setter
@NoArgsConstructor
public class UploadedFileDTO {

    @NotNull(message = "File should be provided")
    private MultipartFile file;
    @NotNull(message ="Name Path shouldn't be empty" )
    private String name;
    @NotNull(message = "File Path shouldn't be empty")
    private Path path;
}
