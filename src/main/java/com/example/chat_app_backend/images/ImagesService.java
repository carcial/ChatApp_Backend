package com.example.chat_app_backend.images;

import com.example.chat_app_backend.appUser.AppUser;
import com.example.chat_app_backend.appUser.AppUserRepository;
import com.example.chat_app_backend.messages.MessageRepository;
import com.example.chat_app_backend.messages.Messages;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
@AllArgsConstructor
public class ImagesService {

    private final ImagesRepository imagesRepository;
    private final AppUserRepository appUserRepository;
    private final MessageRepository messageRepository;







    @Transactional
    public String upLoadProfilePic(String userEmail, MultipartFile file) throws IOException {
        AppUser appUser = appUserRepository.findAppUserByEmail(userEmail);

        if(appUser == null){
            throw new IllegalStateException("There is no user with email: "+ userEmail);
        }
        Images images = new Images(compressImage(file.getBytes()), file.getContentType());
        imagesRepository.save(images);
        appUser.setProfilePic(images);

        return "ProfilePic uploaded successfully";
    }


    public Messages compressImagesInMessages(Messages messages){
        byte[] compressedFile = compressImage(messages.getImages().getImage());
        Images images = new Images(compressedFile, messages.getImages().getImageContentType());
        imagesRepository.save(images);
        messages.setImages(images);
        return messages;
    }


    public void deleteImageById(Long imageId){
        imagesRepository.deleteById(imageId);
    }



    public  byte[] compressImage(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*1024];
        while (!deflater.finished()) {
            int size = deflater.deflate(tmp);
            outputStream.write(tmp, 0, size);
        }
        try {
            outputStream.close();
        } catch (Exception ignored) {
        }
        return outputStream.toByteArray();
    }



    public byte[] decompressImage(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(tmp);
                outputStream.write(tmp, 0, count);
            }
            outputStream.close();
        } catch (Exception ignored) {
        }
        return outputStream.toByteArray();
    }
}
