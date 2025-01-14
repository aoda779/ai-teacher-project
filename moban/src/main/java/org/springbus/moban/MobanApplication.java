package org.springbus.moban;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("org.springbus.moban.mapper")
@SpringBootApplication
public class MobanApplication {

    public static void main(String[] args) {
        SpringApplication.run(MobanApplication.class, args);
    }
}
