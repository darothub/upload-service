package com.darothub.uploadservice.service;

import com.darothub.uploadservice.entity.UploadImage;
import com.darothub.uploadservice.model.dto.UploadImageDTO;
import com.darothub.uploadservice.repository.UploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

@Service
public class UploadServiceImpl implements UploadServices {
    @Autowired
    UploadRepository uploadRepository;

    @Override
    public UploadImageDTO uploadToDb(MultipartFile file) throws IOException {
        UploadImage uploadImage = new UploadImage();
        uploadImage.setFileData(file.getBytes());
        uploadImage.setFileType(file.getContentType());
        uploadImage.setFileName(file.getOriginalFilename());
        UploadImage savedImage = uploadRepository.save(uploadImage);
        UploadImageDTO imageUploadDTO = new UploadImageDTO();
        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/download/")
                .path(savedImage.getFileId())
                .toUriString();
        imageUploadDTO.setDownloadUri(uri);
        imageUploadDTO.setFileId(savedImage.getFileId());
        imageUploadDTO.setFileName(savedImage.getFileName());
        imageUploadDTO.setFileType(savedImage.getFileType());
        imageUploadDTO.setUploadStatus(true);

        return imageUploadDTO;
    }

    @Override
    public UploadImage downloadImage(String id) throws IOException {
        return uploadRepository.getOne(id);
    }
}
