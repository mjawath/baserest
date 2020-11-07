package com.techstart.base.rest.controller;

import com.mycompany.entitybase.spring.BaseRepositoryImpl;
import com.techstart.spring.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jawa on 9/18/2020.
 */
@RestController
@RequestMapping(path = "/generic/api/")
public class GenericCrudRestWSController {


//    @Autowired
    private BaseRepositoryImpl baseRepo;

    @Post(path = "/")
    public ResponseEntity create(@RequestBody Map requestBody) {
        System.out.println("create post data recieved " + requestBody);
        //from the postquery ..create the  object

        return new ResponseEntity("", HttpStatus.CREATED);
    }


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List>  getAll() {
        return ResponseEntity.ok(new ArrayList());
    }

    @RequestMapping("/page/{pageNo}")
    public ResponseEntity<List> getAll(@PathVariable("pageNo") int pageNo, HttpServletRequest request) {
        return ResponseEntity.ok(new ArrayList());
    }

    @RequestMapping("/{id}")
    public ResponseEntity  get(@PathVariable("id") String id) {
        return ResponseEntity.ok("");
    }

}
