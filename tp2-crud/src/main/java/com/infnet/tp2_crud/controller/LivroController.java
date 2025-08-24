package com.infnet.tp2_crud.controller;

import com.infnet.tp2_crud.entity.Livro;
import com.infnet.tp2_crud.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    @Autowired
    private LivroService service;

    // CREATE - POST /api/livros
    @PostMapping
    public ResponseEntity<Livro> create(@RequestBody Livro livro) {
        Livro createdLivro = service.create(livro);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLivro);
    }

    // READ ALL - GET /api/livros
    @GetMapping
    public ResponseEntity<List<Livro>> getAll() {
        List<Livro> livros = service.getAll();
        return ResponseEntity.ok(livros);
    }

    // READ BY ID - GET /api/livros/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Livro> getById(@PathVariable Long id) {
        Livro livro = service.getById(id);
        return ResponseEntity.ok(livro);
    }

    // UPDATE - PUT /api/livros/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Livro> update(@PathVariable Long id, @RequestBody Livro livro) {
        Livro updatedLivro = service.update(id, livro);
        return ResponseEntity.ok(updatedLivro);
    }

    // DELETE - DELETE /api/livros/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}