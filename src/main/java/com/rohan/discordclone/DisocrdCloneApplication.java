package com.rohan.discordclone;

import java.nio.file.Path;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.rohan.discordclone.properties.connection.DataStaxAstraProperties;

@SpringBootApplication
@EnableConfigurationProperties(DataStaxAstraProperties.class)
public class DisocrdCloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(DisocrdCloneApplication.class, args);
	}

	@Bean
	public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProerties) {
		Path bundle = astraProerties.getSecureConnectBundle().toPath();
		return builder -> builder.withCloudSecureConnectBundle(bundle);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
