package com.darothub.uploadservice.controller;

import com.darothub.uploadservice.ConstantUtils;
import com.darothub.uploadservice.entity.UploadImage;
import com.darothub.uploadservice.model.dto.UploadImageDTO;
import com.darothub.uploadservice.model.response.ResponseModel;
import com.darothub.uploadservice.model.response.SuccessResponse;
import com.darothub.uploadservice.service.UploadServiceImpl;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class UploadImageController {

    @Autowired
    private UploadServiceImpl uploadService;
    @Autowired
    private SuccessResponse successResponse;


    @PostMapping("/upload")
    public ResponseEntity<ResponseModel> uploadToDb(@RequestParam("file") MultipartFile file) throws IOException {
        if(file != null && file.getContentType().matches(ConstantUtils.IMAGE_PATTERN)){
            UploadImageDTO imageUploadDTO =  uploadService.uploadToDb(file);
            return handleSuccessResponseEntity("Image uploaded successfully", HttpStatus.OK, imageUploadDTO);
        }
        return handleSuccessResponseEntity("Invalid image format", HttpStatus.BAD_REQUEST, null);

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
        return new ResponseEntity<>(successResponse, status);
    }
}
