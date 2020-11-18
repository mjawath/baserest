package com.techstart.base.rest.controller;

import com.mycompany.entitybase.BaseEntityString;

import javax.persistence.Entity;

/**
 * Created by jawa on 11/18/2020.
 */
@Entity
public class Dummy extends BaseEntityString {
    private String name;
    private String desciption;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }
}
