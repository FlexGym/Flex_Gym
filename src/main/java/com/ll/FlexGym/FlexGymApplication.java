package com.ll.FlexGym;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FlexGymApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlexGymApplication.class, args);
	}

}