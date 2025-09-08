package com.infnet.empresa_crud.service;

import com.infnet.empresa_crud.entity.Cliente;
import com.infnet.empresa_crud.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    public Cliente create(Cliente cliente) {
        if (cliente.getCpf() != null && repository.findByCpf(cliente.getCpf()).isPresent()) {
            throw new RuntimeException("CPF já cadastrado: " + cliente.getCpf());
        }
        return repository.save(cliente);
    }

    public Cliente getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + id));
    }

    public List<Cliente> getAll() {
        return repository.findAll();
    }

    public Cliente update(Long id, Cliente clienteAtualizado) {
        Cliente existente = getById(id);

        if (clienteAtualizado.getNome() != null) {
            existente.setNome(clienteAtualizado.getNome());
        }
        if (clienteAtualizado.getEmail() != null) {
            existente.setEmail(clienteAtualizado.getEmail());
        }
        if (clienteAtualizado.getTelefone() != null) {
            existente.setTelefone(clienteAtualizado.getTelefone());
        }
        if (clienteAtualizado.getEndereco() != null) {
            existente.setEndereco(clienteAtualizado.getEndereco());
        }
        if (clienteAtualizado.getDataNascimento() != null) {
            existente.setDataNascimento(clienteAtualizado.getDataNascimento());
        }
        if (clienteAtualizado.getAtivo() != null) {
            existente.setAtivo(clienteAtualizado.getAtivo());
        }

        return repository.save(existente);
    }

    public void delete(Long id) {
        Cliente cliente = getById(id);
        repository.delete(cliente);
    }
}