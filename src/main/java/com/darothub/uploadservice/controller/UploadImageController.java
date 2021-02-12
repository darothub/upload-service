package com.darothub.uploadservice.controller;

import com.darothub.uploadservice.ConstantUtils;
import com.darothub.uploadservice.entity.UploadImage;
import com.darothub.uploadservice.model.dto.UploadImageDTO;
import com.darothub.uploadservice.model.response.ResponseModel;
import com.darothub.uploadservice.model.response.SuccessResponse;
import com.darothub.uploadservice.service.UploadServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
public class UploadImageController {

    @Autowired
    private UploadServiceImpl uploadService;
    @Autowired
    private SuccessResponse successResponse;


    @PostMapping("/upload")
    public ResponseEntity<ResponseModel> uploadToDb(@RequestParam("file") MultipartFile file) throws IOException {
        UploadImage uploadImage = uploadService.uploadToDb(file);
        UploadImageDTO imageUploadDTO = new UploadImageDTO();
        if(uploadImage != null && uploadImage.getFileType().matches(ConstantUtils.IMAGE_PATTERN)){
            String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/v1/download/")
                    .path(uploadImage.getFileId())
                    .toUriString();
            imageUploadDTO.setDownloadUri(uri);
            imageUploadDTO.setFileId(uploadImage.getFileId());
            imageUploadDTO.setFileName(uploadImage.getFileName());
            imageUploadDTO.setFileType(uploadImage.getFileType());
            imageUploadDTO.setUploadStatus(true);
            return handleSuccessResponseEntity("Image uploaded successfully", HttpStatus.OK, imageUploadDTO);
        }
        return handleSuccessResponseEntity("Invalid image format", HttpStatus.EXPECTATION_FAILED, null);

    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String id) throws IOException {
        UploadImage uploadImage = uploadService.downloadImage(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(uploadImage.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename"+uploadImage.getFileName())
                .body(new ByteArrayResource(uploadImage.getFileData()));

    }

    public ResponseEntity<ResponseModel> handleSuccessResponseEntity(String message, HttpStatus status, Object payload) {
        successResponse.setMessage(message);
        successResponse.setStatus(status.value());
        successResponse.setPayload(payload);
        return ResponseEntity.ok(successResponse);
    }
}
