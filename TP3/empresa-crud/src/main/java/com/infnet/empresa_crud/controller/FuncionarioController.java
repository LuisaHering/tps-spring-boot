package com.infnet.empresa_crud.controller;

import com.infnet.empresa_crud.entity.Funcionario;
import com.infnet.empresa_crud.service.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioService service;

    // CREATE - POST /api/funcionarios
    @PostMapping
    public ResponseEntity<Funcionario> create(@RequestBody Funcionario funcionario) {
        try {
            Funcionario novoFuncionario = service.create(funcionario);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoFuncionario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // READ - GET /api/funcionarios (listar todos)
    @GetMapping
    public ResponseEntity<List<Funcionario>> getAll() {
        List<Funcionario> funcionarios = service.getAll();
        return ResponseEntity.ok(funcionarios);
    }

    // READ - GET /api/funcionarios/{id} (buscar por ID)
    @GetMapping("/{id}")
    public ResponseEntity<Funcionario> getById(@PathVariable Long id) {
        try {
            Funcionario funcionario = service.getById(id);
            return ResponseEntity.ok(funcionario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // READ - GET /api/funcionarios/cpf/{cpf} (buscar por CPF)
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Funcionario> getByCpf(@PathVariable String cpf) {
        try {
            Funcionario funcionario = service.getByCpf(cpf);
            return ResponseEntity.ok(funcionario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // UPDATE - PUT /api/funcionarios/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Funcionario> update(@PathVariable Long id, @RequestBody Funcionario funcionario) {
        try {
            Funcionario funcionarioAtualizado = service.update(id, funcionario);
            return ResponseEntity.ok(funcionarioAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // DELETE - DELETE /api/funcionarios/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Health check - GET /api/funcionarios/health
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Funcionario API is working!");
    }
}