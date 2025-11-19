package com.neurosfera.neurosfera_api;

import com.neurosfera.controller.GlobalExceptionHandler;
import com.neurosfera.dto.ErrorResponse;
import com.neurosfera.exception.DistritoNaoEncontradoException;
import com.neurosfera.exception.EmailJaCadastradoException;
import com.neurosfera.exception.UsuarioNaoEncontradoException;
import com.neurosfera.exception.TipoUsuarioInvalidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        request = new MockHttpServletRequest();
    }

    @Test
    void handleUsuarioNaoEncontrado_retornaErro404() {
        // Arrange
        UsuarioNaoEncontradoException ex = new UsuarioNaoEncontradoException(1L);
        request.setRequestURI("/usuarios/1");

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleUsuarioNaoEncontrado(ex, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        // Não use getErro() - use os métodos que existem em ErrorResponse
    }

    @Test
    void handleDistritoNaoEncontrado_retornaErro404() {
        // Arrange
        DistritoNaoEncontradoException ex = new DistritoNaoEncontradoException(1L);
        request.setRequestURI("/distritos/1");

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleDistritoNaoEncontrado(ex, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void handleConflitosCadastro_EmailJaCadastrado_retornaErro409() {
        // Arrange
        EmailJaCadastradoException ex = new EmailJaCadastradoException("email@teste.com");
        request.setRequestURI("/usuarios");

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleConflitosCadastro(ex, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void handleConflitosCadastro_TipoUsuarioInvalido_retornaErro409() {
        // Arrange
        TipoUsuarioInvalidoException ex = new TipoUsuarioInvalidoException("Tipo inválido");
        request.setRequestURI("/usuarios");

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleConflitosCadastro(ex, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void handleUsuarioNaoEncontrado_retornaStatusCode404() {
        // Arrange
        UsuarioNaoEncontradoException ex = new UsuarioNaoEncontradoException(999L);
        request.setRequestURI("/usuarios/999");

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleUsuarioNaoEncontrado(ex, request);

        // Assert
        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void handleDistritoNaoEncontrado_retornaStatusCode404() {
        // Arrange
        DistritoNaoEncontradoException ex = new DistritoNaoEncontradoException(999L);
        request.setRequestURI("/distritos/999");

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleDistritoNaoEncontrado(ex, request);

        // Assert
        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void handleConflitosCadastro_retornaStatusCode409() {
        // Arrange
        EmailJaCadastradoException ex = new EmailJaCadastradoException("teste@teste.com");
        request.setRequestURI("/usuarios");

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleConflitosCadastro(ex, request);

        // Assert
        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals(409, response.getStatusCode().value());
    }
}
