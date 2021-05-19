package com.projetojava.brewer.service.exception;

public class EmailUsuarioJaExistenteException extends RuntimeException {

    public EmailUsuarioJaExistenteException(String message) {
        super(message);
    }
}
