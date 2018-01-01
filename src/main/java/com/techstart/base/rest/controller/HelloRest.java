/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.techstart.base.rest.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author LENOVO PC
 */
@RestController
public class HelloRest {
    
//   @Autowired
//   private TestService service;
    
    
    @RequestMapping("/")
    private String test(){
        
//       service.call();
        
        return "  heloooooooooooooo ### " ;
    }
}
