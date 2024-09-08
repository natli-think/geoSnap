package com.geoSnap.imageuploader;


import com.geoSnap.imageuploader.model.Image;
import com.geoSnap.imageuploader.service.imageService;
import com.geoSnap.imageuploader.repository.imageRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ImageServiceTests {

    @InjectMocks
    private imageService imageService;

    @Mock
    private imageRepository imageRepository;

    public ImageServiceTests() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveImage() throws IOException {
        // Arrange
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.jpg");
        when(file.getSize()).thenReturn(12345L);
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));

        // Mock the behavior of the repository save
        Image mockImage = new Image();
        mockImage.setName("test.jpg");
        mockImage.setSize(12345L);
        mockImage.setLatitude(12.34f);
        mockImage.setLongitude(56.78f);
        mockImage.setFilePath("https://mockdropbox.com/test.jpg");

        when(imageRepository.save(any(Image.class))).thenReturn(mockImage);

        // Act
        Image savedImage = imageService.saveImage(file, 12.34f, 56.78f);

        // Assert
        assertNotNull(savedImage);
        assertEquals("test.jpg", savedImage.getName());
        assertEquals(12345L, savedImage.getSize());
        assertEquals(12.34f, savedImage.getLatitude());
        assertEquals(56.78f, savedImage.getLongitude());
        assertEquals("https://mockdropbox.com/test.jpg", savedImage.getFilePath());
    }

}
