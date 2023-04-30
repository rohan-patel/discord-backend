package com.rohan.discordclone.security.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.rohan.discordclone.model.User;
import com.rohan.discordclone.security.constants.SecurityConstants;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenGeneratorFilter extends OncePerRequestFilter {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.header}")
	private String header;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		User principal = (User) auth.getPrincipal();
		
		if (null != auth) {
			SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));

			String jwt = Jwts.builder()
					.setIssuer("Discord")
					.setSubject("JWT Token")
					.claim("username", principal.getUsername())
					.claim("email", principal.getEmail())
					.claim("userId", principal.getUserId())
					.setIssuedAt(new Date()).setExpiration(new Date(new Date().getTime() + 30000000))
					.signWith(key)
					.compact();
			
//			String jwt = Utils.generateJwtToken();
			
			response.setHeader(SecurityConstants.JWT_HEADER, jwt);
		}
		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return request.getHeader(SecurityConstants.JWT_HEADER).split(" ")[0].equals("Bearer");
//		return !request.getServletPath().equals("/discord-clone/api/test");
	}
	
	

}
