package com.example.chat_app_backend.images;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Images {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageID;

    @Lob
    @Column(nullable = false, length = 490000000)
    private byte[] image;

    @Column(nullable = false)
    private String imageContentType;

    public Images(byte[] image, String imageContentType) {
        this.image = image;
        this.imageContentType = imageContentType;
    }
}
