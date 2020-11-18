package com.techstart.base.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jawa on 11/18/2020.
 */
@Repository
public class GenericRDBMSDAO  {


    private EntityManager em;

    public GenericRDBMSDAO(EntityManager em) {

        this.em = em;
    }

    @Transactional
    public String create(Object obj){
        this.em.persist(obj);
        return null;
    }



    public List getAll(Class domain,Integer size ,Integer page,String sort){
        String name= domain.getSimpleName();
        String select = " SELECT e FROM " + name + " e";
        select+= sort!=null?" order by "+sort:" ";
        Query q= em.createQuery(select);
        q.setFirstResult(page==null?0:page);
        q.setMaxResults(size==null?10:size);
        List resultList = q.getResultList();
        return resultList;
    }


    public List getAll(Class domain,String sort) {
        String name= domain.getSimpleName();
        Query q= em.createQuery(" SELECT e FROM "+name+" e");
        List resultList = q.getResultList();
        return resultList;
    }

    public List getAll(Class domain){
        String name= domain.getSimpleName();
        Query q= em.createQuery(" SELECT e FROM "+name+" e");
        List resultList = q.getResultList();
        return resultList;
    }


    public void update(Object ob) {

    }

    public void partialUpdate(Object ob) {

    }

    public Object getObject(String id, Class domain) {
        Object tem = em.find(domain,id);
        return tem;
    }
}
