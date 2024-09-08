package com.geoSnap.imageuploader.service;

import com.geoSnap.imageuploader.model.Image;
import com.geoSnap.imageuploader.repository.imageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class imageService {

    @Autowired
    private imageRepository imageRepository;

    public Image saveImage(MultipartFile file, Float latitude, Float longitude) throws IOException {
        // Get the project root directory
        String projectDir = System.getProperty("user.dir");

        // Define the path to save the file
        File directory = new File(projectDir, "imageuploader/src/main/resources/static/images");

        // Ensure directory exists
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Define the file path
        String fileName = file.getOriginalFilename();
        File uploadFile = new File(directory, fileName);
        file.transferTo(uploadFile);

        // Create and save image metadata
        Image image = new Image();
        image.setName(file.getOriginalFilename());
        image.setSize(file.getSize());
        image.setLatitude(latitude);
        image.setLongitude(longitude);
        image.setFilePath(uploadFile.getAbsolutePath());

        return imageRepository.save(image);
    }

    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElse(null);
    }
}