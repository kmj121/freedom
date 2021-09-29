package com.roger.freedom;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.roger.freedom.mapper"})
public class FreedomApplication {

    public static void main(String[] args) {
        SpringApplication.run(FreedomApplication.class, args);
    }

}
