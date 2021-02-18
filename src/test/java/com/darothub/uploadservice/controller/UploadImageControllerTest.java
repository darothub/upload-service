package com.darothub.uploadservice.controller;

import com.darothub.uploadservice.entity.UploadImage;
import com.darothub.uploadservice.exception.CustomRestExceptionHandler;
import com.darothub.uploadservice.service.UploadServiceImpl;
import com.darothub.uploadservice.service.UploadServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.InputStream;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UploadImageController.class)
class UploadImageControllerTest {

    @MockBean
    private UploadServiceImpl uploadServices;
    @InjectMocks
    private UploadImageController uploadImageController;

    @Autowired
    private MockMvc mockMvc;




    @BeforeEach
    void setUp() {
        // We would need this line if we would not use the MockitoExtension
        // MockitoAnnotations.initMocks(this);
        // Here we can't use @AutoConfigureJsonTesters because there isn't a Spring context

        // MockMvc standalone approach
//        mockMvc = MockMvcBuilders.standaloneSetup(uploadImageController)
//                .setControllerAdvice(new CustomRestExceptionHandler())
//                .build();
    }

    @Test
    void uploadToDb() throws Exception {
        Resource fileResource = new ClassPathResource(
                "test.png");

        assertNotNull(fileResource);
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                fileResource.getFilename(),
                MediaType.IMAGE_PNG_VALUE,
                fileResource.getInputStream()
        );


        mockMvc.perform(multipart("/api/v1/upload").file(file))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Image uploaded successfully")));

        then(uploadServices).should().uploadToDb(file);
    }
    @Test
    void uploadToDbShouldReturnInvalidFormatWhenNotImage() throws Exception {
        Resource fileResource = new ClassPathResource(
                "test.png");

        assertNotNull(fileResource);
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                fileResource.getFilename(),
                MediaType.APPLICATION_PDF_VALUE,
                fileResource.getInputStream()
        );


        mockMvc.perform(multipart("/api/v1/upload").file(file))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Invalid image format")));

    }

    @Test
    void downloadFile() {
    }
}