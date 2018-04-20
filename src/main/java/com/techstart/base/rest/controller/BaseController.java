package com.techstart.base.rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


public class BaseController {

    protected static boolean SHOW_CRUD_LIST = true;

    protected  Logger logger;

    @Autowired
    MessageSource messageSource;
    
    public BaseController() {

    }
    
    /**
     * This method will list all existing users.
     */
    @RequestMapping(value = {"/testsss", "/list"}, method = RequestMethod.GET)
    public String listUsers(ModelMap model) {

    
        return "userslist";
    }




    /**
     * This method will be called on form submission, handling POST request for
     * saving user in database. It also validates the user input
     */
    @RequestMapping(value = {"/newuser"}, method = RequestMethod.POST)
    public String saveUser(BindingResult result,
            ModelMap model) {

        if (result.hasErrors()) {
            return "registration";
        }


        model.addAttribute("success", "User "  + " registered successfully");
        //return "success";
        return "registrationsuccess";
    }





    /**
     * This method will delete an user by it's SSOID value.
     */
    @RequestMapping(value = {"/delete/{Id}"}, method = RequestMethod.GET)
    public String delete(@PathVariable String Id) {
 
        return "redirect:/list";
    }

  

}
