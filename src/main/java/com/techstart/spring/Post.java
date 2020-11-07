package com.techstart.spring;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by jawa on 10/25/2020.
 */
//
//@PostMapping
//public @interface Post {
//}


@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = RequestMethod.POST)
public @interface Post {

    @AliasFor(attribute = "path")
    String[] value() default {};

    @AliasFor(attribute = "value")
    String[] path() default {};
}
