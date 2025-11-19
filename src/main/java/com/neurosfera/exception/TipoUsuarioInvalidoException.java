package com.neurosfera.exception;

public class TipoUsuarioInvalidoException extends RuntimeException {

    public TipoUsuarioInvalidoException(String mensagem) {
        super(mensagem);
    }
}