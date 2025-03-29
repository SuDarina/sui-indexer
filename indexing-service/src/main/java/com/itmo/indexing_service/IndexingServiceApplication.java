package com.itmo.indexing_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class IndexingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IndexingServiceApplication.class, args);
	}

}
