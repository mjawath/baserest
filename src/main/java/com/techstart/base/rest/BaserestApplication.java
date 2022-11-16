package com.techstart.base.rest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BaserestApplication implements CommandLineRunner{

    public static void main(String[] args) {
        SpringApplication.run(BaserestApplication.class, args);
    }


    @Override
    public void run(String... strings) throws Exception {
//        dao.getAll(Dummy.class);
    }
}
