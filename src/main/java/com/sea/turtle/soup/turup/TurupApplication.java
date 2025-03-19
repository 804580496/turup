package com.sea.turtle.soup.turup;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.sea.turtle.soup.turup.dao.mapper")
@SpringBootApplication
public class TurupApplication {

    public static void main(String[] args) {
        SpringApplication.run(TurupApplication.class, args);
    }

}
