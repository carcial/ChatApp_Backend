package com.example.chat_app_backend.images;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagesRepository extends JpaRepository<Images, Long> {

    @Query("select im from Images  im where im.imageID = ?1")
    public Images getOneImageById(Long imageId);
}
