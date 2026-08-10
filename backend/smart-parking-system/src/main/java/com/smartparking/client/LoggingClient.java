package com.smartparking.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Talks to the separate .NET Logging microservice over plain REST (HTTP).
 *
 * IMPORTANT (beginner note): logging must NEVER be allowed to break the main
 * booking flow. If the logging microservice is down, slow, or not started
 * yet, we simply print a warning to the console and move on - we do not
 * throw an exception. That is why every call is wrapped in try/catch.
 */
@Component
public class LoggingClient {

	private static final Logger logger = LoggerFactory.getLogger(LoggingClient.class);

	private final RestTemplate restTemplate;

	// Base URL of the .NET logging microservice.
	// Configured in application.properties as: logging.service.url
	@Value("${logging.service.url}")
	private String loggingServiceUrl;

	public LoggingClient(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	/**
	 * Sends one log entry to the logging microservice.
	 *
	 * @param action     short code for what happened, e.g. "BOOKING_CREATED"
	 * @param message    human readable description of the event
	 * @param userEmail  the user this event relates to (can be null)
	 */
	public void log(String action, String message, String userEmail) {

		try {

			Map<String, Object> body = new HashMap<>();
			body.put("serviceName", "smart-parking-backend");
			body.put("action", action);
			body.put("message", message);
			body.put("userEmail", userEmail);

			restTemplate.postForObject(loggingServiceUrl, body, String.class);

		} catch (Exception ex) {

			// Swallow the error on purpose - a logging failure should never
			// stop a booking / check-in / check-out from completing.
			logger.warn("Could not reach logging microservice ({}). Reason: {}", loggingServiceUrl,
					ex.getMessage());
		}
	}
}
