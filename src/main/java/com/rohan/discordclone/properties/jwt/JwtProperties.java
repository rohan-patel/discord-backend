package com.rohan.discordclone.properties.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

	private String secret;

	private String header;

	public String getSecret() {
		return secret;
	}

	public String getHeader() {
		return header;
	}

}
