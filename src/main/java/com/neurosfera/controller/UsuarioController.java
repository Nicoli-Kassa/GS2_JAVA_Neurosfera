package com.neurosfera.controller;

import com.neurosfera.repository.Distrito;
import com.neurosfera.repository.Usuario;
import com.neurosfera.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // GET - Listar todos os usuários
    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        List<Usuario> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    // GET - Buscar usuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(usuario);
    }

    // POST - Criar novo usuário
    @PostMapping
    public ResponseEntity<Usuario> criar(@Valid @RequestBody Usuario usuario) {
        Usuario novoUsuario = usuarioService.criar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    // PUT - Atualizar usuário
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Usuario usuario) {
        Usuario usuarioAtualizado = usuarioService.atualizar(id, usuario);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    // DELETE - Deletar usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // GET - Buscar por email
    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> buscarPorEmail(@PathVariable String email) {
        Usuario usuario = usuarioService.buscarPorEmail(email);
        return ResponseEntity.ok(usuario);
    }

    // GET - Buscar por tipo
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Usuario>> buscarPorTipo(@PathVariable String tipo) {
        List<Usuario> usuarios = usuarioService.buscarPorTipo(tipo);
        return ResponseEntity.ok(usuarios);
    }

    // GET - Buscar usuários ativos
    @GetMapping("/ativos")
    public ResponseEntity<List<Usuario>> buscarAtivos() {
        List<Usuario> usuarios = usuarioService.buscarAtivos();
        return ResponseEntity.ok(usuarios);
    }

    // PATCH - Alterar status
    @PatchMapping("/{id}/status")
    public ResponseEntity<Usuario> alterarStatus(
            @PathVariable Long id,
            @RequestParam Boolean status) {
        Usuario usuario = usuarioService.alterarStatus(id, status);
        return ResponseEntity.ok(usuario);
    }

    // PATCH - Alterar idioma
    @PatchMapping("/{id}/idioma")
    public ResponseEntity<Usuario> alterarIdioma(
            @PathVariable Long id,
            @RequestParam String idioma) {
        Usuario usuario = usuarioService.alterarIdioma(id, idioma);
        return ResponseEntity.ok(usuario);
    }

    // POST - Inscrever em distrito
    @PostMapping("/{usuarioId}/distritos/{distritoId}")
    public ResponseEntity<Usuario> inscreverNoDistrito(
            @PathVariable Long usuarioId,
            @PathVariable Long distritoId) {
        Usuario usuario = usuarioService.inscreverNoDistrito(usuarioId, distritoId);
        return ResponseEntity.ok(usuario);
    }

    // DELETE - Desinscrever de distrito
    @DeleteMapping("/{usuarioId}/distritos/{distritoId}")
    public ResponseEntity<Usuario> desinscreverDoDistrito(
            @PathVariable Long usuarioId,
            @PathVariable Long distritoId) {
        Usuario usuario = usuarioService.desinscreverDoDistrito(usuarioId, distritoId);
        return ResponseEntity.ok(usuario);
    }

    // GET - Listar distritos do usuário
    @GetMapping("/{usuarioId}/distritos")
    public ResponseEntity<Set<Distrito>> listarDistritosDoUsuario(
            @PathVariable Long usuarioId) {
        Set<Distrito> distritos = usuarioService.listarDistritosDoUsuario(usuarioId);
        return ResponseEntity.ok(distritos);
    }
}