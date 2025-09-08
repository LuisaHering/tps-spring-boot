package com.infnet.empresa_crud.repository;

import com.infnet.empresa_crud.entity.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProjetoRepository extends JpaRepository<Projeto, Long> {
    List<Projeto> findByStatus(Projeto.StatusProjeto status);
    List<Projeto> findByGerenteResponsavel(String gerenteResponsavel);
}