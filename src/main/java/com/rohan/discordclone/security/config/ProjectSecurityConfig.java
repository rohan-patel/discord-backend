package com.rohan.discordclone.security.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.rohan.discordclone.security.filter.JwtTokenGeneratorFilter;
import com.rohan.discordclone.security.filter.JwtTokenValidatorFilter;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class ProjectSecurityConfig {

	@Autowired
	private EmailPwdAuthenticationProvider emailAuthProvider;

	@Autowired
	private UsernamePwdAuthenticationProvider usernameAuthProvider;
	
	@Autowired
	@Qualifier("customAuthenticationEntryPoint")
	private AuthenticationEntryPoint authEntryPoint;

	@Bean
	public AuthenticationManager authManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http
				.getSharedObject(AuthenticationManagerBuilder.class);

		authenticationManagerBuilder.authenticationProvider(emailAuthProvider);
		authenticationManagerBuilder.authenticationProvider(usernameAuthProvider);

		return authenticationManagerBuilder.build();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {

//		CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
//		requestHandler.setCsrfRequestAttributeName("_csrf");
//		
//		CookieCsrfTokenRepository cookieCsrfTokenRepository = new CookieCsrfTokenRepository();
//		cookieCsrfTokenRepository.setCookieHttpOnly(false);
//		cookieCsrfTokenRepository.setCookieMaxAge(-1);
//		cookieCsrfTokenRepository.setSecure(false);
		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.cors().configurationSource(new CorsConfigurationSource() {
				
				@Override
				public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
					
					CorsConfiguration config = new CorsConfiguration();
					config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
					config.setAllowedMethods(Collections.singletonList("*"));
					config.setAllowCredentials(true);
					config.setAllowedHeaders(Collections.singletonList("*"));
					config.setExposedHeaders(Arrays.asList("Authorization"));
					config.setMaxAge(3600L);
					
					return config;
				}
			})
			.and()
			.csrf().disable()
//			.csrf((csrf) -> csrf.csrfTokenRequestHandler(requestHandler)
//					.csrfTokenRepository(cookieCsrfTokenRepository))
					
			.addFilterBefore(new JwtTokenValidatorFilter(), BasicAuthenticationFilter.class)
			.addFilterAfter(new JwtTokenGeneratorFilter(), BasicAuthenticationFilter.class)
//			.addFilterAfter(new CsrfCookieFilter(), JwtTokenGeneratorFilter.class)
			.authorizeHttpRequests()
				.anyRequest().authenticated()
			.and()
			.httpBasic()
			.and()
			.formLogin()
			.and()
			.exceptionHandling()
			.authenticationEntryPoint(authEntryPoint);
			
		return http.build();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/auth/register");
	}
	
}
