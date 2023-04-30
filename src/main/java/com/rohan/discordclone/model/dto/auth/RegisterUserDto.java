package com.rohan.discordclone.model.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern.Flag;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUserDto {

	@NotEmpty(message = "username is required.")
	@Size(min = 3, max = 12, message = "username should be of length 3-12 characters.")
	private String username;

	@NotEmpty(message = "password is required.")
	@Size(min = 6, max = 12, message = "passwordshould be of length 6-12 characters.")
	private String password;

	@NotEmpty(message = "email is required.")
	@Email(message = "The email address is invalid", flags = { Flag.CASE_INSENSITIVE })
	private String email;
	
	public RegisterUserDto(
			@NotEmpty(message = "username is required.") @Size(min = 3, max = 12, message = "username should be of length 3-12 characters.") String username,
			@NotEmpty(message = "password is required.") @Size(min = 6, max = 12, message = "passwordshould be of length 6-12 characters.") String password,
			@NotEmpty(message = "email is required.") @Email(message = "The email address is invalid", flags = Flag.CASE_INSENSITIVE) String email) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
