package com.techstart.commons.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.entitybase.DataException;
import com.techstart.base.rest.controller.DomainModel;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by jawa on 11/6/2020.
 */
public class ReflectionUtil {

    public static Class getParameterisedBusinessClass(Class aClass) {
        Type genericSuperclass = aClass.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
            if (actualTypeArguments != null && actualTypeArguments.length > 0) {
                return (Class) actualTypeArguments[0];
            }
        }

        return null;
    }

    public static <T> T getEntity(ObjectMapper om, String payloadJson, Class busClass) {
        return (T) getValue(om, payloadJson, busClass);
    }

    public static <T> T getValue(ObjectMapper om, String payloadJson, Class<T> busClass) {
        T ob = null;
        try {  //read JSON like DOM Parser
            JsonNode rootNode = om.readTree(payloadJson);
            JsonNode entityNode = rootNode.path("entity");
            if (entityNode != null && entityNode.isMissingNode()) {

                ob = om.readValue(rootNode.toString(), busClass);

            } else {
                ob = om.readValue(entityNode.toString(), busClass);

            }
            return ob;

        } catch (IOException ie) {
            throw new DataException("JSON Parsing Error  " + payloadJson, ie);
        }
    }

    public static <T> T getT(ObjectMapper om, String payloadJson, String path, Class busClass) {
        T ob = null;
        try {  //read JSON like DOM Parser
            JsonNode rootNode = om.readTree(payloadJson);
            JsonNode entityNode = rootNode.path(path);
            return (T) om.readValue(entityNode.toString(), busClass);
        } catch (IOException ie) {
            throw new DataException("JSON Parsing Error  " + payloadJson, ie);
        }
    }

    public static Class getClassFromPath(String classPath) {
        try {
            return Class.forName(classPath);
        } catch (ClassNotFoundException e) {
//            logger.error("cannot find domain class", e);
        e.printStackTrace();
        }
        return null;
    }
}
