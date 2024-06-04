package com.previdenciaprivada.emprestimos.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="registros")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Registro {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name="cpf", length=11)
    private String CPF;

    @Column(name="valor_emprestimo", nullable = false, precision = 8, scale = 2)
    private BigDecimal valorEmprestimo;

    @Column(name="quantidade_parcelas")
    private int quantidadeParcela;

    @Column(name="nome_instituicao", length=100, nullable=false)
    private String nomeInstituicao;

    @Column(name="cnpj", length=14, nullable=false)
    private String CNPJ;

    @Column(name="data_emprestimo")
    private LocalDate dataEmprestimo;
}
