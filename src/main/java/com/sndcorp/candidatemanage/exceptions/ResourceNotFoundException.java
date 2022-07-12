package com.sndcorp.candidatemanage.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{
    private static final long serialVersionUID = -28267242159090386L;

    private static String message = " not for for Id: ";
	public ResourceNotFoundException(Object s1, Object s2) {
        super(s1.toString() + message + s2.toString());
    }
}
