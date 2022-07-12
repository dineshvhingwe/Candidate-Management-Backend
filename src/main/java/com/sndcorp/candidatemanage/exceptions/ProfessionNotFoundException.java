package com.sndcorp.candidatemanage.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProfessionNotFoundException extends RuntimeException{
    private static final long serialVersionUID = -28267242159090386L;
	private static String message = "Profession is not found For id: ";

	public ProfessionNotFoundException(String s) {
        super(message + s);
    }
	public ProfessionNotFoundException() {}
}
