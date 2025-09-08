package com.infnet.empresa_crud.repository;

import com.infnet.empresa_crud.entity.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
    Optional<Fornecedor> findByCnpj(String cnpj);
    Optional<Fornecedor> findByEmail(String email);
    List<Fornecedor> findBySegmento(String segmento);
    List<Fornecedor> findByAtivoTrue();
}