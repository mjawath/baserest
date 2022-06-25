package com.techstart.base.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
        MediaType mt = id.lastIndexOf(".jpg") != -1 ? MediaType.IMAGE_JPEG : MediaType.IMAGE_JPEG;
        return ResponseEntity
                .ok()
                .contentType(mt)
                .body(load);
    }


}
