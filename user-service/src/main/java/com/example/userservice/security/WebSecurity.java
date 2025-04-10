package com.example.userservice.security;

import java.util.function.Supplier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

import com.example.userservice.service.UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity {

	private final UserService userService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final Environment env;

		public static final String ALLOWED_IP_ADDRESS = "127.0.0.1";
		public static final String SUBNET = "/32";
		public static final IpAddressMatcher ALLOWED_IP_ADDRESS_MATCHER = new IpAddressMatcher(ALLOWED_IP_ADDRESS + SUBNET);

		@Bean
		public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
			AuthenticationManagerBuilder authenticationManagerBuilder = http
					.getSharedObject(AuthenticationManagerBuilder.class);
			authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);

		AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

		http.csrf((csrf) -> csrf.disable());
		
		http.authorizeHttpRequests((auth) -> auth
				.requestMatchers("/actuator/**").permitAll());	
		
		http.authorizeHttpRequests(requests -> requests 
						.anyRequest().permitAll()) 
				.authenticationManager(authenticationManager)
				.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.addFilter(getAuthenticationFilter(authenticationManager));
		http.headers((headers) -> headers.frameOptions((frameOptions) -> frameOptions.sameOrigin()));

		return http.build();
	}

	private AuthorizationDecision hasIpAddress(Supplier<Authentication> authentication,
			RequestAuthorizationContext object) { 
		return new AuthorizationDecision(ALLOWED_IP_ADDRESS_MATCHER.matches(object.getRequest()));
	}

	private AuthenticationFilter getAuthenticationFilter(AuthenticationManager authenticationManager) throws Exception { 
		AuthenticationFilter authenticationFilter = new AuthenticationFilter(userService, env);
		authenticationFilter.setAuthenticationManager(authenticationManager);
		return authenticationFilter;
	}

}
