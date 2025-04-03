package com.cornsoup.newitching;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class NewItchingApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewItchingApplication.class, args);
	}

}
