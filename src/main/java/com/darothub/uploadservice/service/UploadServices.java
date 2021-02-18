package com.darothub.uploadservice.service;

import com.darothub.uploadservice.entity.UploadImage;
import com.darothub.uploadservice.model.dto.UploadImageDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadServices {
    public UploadImageDTO uploadToDb(MultipartFile file) throws IOException;
    public UploadImage downloadImage(String id) throws IOException;
}
