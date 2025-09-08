package com.infnet.empresa_crud.repository;

import com.infnet.empresa_crud.entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    // Métodos de busca customizados
    Optional<Funcionario> findByCpf(String cpf);

    Optional<Funcionario> findByEmail(String email);

    // Métodos CRUD automáticos herdados:
    // save(), findById(), findAll(), deleteById(), delete(), count(), etc.
}