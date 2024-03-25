package com.previdenciaprivada.emprestimos.dao;

import com.previdenciaprivada.emprestimos.models.Emprestimo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmprestimoDAO {

    Emprestimo addEmprestimo(String CPF, BigDecimal valorParcela, int quantidadeParcelas);

    void updateEmprestimo(Emprestimo emprestimo);

    Optional<List<Emprestimo>> getEmprestimosPorCPF(String CPF);

    Optional<Emprestimo> getEmprestimoPorId(UUID idEmprestimo);
}
