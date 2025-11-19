package com.neurosfera.exception;

public class UsuarioJaInscritoException extends RuntimeException {

    public UsuarioJaInscritoException() {
        super("Usuário já inscrito neste distrito");
    }

    public UsuarioJaInscritoException(String mensagem) {
        super(mensagem);
    }
}