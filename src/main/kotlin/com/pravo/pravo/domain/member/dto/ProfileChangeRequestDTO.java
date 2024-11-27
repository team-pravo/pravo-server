package com.pravo.pravo.domain.member.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public record ProfileChangeRequestDTO(

    @NotBlank(message = "Name cannot be blank")
    String name,

    @Nullable MultipartFile file,

    @NotBlank Boolean resetToDefaultImage

) {

}
