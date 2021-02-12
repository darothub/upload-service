package com.darothub.uploadservice.repository;

import com.darothub.uploadservice.entity.UploadImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadRepository extends JpaRepository<UploadImage, String> {
}
