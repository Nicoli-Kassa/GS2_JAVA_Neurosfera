package com.neurosfera.exception;

public class NomeJaCadastradoException extends RuntimeException {

    public NomeJaCadastradoException(String nome) {
        super("Distrito já cadastrado com este nome: " + nome);
    }
}