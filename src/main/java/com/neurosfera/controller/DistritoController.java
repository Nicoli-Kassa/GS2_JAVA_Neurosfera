package com.neurosfera.controller;

import com.neurosfera.repository.Distrito;
import com.neurosfera.service.DistritoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/distritos")
@CrossOrigin(origins = "*")
public class DistritoController {

    @Autowired
    private DistritoService distritoService;

    // GET - Listar todos os distritos
    @GetMapping
    public ResponseEntity<List<Distrito>> listarTodos() {
        List<Distrito> distritos = distritoService.listarTodos();
        return ResponseEntity.ok(distritos);
    }

    // GET - Buscar distrito por ID
    @GetMapping("/{id}")
    public ResponseEntity<Distrito> buscarPorId(@PathVariable Long id) {
        Distrito distrito = distritoService.buscarPorId(id);
        return ResponseEntity.ok(distrito);
    }

    // POST - Criar novo distrito
    @PostMapping
    public ResponseEntity<Distrito> criar(@Valid @RequestBody Distrito distrito) {
        Distrito novoDistrito = distritoService.criar(distrito);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoDistrito);
    }

    // PUT - Atualizar distrito
    @PutMapping("/{id}")
    public ResponseEntity<Distrito> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Distrito distrito) {
        Distrito distritoAtualizado = distritoService.atualizar(id, distrito);
        return ResponseEntity.ok(distritoAtualizado);
    }

    // DELETE - Deletar distrito
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        distritoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // GET - Buscar por área
    @GetMapping("/area/{area}")
    public ResponseEntity<List<Distrito>> buscarPorArea(@PathVariable String area) {
        List<Distrito> distritos = distritoService.buscarPorArea(area);
        return ResponseEntity.ok(distritos);
    }

    // GET - Buscar distritos ativos
    @GetMapping("/ativos")
    public ResponseEntity<List<Distrito>> buscarAtivos() {
        List<Distrito> distritos = distritoService.buscarAtivos();
        return ResponseEntity.ok(distritos);
    }

    // PATCH - Alterar status
    @PatchMapping("/{id}/status")
    public ResponseEntity<Distrito> alterarStatus(
            @PathVariable Long id,
            @RequestParam Boolean status) {
        Distrito distrito = distritoService.alterarStatus(id, status);
        return ResponseEntity.ok(distrito);
    }
}
