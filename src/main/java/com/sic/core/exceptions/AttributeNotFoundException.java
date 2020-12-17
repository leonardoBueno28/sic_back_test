package com.sic.core.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AttributeNotFoundException extends RuntimeException {

	private static final String mensaje="No se encontró el atributo: ";
	public AttributeNotFoundException(String atributo) {
		super(mensaje+ atributo);
	}
	
	
}
