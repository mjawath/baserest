package com.techstart.base.rest.controller;

import com.techstart.spring.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by jawa on 12/7/2020.
 */
@RestController
@RequestMapping("/images/")
public class ImageController {

    @Autowired
    private StorageService storage;

    @PostMapping(path = {"/",""})
    public ResponseEntity handleFileUpload(@RequestParam("file") MultipartFile file) {
        String nf = storage.store(file);
        return new ResponseEntity("{\"newFile\": \""+nf+"\"}", HttpStatus.CREATED);
    }




    @RequestMapping(value = {"/{id}","/{id}/"}, method = RequestMethod.GET)
    public ResponseEntity<byte[]> getImage(@PathVariable String id)  {


        byte[] load = storage.load(id);
        MediaType mt =  id.lastIndexOf( ".jpg")!=-1? MediaType.IMAGE_JPEG:MediaType.IMAGE_JPEG;
        return ResponseEntity
                .ok()
                .contentType(mt)
                .body(load);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(ResourceNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }


}
