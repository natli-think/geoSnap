package com.geoSnap.imageuploader.service;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;
import com.geoSnap.imageuploader.model.Image;
import com.geoSnap.imageuploader.repository.imageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class imageService {
    private static final Logger logger = LoggerFactory.getLogger(imageService.class);
    @Autowired
    private imageRepository imageRepository;
    @Value("${dropbox.access.token}")
    private String ACCESS_TOKEN = "enter_token";

    /* This function is used to save images to Dropbox as well as to the Database
     * @param file : The image that is being uploaded by the user.
     * @param latitude : The latitude input by the user.
     * @param longitude : The longtitude input by the user.
     * @return The method returns the image which has all the input details by the user along with filepath.
     * @throws IOException If there is any error in uploading to Dropbox, then exception will be thrown
    */
    public Image saveImage(MultipartFile file, Float latitude, Float longitude) throws IOException {


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
            logger.error("Error uploading file to Dropbox", e);
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

    /* This function is used to get all the images stored in the Database
     * @return The method returns the List of Images
     */
    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    /* This function is used to fetch a single image by ID
     * @param id: The id of the image to be fetched.
     * @return The method returns the Image with the mentioned ID
     */
    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElse(null);
    }
}