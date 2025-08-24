package com.infnet.tp2_crud.repository;

import com.infnet.tp2_crud.entity.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {
    // Métodos CRUD automáticos: save(), findById(), findAll(), deleteById()

    // Métodos extras para validação
    Optional<Livro> findByIsbn(String isbn);
    boolean existsByIsbn(String isbn);
}