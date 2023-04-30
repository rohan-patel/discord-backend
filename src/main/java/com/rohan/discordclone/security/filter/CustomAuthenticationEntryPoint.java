package com.rohan.discordclone.security.filter;

import java.io.IOException;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rohan.discordclone.model.Error;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component("customAuthenticationEntryPoint")
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Autowired
	@Qualifier("handlerExceptionResolver")
	private HandlerExceptionResolver resolver;
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {

		String name = authException.getClass().getName();
		authException.printStackTrace();
		Error error = new Error(HttpStatus.UNAUTHORIZED, "Invalid Credentials", authException.getMessage());
		
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		
		OutputStream stream = response.getOutputStream();
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		
		mapper.writeValue(stream, error);
		
		stream.flush();
//		resolver.resolveException(request, response, null, authException);
		
	}

}
