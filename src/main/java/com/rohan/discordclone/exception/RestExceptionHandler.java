package com.rohan.discordclone.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.rohan.discordclone.model.Error;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(EntityAlreadyExistsException.class)
	@ResponseStatus(value = HttpStatus.CONFLICT)
	private ResponseEntity<Error> handleEntityAlreadyExistsException(EntityAlreadyExistsException ex) {
		Error error = new Error(HttpStatus.CONFLICT, "Entity Already Exists", ex.getMessage());
		return new ResponseEntity<Error>(error, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	private ResponseEntity<Error> handleEntityNotFoundException(EntityNotFoundException ex) {
		Error error = new Error(HttpStatus.BAD_REQUEST, "Entity Not Found", ex.getMessage());
		return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidCredentialsException.class)
	@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
	private ResponseEntity<Error> handleInvalidCredentialsException(InvalidCredentialsException ex) {
		Error error = new Error(HttpStatus.NOT_ACCEPTABLE, "Invalid Credentials", ex.getMessage());
		return new ResponseEntity<Error>(error, HttpStatus.NOT_ACCEPTABLE);
	}

	@ExceptionHandler(InvalidFriendInviteException.class)
	@ResponseStatus(value = HttpStatus.CONFLICT)
	private ResponseEntity<Error> handleInvalidFriendInviteException(InvalidFriendInviteException ex) {
		Error error = new Error(HttpStatus.CONFLICT, "Invalid Invite", ex.getMessage());
		return new ResponseEntity<Error>(error, HttpStatus.CONFLICT);
	}

}
