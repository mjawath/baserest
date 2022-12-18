package com.techstart.base.rest.controller;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jawa on 11/18/2020.
 */
@Repository
public class GenericNSQLDAO {

    public GenericNSQLDAO() {

    }

    public Object create(Object obj){
        return obj;
    }

    public List getAll(Class domain,Integer size ,Integer page,String sort){

        return null;
    }

    public List getAll(Class domain,String sort) {
        return null;
    }

    public List getAll(Class domain){
        return null;
    }

    public Object update(Object ob) {
        return null;
    }

    public Object partialUpdate(Object ob) {
        return null;
    }


    public Object getObject(String id, Class domain) {;
        return null;
    }
}
