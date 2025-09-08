package com.infnet.empresa_crud.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_fornecedor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, length = 100)
    private String nomeEmpresa;

    @Column(unique = true, nullable = false, length = 14)
    private String cnpj;

    @Column(nullable = false, length = 100)
    private String nomeContato;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(length = 15)
    private String telefone;

    @Column(length = 200)
    private String endereco;

    @Column(length = 50)
    private String segmento;

    @Column(nullable = false)
    private Boolean ativo = true;
}