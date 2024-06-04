package com.previdenciaprivada.emprestimos.dao;

import com.previdenciaprivada.emprestimos.services.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;



@Entity
@Table(name = "Emprestimos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Emprestimo {
    @Id
    private UUID idEmprestimo;

    @Column(nullable = false, length = 11, name = "Cpf")
    private String CPF;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal valorParcela;
    @Column(nullable = false)
    private int quantidadeParcelas;

    private LocalDate dataEmprestimo;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Status status;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "instituicao_id", foreignKey = @ForeignKey(name = "INSTITUICAO_FK_KEY"))
    @OnDelete(action= OnDeleteAction.CASCADE)
    private Instituicao instituicao;

}

