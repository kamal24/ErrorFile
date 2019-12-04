package com.coderz.errorfiles;

import com.coderz.errorfiles.Model.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class ErrorfilesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ErrorfilesApplication.class, args);
	}

}

