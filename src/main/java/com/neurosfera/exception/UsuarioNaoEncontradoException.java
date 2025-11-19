package com.neurosfera.exception;

public class UsuarioNaoEncontradoException extends RuntimeException {

    public UsuarioNaoEncontradoException(Long id) {
        super("Usuário não encontrado com ID: " + id);
    }

    public UsuarioNaoEncontradoException(String email) {
        super("Usuário não encontrado com email: " + email);
    }

    public UsuarioNaoEncontradoException(String mensagem, boolean custom) {
        super(mensagem);
    }
}