package com.infnet.empresa_crud.repository;

import com.infnet.empresa_crud.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    Optional<Produto> findByCodigoBarras(String codigoBarras);
    List<Produto> findByCategoria(String categoria);
    List<Produto> findByAtivoTrue();
}
