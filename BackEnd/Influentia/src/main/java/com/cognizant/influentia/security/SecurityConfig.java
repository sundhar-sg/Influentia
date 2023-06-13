package com.cognizant.influentia.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.cognizant.influentia.security.jwtconfig.JwtRequestFilter;
import com.cognizant.influentia.security.jwtconfig.JwtUtils;
import com.cognizant.influentia.security.service.UserService;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(configurer -> 
				configurer
//				.requestMatchers(HttpMethod.GET, "/api/content/**").hasRole("User")
//				.requestMatchers(HttpMethod.POST, "/api/content/**").hasRole("User")
//				.requestMatchers(HttpMethod.PUT, "/api/content/**").hasRole("User")
//				.requestMatchers(HttpMethod.GET, "/api/acount/**").hasRole("User")
//				.requestMatchers(HttpMethod.POST, "/api/account/**").hasRole("User")
//				.requestMatchers(HttpMethod.PUT, "/api/account/**").hasRole("User")
//				.requestMatchers(HttpMethod.GET, "/api/subscription/**").hasRole("User")
//				.requestMatchers(HttpMethod.POST, "/api/subscription/**").hasRole("User")
//				.requestMatchers(HttpMethod.PUT, "/api/subscription/**").hasRole("User")
				.requestMatchers("/**").permitAll()
				.requestMatchers("/api/signup").permitAll()
				.requestMatchers("/api/login").permitAll()
				.anyRequest().authenticated()
				)
		.formLogin((form) ->
				form.disable()
				)
		.cors(Customizer.withDefaults())
		.exceptionHandling((exception) -> 
				exception.authenticationEntryPoint((request, response, authException) -> {
						response.setStatus(HttpStatus.UNAUTHORIZED.value());
						response.setContentType("application/json");
						response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"Access denied\"}");
					})
				);
		http.csrf((csrf) -> csrf.disable());
		http.sessionManagement((session) ->
				session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				)
			.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userService);
		auth.setPasswordEncoder(passwordEncoder);
		return auth;
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedOrigin("http://localhost:4200");
		configuration.addAllowedOrigin("http://localhost:52427");
		configuration.addAllowedMethod("*");
		configuration.addAllowedHeader("Content-Type");
		configuration.addAllowedHeader("Access-Control-Allow-Headers");
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/api/**", configuration);
		return source;
	}
	
//	@Bean
//    public AuthenticationManager authManager(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userService)
//                .passwordEncoder(passwordEncoder());
//        return auth.build();
//    }
	
	public AuthenticationFailureHandler authenticationFailureHandler() {
		return new CustomAuthenticationFailureHandler();
	}
	
	@Bean
	public JwtUtils jwtUtil() {
		JwtUtils jwtUtils = new JwtUtils();
		return jwtUtils;
	}
}