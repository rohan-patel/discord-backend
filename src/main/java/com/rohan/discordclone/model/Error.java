package com.rohan.discordclone.model;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Error {

	private HttpStatus httpStatus;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime timestamp;

	private String message;

	private String details;

	public Error(HttpStatus httpStatus, String message, String details) {
		super();
		this.httpStatus = httpStatus;
		this.timestamp = LocalDateTime.now();
		this.message = message;
		this.details = details;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

}
