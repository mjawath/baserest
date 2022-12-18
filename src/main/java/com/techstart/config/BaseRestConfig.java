package com.techstart.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
//@Import({RestConfig.class, Test.class})
@ComponentScan("com.techstart")
public class BaseRestConfig {

}


