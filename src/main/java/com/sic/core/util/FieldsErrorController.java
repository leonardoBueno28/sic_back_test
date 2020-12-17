package com.sic.core.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;

public class FieldsErrorController {
	public final static String MESSAGE = "message";
    public final static String ERRORS = "errors";
    
    public static ResponseEntity<?> attachErrors(List<FieldError> fieldErrors) {
        List<Map<String, String>> errors = fieldErrors
                .stream()
                .map(FieldsErrorController::errorMessageFormat)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put(ERRORS, errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private static Map<String, String> errorMessageFormat(FieldError error) {
    	Map<String, String> fields = new HashMap<>();
        String message = error.getDefaultMessage();
        fields.put(error.getField(), message);
        return fields;
    }

    public static String attachErrorMessages(String... errors) {
        StringBuilder message = new StringBuilder();

        for(String error: errors) {
            message.append(errorMessageFormat(error));
        }

        return message.toString().trim();
    }

    private static String errorMessageFormat(String error) {
        String message = String.format("%s.", error);
        return message;
    }
}
