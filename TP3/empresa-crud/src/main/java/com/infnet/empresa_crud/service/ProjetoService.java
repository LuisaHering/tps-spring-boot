package com.infnet.empresa_crud.service;

import com.infnet.empresa_crud.entity.Projeto;
import com.infnet.empresa_crud.repository.ProjetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProjetoService {

    @Autowired
    private ProjetoRepository repository;

    public Projeto create(Projeto projeto) {
        return repository.save(projeto);
    }

    public Projeto getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado com ID: " + id));
    }

    public List<Projeto> getAll() {
        return repository.findAll();
    }

    public List<Projeto> getByStatus(Projeto.StatusProjeto status) {
        return repository.findByStatus(status);
    }

    public Projeto update(Long id, Projeto projetoAtualizado) {
        Projeto existente = getById(id);

        if (projetoAtualizado.getNome() != null) {
            existente.setNome(projetoAtualizado.getNome());
        }
        if (projetoAtualizado.getDescricao() != null) {
            existente.setDescricao(projetoAtualizado.getDescricao());
        }
        if (projetoAtualizado.getDataInicio() != null) {
            existente.setDataInicio(projetoAtualizado.getDataInicio());
        }
        if (projetoAtualizado.getDataFimPrevista() != null) {
            existente.setDataFimPrevista(projetoAtualizado.getDataFimPrevista());
        }
        if (projetoAtualizado.getDataFimReal() != null) {
            existente.setDataFimReal(projetoAtualizado.getDataFimReal());
        }
        if (projetoAtualizado.getOrcamento() != null) {
            existente.setOrcamento(projetoAtualizado.getOrcamento());
        }
        if (projetoAtualizado.getStatus() != null) {
            existente.setStatus(projetoAtualizado.getStatus());
        }
        if (projetoAtualizado.getGerenteResponsavel() != null) {
            existente.setGerenteResponsavel(projetoAtualizado.getGerenteResponsavel());
        }

        return repository.save(existente);
    }

    public void delete(Long id) {
        Projeto projeto = getById(id);
        repository.delete(projeto);
    }
}
