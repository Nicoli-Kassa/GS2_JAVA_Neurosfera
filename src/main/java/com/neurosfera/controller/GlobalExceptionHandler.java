package com.neurosfera.controller;

import com.neurosfera.dto.ErrorResponse;
import com.neurosfera.exception.DistritoNaoEncontradoException;
import com.neurosfera.exception.EmailJaCadastradoException;
import com.neurosfera.exception.NomeJaCadastradoException;
import com.neurosfera.exception.UsuarioJaInscritoException;
import com.neurosfera.exception.UsuarioNaoEncontradoException;
import com.neurosfera.exception.TipoUsuarioInvalidoException;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleUsuarioNaoEncontrado(
            UsuarioNaoEncontradoException ex,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                "Usuário não encontrado",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(DistritoNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleDistritoNaoEncontrado(
            DistritoNaoEncontradoException ex,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                "Distrito não encontrado",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler({
            EmailJaCadastradoException.class,
            NomeJaCadastradoException.class,
            UsuarioJaInscritoException.class,
            TipoUsuarioInvalidoException.class
    })
    public ResponseEntity<ErrorResponse> handleConflitosCadastro(
            RuntimeException ex,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                "Conflito de cadastro",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}
