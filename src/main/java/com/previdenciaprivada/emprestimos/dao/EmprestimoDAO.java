package com.previdenciaprivada.emprestimos.dao;

import com.previdenciaprivada.emprestimos.models.Emprestimo;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface EmprestimoDAO {

    Emprestimo addEmprestimo(String CPF, BigDecimal valorParcela, int quantidadeParcelas);

    int updateEmprestimo(Emprestimo emprestimo);

    List<Emprestimo> getEmprestimosPorCPF(String CPF);

    Emprestimo getEmprestimoPorId(UUID idEmprestimo);
}
