package com.techstart.base.rest.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
        Object ob = null;
        ob = getObject(domain, requestBody, ob);
        repo.create(ob);
        return new ResponseEntity("", HttpStatus.CREATED);
    }


    @PutMapping(path = {"/{domain}/{id}","/{domain}/{id}/"})
    public ResponseEntity update(@PathVariable String domain,@RequestBody String requestBody) {
        System.out.println("update  data received " + requestBody);
        Object ob = null;
        ob = getObject(domain, requestBody, ob);
        repo.update(ob);
        return new ResponseEntity("", HttpStatus.CREATED);
    }


    @PatchMapping(path = {"/{domain}/{id}","/{domain}/{id}/"})
    public ResponseEntity patch(@PathVariable String domain,@RequestBody String requestBody) {
        System.out.println("update  data received " + requestBody);
        Object ob = null;
        ob = getObject(domain, requestBody, ob);
        repo.partialUpdate(ob);
        return new ResponseEntity("", HttpStatus.CREATED);
    }

    private Object getObject(@PathVariable String domain, @RequestBody String requestBody, Object ob) {
        try {
            ob = om.readValue(requestBody, getDomainClass(domain));
        } catch (Exception e) {
            logger.error("request parse error",e);
            e.printStackTrace();
        }
        return ob;
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

    @RequestMapping("/{id}")
    public ResponseEntity  get(@PathVariable("id") String id,@PathVariable() String domain) {
        Object ob = repo.getObject(id,getDomainClass(domain));
        return ResponseEntity.ok("");
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
