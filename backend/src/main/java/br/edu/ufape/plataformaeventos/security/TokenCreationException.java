package br.edu.ufape.plataformaeventos.security;

public class TokenCreationException extends RuntimeException {

    public TokenCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
