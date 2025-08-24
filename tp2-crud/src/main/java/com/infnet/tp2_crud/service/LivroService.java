package com.infnet.tp2_crud.service;

import com.infnet.tp2_crud.entity.Livro;
import com.infnet.tp2_crud.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LivroService {

    @Autowired
    private LivroRepository repository;

    public Livro create(Livro livro) {
        // Validação manual
        validarLivro(livro);

        // Verificar se ISBN já existe
        if (repository.existsByIsbn(livro.getIsbn())) {
            throw new IllegalArgumentException("ISBN já existe no sistema");
        }

        return repository.save(livro);
    }

    public List<Livro> getAll() {
        return repository.findAll();
    }

    public Livro getById(Long id) {
        Optional<Livro> livro = repository.findById(id);
        if (livro.isEmpty()) {
            throw new RuntimeException("Livro não encontrado com ID: " + id);
        }
        return livro.get();
    }

    public Livro update(Long id, Livro livro) {
        // Verificar se existe
        Livro existingLivro = getById(id); // Vai dar erro se não existir

        // Validar dados
        validarLivro(livro);

        // Verificar ISBN (só se mudou)
        if (!existingLivro.getIsbn().equals(livro.getIsbn()) &&
                repository.existsByIsbn(livro.getIsbn())) {
            throw new IllegalArgumentException("ISBN já existe no sistema");
        }

        // Atualizar campos (como na Aula 9)
        existingLivro.setTitulo(livro.getTitulo());
        existingLivro.setAutor(livro.getAutor());
        existingLivro.setIsbn(livro.getIsbn());
        existingLivro.setAnoPublicacao(livro.getAnoPublicacao());
        existingLivro.setDisponivel(livro.getDisponivel());

        return repository.save(existingLivro);
    }

    public void delete(Long id) {
        // Verificar se existe antes de deletar
        getById(id); // Vai dar erro se não existir
        repository.deleteById(id);
    }

    // Validação manual
    private void validarLivro(Livro livro) {
        if (livro.getTitulo() == null || livro.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("Título é obrigatório");
        }
        if (livro.getAutor() == null || livro.getAutor().trim().isEmpty()) {
            throw new IllegalArgumentException("Autor é obrigatório");
        }
        if (livro.getIsbn() == null || livro.getIsbn().trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN é obrigatório");
        }
        if (livro.getAnoPublicacao() == null || livro.getAnoPublicacao() < 1000 || livro.getAnoPublicacao() > 2025) {
            throw new IllegalArgumentException("Ano de publicação deve estar entre 1000 e 2025");
        }
    }
}