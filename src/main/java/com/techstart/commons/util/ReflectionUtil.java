package com.techstart.commons.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by jawa on 11/6/2020.
 */
public class ReflectionUtil {

    public static Class getParameterisedBusinessClass(Class aClass) {
        Type genericSuperclass = aClass.getGenericSuperclass();
        if(genericSuperclass instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType)genericSuperclass).getActualTypeArguments();
            if(actualTypeArguments != null && actualTypeArguments.length > 0) {
                return  (Class)actualTypeArguments[0];
            }
        }

        return null;
    }
}
