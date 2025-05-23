package com.itmo.indexing_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
//@EntityScan(basePackages = "com.itmo.model.postgres")
//@EnableJpaRepositories(basePackages = "com.itmo.indexing_service.repository.postgres")
@SpringBootApplication
public class IndexingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IndexingServiceApplication.class, args);
	}

}
