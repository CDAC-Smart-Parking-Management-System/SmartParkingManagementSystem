package com.smartparking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate is the simplest way for a Spring Boot app to call another
 * REST API (in our case, the .NET logging microservice).
 *
 * Short timeouts are set so that if the logging microservice is not
 * running, our main request (e.g. booking a slot) does not hang for a
 * long time waiting for a reply - it fails fast and LoggingClient
 * silently ignores the error.
 *
 * Note: we build the RestTemplate directly with SimpleClientHttpRequestFactory
 * instead of using RestTemplateBuilder, because RestTemplateBuilder's
 * package location changed between Spring Boot versions - this plain
 * Spring Framework class (org.springframework.http.client) works the
 * same way regardless of which Spring Boot version the project uses.
 */
@Configuration
public class RestTemplateConfig {

	@Bean
	public RestTemplate restTemplate() {

		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setConnectTimeout(2000); // milliseconds
		factory.setReadTimeout(2000);    // milliseconds

		return new RestTemplate(factory);
	}
}
