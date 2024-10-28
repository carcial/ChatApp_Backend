package com.example.chat_app_backend.images;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageDTO {

    private Long imageId;

    private String base64Image;

    private String contentType;
}
