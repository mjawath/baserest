package com.techstart.base.rest.controller;

/**
 * Created by jawa on 11/18/2020.
 */
public class DomainModel {

    private String className;
    private String persistenceType; //RDBMS, NoSQL, etc.
    private String classPath;


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPersistenceType() {
        return persistenceType;
    }

    public void setPersistenceType(String persistenceType) {
        this.persistenceType = persistenceType;
    }

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }
}
