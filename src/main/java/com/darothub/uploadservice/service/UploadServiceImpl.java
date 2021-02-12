package com.darothub.uploadservice.service;

import com.darothub.uploadservice.entity.UploadImage;
import com.darothub.uploadservice.repository.UploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UploadServiceImpl implements UploadServices {
    @Autowired
    UploadRepository uploadRepository;

    @Override
    public UploadImage uploadToDb(MultipartFile file) throws IOException {
        UploadImage uploadImage = new UploadImage();
        uploadImage.setFileData(file.getBytes());
        uploadImage.setFileType(file.getContentType());
        uploadImage.setFileName(file.getOriginalFilename());
        return uploadRepository.save(uploadImage);
    }

    @Override
    public UploadImage downloadImage(String id) throws IOException {
        return uploadRepository.getOne(id);
    }
}
