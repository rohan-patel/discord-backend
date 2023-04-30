package com.rohan.discordclone.security.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.rohan.discordclone.model.User;
import com.rohan.discordclone.security.constants.SecurityConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenValidatorFilter extends OncePerRequestFilter {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.header}")
	private String header;

//	@Autowired
//	private JwtProperties jwtProperties;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String header = request.getHeader(SecurityConstants.JWT_HEADER);
		String jwt = header.split(" ")[1];

		if (null != jwt) {
			try {

				SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));

				Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();

				String userId = String.valueOf(claims.get("userId"));
				String username = String.valueOf(claims.get("username"));
				String email = String.valueOf(claims.get("email"));
				
				User principal = new User(userId, email, username);

				Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, null);

				SecurityContextHolder.getContext().setAuthentication(auth);

			} catch (Exception e) {
				throw new BadCredentialsException("Invalid Token Received");
			}
		}

		filterChain.doFilter(request, response);

	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return request.getHeader("Authorization").split(" ")[0].equals("Basic");
//		return request.getServletPath().equals("/discord-clone/api/test");
	}

}
