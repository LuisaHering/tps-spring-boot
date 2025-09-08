package com.infnet.empresa_crud.service;

import com.infnet.empresa_crud.entity.Funcionario;
import com.infnet.empresa_crud.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository repository;

    // CREATE
    public Funcionario create(Funcionario funcionario) {
        // Validações básicas
        if (funcionario.getCpf() != null && repository.findByCpf(funcionario.getCpf()).isPresent()) {
            throw new RuntimeException("CPF já cadastrado: " + funcionario.getCpf());
        }
        if (funcionario.getEmail() != null && repository.findByEmail(funcionario.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado: " + funcionario.getEmail());
        }
        return repository.save(funcionario);
    }

    // READ - Buscar por ID
    public Funcionario getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado com ID: " + id));
    }

    // READ - Buscar todos
    public List<Funcionario> getAll() {
        return repository.findAll();
    }

    // READ - Buscar por CPF
    public Funcionario getByCpf(String cpf) {
        return repository.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado com CPF: " + cpf));
    }

    // UPDATE
    public Funcionario update(Long id, Funcionario funcionarioAtualizado) {
        Funcionario funcionarioExistente = getById(id);

        // Atualiza apenas os campos não nulos
        if (funcionarioAtualizado.getNome() != null) {
            funcionarioExistente.setNome(funcionarioAtualizado.getNome());
        }
        if (funcionarioAtualizado.getEmail() != null) {
            // Verifica se o email não está sendo usado por outro funcionário
            Optional<Funcionario> funcionarioComEmail = repository.findByEmail(funcionarioAtualizado.getEmail());
            if (funcionarioComEmail.isPresent() && !funcionarioComEmail.get().getId().equals(id)) {
                throw new RuntimeException("Email já cadastrado: " + funcionarioAtualizado.getEmail());
            }
            funcionarioExistente.setEmail(funcionarioAtualizado.getEmail());
        }
        if (funcionarioAtualizado.getCargo() != null) {
            funcionarioExistente.setCargo(funcionarioAtualizado.getCargo());
        }
        if (funcionarioAtualizado.getSalario() != null) {
            funcionarioExistente.setSalario(funcionarioAtualizado.getSalario());
        }
        if (funcionarioAtualizado.getDataAdmissao() != null) {
            funcionarioExistente.setDataAdmissao(funcionarioAtualizado.getDataAdmissao());
        }
        if (funcionarioAtualizado.getTelefone() != null) {
            funcionarioExistente.setTelefone(funcionarioAtualizado.getTelefone());
        }

        return repository.save(funcionarioExistente);
    }

    // DELETE
    public void delete(Long id) {
        Funcionario funcionario = getById(id); // Verifica se existe
        repository.delete(funcionario);
    }

    // Metodo para verificar se existe
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}