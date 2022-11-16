package com.techstart.base.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techstart.commons.util.ReflectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;

//@Component
public class Gencon {

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;
    @Autowired
    private DynamicController dynamicController;

    @PostConstruct
    public void configitem() throws NoSuchMethodException {
//         = new DynamicController();
        handlerMapping.registerMapping(
                RequestMappingInfo.paths("/users/")
                        .methods(RequestMethod.GET)
                        .produces(MediaType.TEXT_PLAIN_VALUE)
                        .build(),
                dynamicController,
                dynamicController.getClass()
                        .getMethod("getAll"));

        handlerMapping.registerMapping(
                RequestMappingInfo.paths("/users/")
                        .methods(RequestMethod.POST)
                        .consumes(MediaType.APPLICATION_JSON_VALUE)
                        .produces(MediaType.APPLICATION_JSON_VALUE)
                        .build(),
                dynamicController,
                dynamicController.getClass()
                        .getMethod("postItem",String.class));

    }

}
@Component
class DynamicController {

    private ObjectMapper objectMapper= new ObjectMapper();


    public ResponseEntity<String> getAll(){
        return ResponseEntity.ok("heloo");
    }

    public ResponseEntity<Object> postItem(@RequestBody String obj){

        //get class from package
        //DTO
        Class cls=ReflectionUtil.getClassFromPath("com.techstart.catalog.product.SKU");
        Object ss =ReflectionUtil.getEntity(objectMapper,obj,cls);

//        SKU ss= new SKU();
//        ss.setCode("ddtest");
        return ResponseEntity.ok(ss);
    }
}
