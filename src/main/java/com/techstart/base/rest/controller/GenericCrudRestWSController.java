package com.techstart.base.rest.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jawa on 9/18/2020.
 */
@RestController
@RequestMapping(path = "/generic/api/v1")
public class GenericCrudRestWSController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private GenericRDBMSDAO repo;

    @Autowired
    private ObjectMapper om;
    private JsonNode node;
    private DomainModel[] domains;


    public GenericCrudRestWSController(@Autowired ObjectMapper om) {

        try {
            String content =new String(Files.readAllBytes(Paths.get("DomainModel.json")));
            node = om.readTree(content);
            JsonNode doms = node.get("domainModels");
            domains = om.convertValue(doms,DomainModel[].class);

        } catch (Exception e) {
            logger.error("GenericCrudRestWSController",e);
            e.printStackTrace();
        }

    }

    @PostMapping(path = {"/{domain}/","/{domain}"})
    public ResponseEntity create(@PathVariable() String domain,@RequestBody String requestBody) {
        System.out.println("create post data received " + requestBody);
        Object ob= getObject(domain, requestBody);
        Object ret = repo.create(ob);
        return new ResponseEntity(ret, HttpStatus.CREATED);
    }


    @PutMapping(path = {"/{domain}/{id}","/{domain}/{id}/"})
    public ResponseEntity update(@PathVariable String id,@PathVariable String domain,@RequestBody String requestBody) {
        System.out.println("updated  data received " + requestBody);
        Object ob  = getObjectFromString(domain, requestBody);
        Object updated = repo.update(ob);
        return new ResponseEntity(updated, HttpStatus.OK);
    }


    @PatchMapping(path = {"/{domain}/{id}","/{domain}/{id}/"})
    public ResponseEntity patchObject(@PathVariable String id,@PathVariable String domain,@RequestBody String requestBody) {
        System.out.println("update  data received " + requestBody);
        Object existing = repo.getObject(id, getDomainClass(domain));
        ObjectReader objectReader = om.readerForUpdating(existing);
        try {
            Object toUpdate = objectReader.readValue(requestBody);
            Object updated = repo.partialUpdate(toUpdate);
            return new ResponseEntity(updated, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity(" patch error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Object getObjectFromString(String domain, @RequestBody String requestBody) {
        try {
            return om.readValue(requestBody, getDomainClass(domain));
        } catch (Exception e) {
            logger.error("request parse error",e);
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping(path = {"/{domain}/{id}","/{domain}/{id}/"})
    public ResponseEntity  getObject(@PathVariable("id") String id,@PathVariable String domain) {
        Object ob = repo.getObject(id,getDomainClass(domain));
        return ResponseEntity.ok(ob);
    }


    @GetMapping(path = {"/{domain}/","/{domain}"})
    public ResponseEntity<List>  getAll(@PathVariable() String domain,@RequestParam(required = false)Integer  size,
                                        @RequestParam(required = false) Integer page,@RequestParam(required = false) String sort) {
        logger.info("getAll");
        List all = repo.getAll(Dummy.class,page,size,sort);
        logger.info("getAll " + all.size());
        return ResponseEntity.ok(all);
    }

    @RequestMapping("/page/{pageNo}")
    public ResponseEntity<List> getAll(@PathVariable("pageNo") int pageNo, HttpServletRequest request) {

        return ResponseEntity.ok(new ArrayList());
    }



    private Class getDomainClass(String domain){
        for(DomainModel dom:domains){
            if(dom.getClassName().contains("."+domain)){
                try {
                    return Class.forName(dom.getClassName());
                } catch (ClassNotFoundException e) {
                    logger.error("cannot find domain class",e);
                }
            }
        }
        return null;
    }

}
