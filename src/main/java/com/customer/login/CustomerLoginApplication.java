package com.customer.login;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.RequestScope;

import java.time.Duration;

@SpringBootApplication
@EnableJpaRepositories
@ComponentScan(basePackages="com.customer")
//@EnableEurekaClient
public class CustomerLoginApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerLoginApplication.class, args);
	}

	@Bean
	@RequestScope
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder
				.setConnectTimeout(Duration.ofMillis(60_000))
				.setReadTimeout(Duration.ofMillis(60_000))
				.build();
	}
}
