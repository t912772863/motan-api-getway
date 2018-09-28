package com.tian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource(locations={"classpath:motan-client.xml"})
public class MotanApiGetwayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MotanApiGetwayApplication.class, args);
	}
}
