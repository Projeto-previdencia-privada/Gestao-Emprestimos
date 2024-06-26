package com.previdenciaprivada.emprestimos.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Set;

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

    @Column(name = "nome_imagem")
    private String image_path;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="instituicao")
    private Set<Emprestimo> emprestimos;

}
