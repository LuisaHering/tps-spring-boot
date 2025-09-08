package com.infnet.empresa_crud.service;

import com.infnet.empresa_crud.entity.Produto;
import com.infnet.empresa_crud.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    // CREATE
    public Produto create(Produto produto) {
        // Validação de código de barras único
        if (produto.getCodigoBarras() != null &&
                repository.findByCodigoBarras(produto.getCodigoBarras()).isPresent()) {
            throw new RuntimeException("Código de barras já cadastrado: " + produto.getCodigoBarras());
        }
        return repository.save(produto);
    }

    // READ - Buscar por ID
    public Produto getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + id));
    }

    // READ - Buscar todos
    public List<Produto> getAll() {
        return repository.findAll();
    }

    // READ - Buscar apenas ativos
    public List<Produto> getAllAtivos() {
        return repository.findByAtivoTrue();
    }

    // READ - Buscar por categoria
    public List<Produto> getByCategoria(String categoria) {
        return repository.findByCategoria(categoria);
    }

    // UPDATE
    public Produto update(Long id, Produto produtoAtualizado) {
        Produto produtoExistente = getById(id);

        // Atualiza campos não nulos
        if (produtoAtualizado.getNome() != null) {
            produtoExistente.setNome(produtoAtualizado.getNome());
        }
        if (produtoAtualizado.getDescricao() != null) {
            produtoExistente.setDescricao(produtoAtualizado.getDescricao());
        }
        if (produtoAtualizado.getPreco() != null) {
            produtoExistente.setPreco(produtoAtualizado.getPreco());
        }
        if (produtoAtualizado.getQuantidadeEstoque() != null) {
            produtoExistente.setQuantidadeEstoque(produtoAtualizado.getQuantidadeEstoque());
        }
        if (produtoAtualizado.getCategoria() != null) {
            produtoExistente.setCategoria(produtoAtualizado.getCategoria());
        }
        if (produtoAtualizado.getCodigoBarras() != null) {
            // Verifica se o código não está sendo usado por outro produto
            Optional<Produto> produtoComCodigo = repository.findByCodigoBarras(produtoAtualizado.getCodigoBarras());
            if (produtoComCodigo.isPresent() && !produtoComCodigo.get().getId().equals(id)) {
                throw new RuntimeException("Código de barras já cadastrado: " + produtoAtualizado.getCodigoBarras());
            }
            produtoExistente.setCodigoBarras(produtoAtualizado.getCodigoBarras());
        }
        if (produtoAtualizado.getAtivo() != null) {
            produtoExistente.setAtivo(produtoAtualizado.getAtivo());
        }

        return repository.save(produtoExistente);
    }

    // DELETE
    public void delete(Long id) {
        Produto produto = getById(id); // Verifica se existe
        repository.delete(produto);
    }

    // Soft delete - marca como inativo
    public Produto inativar(Long id) {
        Produto produto = getById(id);
        produto.setAtivo(false);
        return repository.save(produto);
    }

    // Reativar produto
    public Produto reativar(Long id) {
        Produto produto = getById(id);
        produto.setAtivo(true);
        return repository.save(produto);
    }
}