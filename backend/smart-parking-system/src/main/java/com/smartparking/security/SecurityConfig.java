package com.smartparking.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable()).cors(Customizer.withDefaults())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth

						.requestMatchers("/api/auth/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
						.permitAll()

						// Attendant Management
						.requestMatchers("/api/attendants").hasRole("ADMIN").requestMatchers("/api/attendants/**")
						.hasRole("ADMIN")

						// Parking Property
						.requestMatchers(HttpMethod.POST, "/api/properties/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PUT, "/api/properties/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/api/properties/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.GET, "/api/properties/**")
						.hasAnyRole("ADMIN", "CUSTOMER", "ATTENDANT")

						// Floor
						.requestMatchers(HttpMethod.POST, "/api/floors/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PUT, "/api/floors/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/api/floors/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.GET, "/api/floors/**").hasAnyRole("ADMIN", "CUSTOMER", "ATTENDANT")

						// Slot
						.requestMatchers(HttpMethod.POST, "/api/slots/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PUT, "/api/slots/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/api/slots/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.GET, "/api/slots/**").hasAnyRole("ADMIN", "CUSTOMER", "ATTENDANT")

						// Vehicle
						.requestMatchers("/api/vehicles/**").hasRole("CUSTOMER")

						// Booking

						// Customer can create booking
						.requestMatchers(HttpMethod.POST, "/api/bookings/**").hasRole("CUSTOMER")

						// Customer can cancel booking
						.requestMatchers(HttpMethod.PUT, "/api/bookings/**").hasRole("CUSTOMER")

						// Admin, Customer and Attendant can view bookings
						.requestMatchers(HttpMethod.GET, "/api/bookings/**")
						.hasAnyRole("ADMIN", "CUSTOMER", "ATTENDANT")

						// Entry / Exit
						.requestMatchers("/api/entry/**").hasAnyRole("ADMIN", "ATTENDANT")
						.requestMatchers("/api/exit/**").hasAnyRole("ADMIN", "ATTENDANT")

						.anyRequest().authenticated())
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

		return configuration.getAuthenticationManager();
	}
}