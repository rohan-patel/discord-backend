package com.rohan.discordclone.controller.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rohan.discordclone.exception.EntityAlreadyExistsException;
import com.rohan.discordclone.exception.EntityNotFoundException;
import com.rohan.discordclone.model.User;
import com.rohan.discordclone.model.UserDetails;
import com.rohan.discordclone.model.dto.auth.LoginUserDto;
import com.rohan.discordclone.model.dto.auth.RegisterUserDto;
import com.rohan.discordclone.repository.UserDetailsRepository;
import com.rohan.discordclone.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private UserDetailsRepository userDetailsRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/register")
	public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUser) {
		
		String email = registerUser.getEmail().toLowerCase();
		String username = registerUser.getUsername();
		
		if (userRepo.findByEmail(email).isPresent()) {
			throw new EntityAlreadyExistsException("A user with email " + email + " already exists in the database.");
		}
		if (userRepo.findByUsername(username).isPresent()) {
			throw new EntityAlreadyExistsException("A user with username " + username + " already exists in the database.");
		}
		
//		System.out.println("Implementing without exception");
		
		String hashPassword = passwordEncoder.encode(registerUser.getPassword());
//		List<String> friends = new ArrayList<>();
		String userId = UUID.randomUUID().toString();
		User user = new User(userId, email, username, hashPassword);
		userRepo.save(user);
		
		List<String> friends = new ArrayList<String>();
		UserDetails userDetails = new UserDetails(userId, email, username, friends);
		userDetailsRepo.save(userDetails);
		
//		System.out.println("-----------------------User Registered-----------------------");
		
		
		return new ResponseEntity<User>(user, HttpStatus.CREATED);
	}
	
	@PostMapping("/login")
	public String login(@Valid @RequestBody LoginUserDto loginUser) {
		
		String principal = loginUser.getPrincipal();
		String password = loginUser.getPassword();
		
		User user;
		
		if (principal.contains(".")) {
			System.out.println("Inside Email Authentication");
			if (!userRepo.findByEmail(principal).isPresent()) {
				throw new EntityNotFoundException("The user with email " + principal + " was not found in the database");
			}
			user = userRepo.findByEmail(principal).get();
		} else {
			System.out.println("Inside Username Authentication");
			if (!userRepo.findByUsername(principal).isPresent()) {
				throw new EntityNotFoundException("The user with username " + principal + " was not found in the database");
			}
			user = userRepo.findByUsername(principal).get();
		}
		
		if (passwordEncoder.matches(password, user.getPassword())) {
			return "login successfull";
		}
		
		return "login failed. Password Mismatch.";
	}
}
