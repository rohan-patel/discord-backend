package com.rohan.discordclone.socket.config;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOServer;
import com.rohan.discordclone.security.constants.SecurityConstants;
import com.rohan.discordclone.socket.model.Principal;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Configuration
@EnableScheduling
public class SocketIOConfig {

	@Value("${socket-server.host}")
	private String host;

	@Value("${socket-server.port}")
	private Integer port;
	
	@Bean
	public SocketIOServer socketIOServer() {
		com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
		
		config.setHostname(host);
		config.setPort(port);
		config.setOrigin("http://localhost:3000");
		config.setAuthorizationListener(new AuthorizationListener() {
			
			@Override
			public boolean isAuthorized(HandshakeData data) {
				
				try {
					String jwt = data.getSingleUrlParam("token");
					
					SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));
					Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
					
					String userId = String.valueOf(claims.get("userId"));
					
					Principal.setPrincipal(userId);
					
					System.out.println("Authentication true");
					return true;
					
				} catch (Exception e) {
					System.out.println("Authentication false");
					return false;
				}
				
			}
		});
		
		return new SocketIOServer(config);
	}
	
}
