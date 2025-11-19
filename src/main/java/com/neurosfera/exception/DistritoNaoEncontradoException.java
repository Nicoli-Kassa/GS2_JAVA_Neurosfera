package com.neurosfera.exception;

public class DistritoNaoEncontradoException extends RuntimeException {

    public DistritoNaoEncontradoException(Long id) {
        super("Distrito não encontrado com ID: " + id);
    }

    public DistritoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}