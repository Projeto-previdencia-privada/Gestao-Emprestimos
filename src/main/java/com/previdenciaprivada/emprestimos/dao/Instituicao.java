package com.previdenciaprivada.emprestimos.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="instituicoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Instituicao {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name="nome", length=100, nullable=false)
    private String nome;

    @Column(name="cnpj", length=14, nullable=false)
    private String CNPJ;

    @Column(name="chave_acesso")
    private String chaveAPI;
}
