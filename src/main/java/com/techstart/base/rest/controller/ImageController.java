package com.techstart.base.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by jawa on 12/7/2020.
 */
@RestController
@RequestMapping("/images/")
public class ImageController {

    @Autowired
    private StorageService storage;

    @PostMapping(path = {"/", ""})
    public ResponseEntity handleFileUpload(@RequestParam("file") MultipartFile file) {
        String nf = storage.store(file);
        return new ResponseEntity("{\"newFile\": \"" + nf + "\"}", HttpStatus.CREATED);
    }


    @RequestMapping(value = {"/{id}", "/{id}/"}, method = RequestMethod.GET)
    public ResponseEntity<byte[]> getImage(@PathVariable String id) {
        byte[] load = storage.load(id);
        String extension = StringUtils.getFilenameExtension(id);
        MediaType mt = determineMediaType(extension);
        return ResponseEntity
                .ok()
                .contentType(mt)
                .body(load);
    }
    private MediaType determineMediaType(String extension) {
        switch (extension) {
            case "jpg":
            case "jpeg":
                return MediaType.IMAGE_JPEG;
            case "png":
                return MediaType.IMAGE_PNG;
            case "gif":
                return MediaType.IMAGE_GIF;
            // Add more cases for other supported image formats if needed
            default:
                return MediaType.IMAGE_JPEG; // Unsupported media type
        }
    }


}
