package com.rohan.discordclone.security.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.rohan.discordclone.exception.InvalidCredentialsException;
import com.rohan.discordclone.model.User;
import com.rohan.discordclone.repository.UserRepository;

@Component
public class EmailPwdAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String email = authentication.getName();
		String password = authentication.getCredentials().toString();

		Optional<User> optionalUser = userRepo.findByEmail(email);

		if (optionalUser.isPresent()) {
			if (passwordEncoder.matches(password, optionalUser.get().getPassword())) {
				return new UsernamePasswordAuthenticationToken(optionalUser.get(), password, null);
			} else {
				throw new InvalidCredentialsException("Invalid Password");
			}
		} else {
			throw new BadCredentialsException("No user registered with this details");
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}

}
