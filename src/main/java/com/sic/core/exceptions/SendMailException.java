package com.sic.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class SendMailException extends RuntimeException {
	private static final long serialVersionUID = 4717222007988224185L;
	
	private final String FORMAT_MESSAGE = "No se ha podido enviar el correo de recuperarion. %s. %s";
	
	public SendMailException(String message, Throwable cause) {
		String.format(FORMAT_MESSAGE, message, cause.getMessage());
	}
}