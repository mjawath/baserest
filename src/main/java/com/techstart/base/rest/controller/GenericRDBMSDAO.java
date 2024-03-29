package com.techstart.base.rest.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by jawa on 11/18/2020.
 */
@Component
public class GenericRDBMSDAO {

    private EntityManager em;

    public GenericRDBMSDAO(EntityManager em) {

        this.em = em;
    }

    @Transactional
    public Object create(Object obj) {
        this.em.persist(obj);
        return obj;
    }


    public List getAll(Class domain, Integer size, Integer page, String sort) {
        String name = domain.getSimpleName();
        String select = " SELECT e FROM " + name + " e";
        select += sort != null ? " order by " + sort : " ";
        Query q = em.createQuery(select);
        q.setFirstResult(page == null ? 0 : page);
        q.setMaxResults(size == null ? 10 : size);
        List resultList = q.getResultList();
        return resultList;
    }


    public List getAll(Class domain, String sort) {
        String name = domain.getSimpleName();
        Query q = em.createQuery(" SELECT e FROM " + name + " e");
        List resultList = q.getResultList();
        return resultList;
    }

    public List getAll(Class domain) {
        String name = domain.getSimpleName();
        Query q = em.createQuery(" SELECT e FROM " + name + " e");
        List resultList = q.getResultList();
        return resultList;
    }

    @Transactional
    public Object update(Object ob) {
        return this.em.merge(ob);
    }

    @Transactional
    public Object partialUpdate(Object ob) {
        return this.em.merge(ob);
    }


    public Object getObject(String id, Class domain) {
        Object tem = em.find(domain, id);
        return tem;
    }
}
