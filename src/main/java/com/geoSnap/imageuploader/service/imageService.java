package com.geoSnap.imageuploader.service;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;
import com.geoSnap.imageuploader.model.Image;
import com.geoSnap.imageuploader.repository.imageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class imageService {

    @Autowired
    private imageRepository imageRepository;

    public Image saveImage(MultipartFile file, Float latitude, Float longitude) throws IOException {
        final String ACCESS_TOKEN = "sl.B8h3CMbSjj7ribhb2UdvCiV8hivA7cSuOz95kEWTnZBX8Nrf5TFdpq_q3JC725OgyJL2J9m9loGVMG4GRSyZEGD7PG9Nd_HUJQezeoyKPXlClvWuApXGiY7cIVDxmMEuwhER-o8abkfC-fUibADQlNM";

//        // Get the project root directory
//        String projectDir = System.getProperty("user.dir");
//
//        // Define the path to save the file
//        File directory = new File(projectDir, "imageuploader/src/main/resources/static/images");
//
//        // Ensure directory exists
//        if (!directory.exists()) {
//            directory.mkdirs();
//        }
//
//        // Define the file path
//        String fileName = file.getOriginalFilename();
//        File uploadFile = new File(directory, fileName);
//        file.transferTo(uploadFile);

        // Initialize Dropbox client
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);

        // Define the Dropbox path
        String dropboxPath = "/geoSnap/" + file.getOriginalFilename();

        String uploadFile="";
        try (InputStream in = file.getInputStream()) {
            // Upload to Dropbox
            FileMetadata metadata = client.files().uploadBuilder(dropboxPath)
                    .withMode(WriteMode.OVERWRITE)
                    .uploadAndFinish(in);

            // Generate a shared link for the file
            uploadFile = client.sharing().createSharedLinkWithSettings(metadata.getPathLower()).getUrl();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Error uploading file to Dropbox", e);
        }

        // Create and save image metadata
        Image image = new Image();
        image.setName(file.getOriginalFilename());
        image.setSize(file.getSize());
        image.setLatitude(latitude);
        image.setLongitude(longitude);
        image.setFilePath(uploadFile);

        return imageRepository.save(image);
    }

    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElse(null);
    }
}