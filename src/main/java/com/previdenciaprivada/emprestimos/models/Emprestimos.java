package com.previdenciaprivada.emprestimos.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;



@Entity
@Table(name = "Emprestimos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Emprestimos {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idEmprestimo;

    @Column(nullable = false, length = 11)
    private String CPF;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal valorParcela;
    @Column(nullable = false)
    private int quantidadeParcelas;

    private Instant dataEmprestimo;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Status status;

    @Override
    public String toString() {
        return "Emprestimos{" +
                "idEmprestimo=" + idEmprestimo +
                ", CPF='" + CPF + '\'' +
                ", valorParcela=" + valorParcela +
                ", quantidadeParcelas=" + quantidadeParcelas +
                ", dataEmprestimo=" + dataEmprestimo +
                ", status=" + status +
                '}';
    }
}

