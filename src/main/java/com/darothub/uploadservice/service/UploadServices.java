package com.darothub.uploadservice.service;

import com.darothub.uploadservice.entity.UploadImage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadServices {
    public UploadImage uploadToDb(MultipartFile file) throws IOException;
    public UploadImage downloadImage(String id) throws IOException;
}
