package com.stream.file.storage.service.model;

import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class UploadFileResponseDTO{
    private String message;
    private String selfLink;
    private String fileLocationLink;


}
