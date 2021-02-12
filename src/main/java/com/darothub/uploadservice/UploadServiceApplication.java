package com.darothub.uploadservice;

import com.darothub.uploadservice.model.response.ErrorResponse;
import com.darothub.uploadservice.model.response.SuccessResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UploadServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UploadServiceApplication.class, args);
    }

    @Bean
    public static SuccessResponse returnSuccessResponse(){
        return new SuccessResponse();
    }
    @Bean
    public static ErrorResponse returnErrorResponse(){
        return new ErrorResponse();
    }

}
