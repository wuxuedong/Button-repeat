package com.button.demo1;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.button.demo1.mapper")
public class BDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BDemoApplication.class, args);
	}
}
