package com.w2m.superheroe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableCaching
@EnableWebMvc
@EnableSwagger2
public class SuperheroeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SuperheroeApplication.class, args);
	}

}
