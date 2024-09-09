package com.geoSnap.imageuploader.controller;

import com.geoSnap.imageuploader.model.Image;
import com.geoSnap.imageuploader.service.imageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Controller
public class ImageController {

    @Autowired
    private imageService imageService;

   /* API endpoint used to upload an image with metadata
    * Usage: http://localhost:8080/upload
    */

    @GetMapping("/upload")
    public String showUploadForm() {
        return "upload";
    }

    /* API endpoint used within the application to upload.
     * This will be redirected to the uploaded images with metadata.
    */
    @PostMapping("/api/image/upload")
    public String handleImageUpload(@RequestParam("file") MultipartFile file,
                                    @RequestParam("latitude") float latitude,
                                    @RequestParam("longitude") float longitude) throws IOException {
        imageService.saveImage(file, latitude, longitude);
        return "redirect:/image";
    }

    /* API endpoint used to list  images with metadata
     * Usage: http://localhost:8080/image
    */
    @GetMapping("/image")
    public String listImages(Model model) {
        model.addAttribute("imageList", imageService.getAllImages());
        return "list";
    }

    /* API endpoint used to fetch a single image by ID
     * Usage: http://localhost:8080/image/12
    */
    @GetMapping("/image/{id}")
    public String viewImageById(@PathVariable("id") Long id, Model model) {
        Image image = imageService.getImageById(id);
        if (image == null) {
            return "redirect:/image";
        }
        model.addAttribute("image", image);
        return "view";
    }

    /* API endpoint used as the landing page.
     * Usage: http://localhost:8080/geoSnap
    */
    @GetMapping("/geoSnap")
    public String returnInfo(){
        return "about";
    }

}