package com.sic.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -6131724969500827721L;
	
	public UserNotFoundException(String message) {
		super(message);
	}
}
