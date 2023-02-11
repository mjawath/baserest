package com.techstart.base.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by jawa on 11/18/2020.
 */
@Service
public class GenericService {

    private GenericRDBMSDAO repo;

    @Autowired
    public GenericService(GenericRDBMSDAO repo) {
        this.repo = repo;
    }


    @Transactional
    public Object create(Object obj) {
        return repo.create(obj);
    }


    public List getAll(Class domain, Integer size, Integer page, String sort) {
        return repo.getAll(domain, size, page, sort);
    }


    public List getAll(Class domain, String sort) {
        return repo.getAll(domain, sort);
    }

    public List getAll(Class domain) {
        return repo.getAll(domain);
    }

    @Transactional
    public Object update(Object ob) {
        return repo.update(ob);
    }

    @Transactional
    public Object partialUpdate(Object ob) {
        return this.repo.partialUpdate(ob);
    }


    public Object getObject(String id, Class domain) {
        Object tem = repo.getObject(id, domain);
        return tem;
    }
}
