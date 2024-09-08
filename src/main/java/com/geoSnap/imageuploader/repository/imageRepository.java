package com.geoSnap.imageuploader.repository;
import com.geoSnap.imageuploader.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface imageRepository extends JpaRepository<Image, Long> {
}