package com.rohan.discordclone.model.dto.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginUserDto {

	@NotEmpty(message = "password is required.")
	@Size(min = 6, max = 12, message = "passwordshould be of length 6-12 characters.")
	private String password;

	@NotEmpty(message = "principal is required.")
	private String principal;

	public String getPassword() {
		return password;
	}

	public String getPrincipal() {
		return principal;
	}

}
