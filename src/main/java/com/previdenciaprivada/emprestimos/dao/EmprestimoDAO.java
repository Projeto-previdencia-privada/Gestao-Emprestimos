package com.previdenciaprivada.emprestimos.dao;

import com.previdenciaprivada.emprestimos.models.Emprestimo;

import java.math.BigDecimal;
import java.util.List;

public interface EmprestimoDAO {

    Emprestimo addEmprestimo(String CPF, BigDecimal valorParcela, int quantidadeParcelas);

    //int updateEmprestimo();

    List<Emprestimo> getEmprestimo(String CPF);
}
