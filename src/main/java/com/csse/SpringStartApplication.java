package com.csse;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.csse.mapper")
public class SpringStartApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringStartApplication.class,args);
    }
}


