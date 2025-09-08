package com.infnet.empresa_crud.service;

import com.infnet.empresa_crud.entity.Fornecedor;
import com.infnet.empresa_crud.repository.FornecedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FornecedorService {

    @Autowired
    private FornecedorRepository repository;

    public Fornecedor create(Fornecedor fornecedor) {
        if (fornecedor.getCnpj() != null && repository.findByCnpj(fornecedor.getCnpj()).isPresent()) {
            throw new RuntimeException("CNPJ já cadastrado: " + fornecedor.getCnpj());
        }
        return repository.save(fornecedor);
    }

    public Fornecedor getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado com ID: " + id));
    }

    public List<Fornecedor> getAll() {
        return repository.findAll();
    }

    public Fornecedor update(Long id, Fornecedor fornecedorAtualizado) {
        Fornecedor existente = getById(id);

        if (fornecedorAtualizado.getNomeEmpresa() != null) {
            existente.setNomeEmpresa(fornecedorAtualizado.getNomeEmpresa());
        }
        if (fornecedorAtualizado.getNomeContato() != null) {
            existente.setNomeContato(fornecedorAtualizado.getNomeContato());
        }
        if (fornecedorAtualizado.getEmail() != null) {
            existente.setEmail(fornecedorAtualizado.getEmail());
        }
        if (fornecedorAtualizado.getTelefone() != null) {
            existente.setTelefone(fornecedorAtualizado.getTelefone());
        }
        if (fornecedorAtualizado.getEndereco() != null) {
            existente.setEndereco(fornecedorAtualizado.getEndereco());
        }
        if (fornecedorAtualizado.getSegmento() != null) {
            existente.setSegmento(fornecedorAtualizado.getSegmento());
        }
        if (fornecedorAtualizado.getAtivo() != null) {
            existente.setAtivo(fornecedorAtualizado.getAtivo());
        }

        return repository.save(existente);
    }

    public void delete(Long id) {
        Fornecedor fornecedor = getById(id);
        repository.delete(fornecedor);
    }
}

