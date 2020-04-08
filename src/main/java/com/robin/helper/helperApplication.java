package com.robin.helper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource(locations={"classpath:spring-common.xml"})
public class helperApplication {

	public static void main(String[] args) {
		SpringApplication.run(helperApplication.class, args);
	}

}
