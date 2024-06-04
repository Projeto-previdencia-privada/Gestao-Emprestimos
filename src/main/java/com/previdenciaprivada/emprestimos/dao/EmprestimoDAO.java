package com.previdenciaprivada.emprestimos.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmprestimoDAO {

    Emprestimo addEmprestimo(String CPF, BigDecimal valorParcela, int quantidadeParcelas, Instituicao instituicao);

    void updateEmprestimo(Emprestimo emprestimo);

    Optional<List<Emprestimo>> getEmprestimosPorCPF(String CPF);

    Optional<Emprestimo> getEmprestimoPorId(UUID idEmprestimo);

    Optional<List<Emprestimo>> getEmprestimosPorInstituicaoECPF(String CPF, long instituicao);

    Optional<List<Emprestimo>> getEmprestimosPorInstituicao(long instituicao);

    public Optional<List<Emprestimo>> getEmprestimoPorCPF(String cpf);
}
