package com.previdenciaprivada.emprestimos.dao;

import com.previdenciaprivada.emprestimos.EmprestimosApplication;
import com.previdenciaprivada.emprestimos.models.Emprestimo;
import com.previdenciaprivada.emprestimos.models.EmprestimoRepository;
import com.previdenciaprivada.emprestimos.models.Status;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class EmprestimoDAOImpl implements EmprestimoDAO{

    private final EmprestimoRepository emprestimoRepository;

    public EmprestimoDAOImpl(EmprestimoRepository emprestimoRepository) {
        this.emprestimoRepository = emprestimoRepository;
    }

    @Override
    public Emprestimo addEmprestimo(String CPF, BigDecimal valorParcela, int quantidadeParcelas) {
        Emprestimo emprestimo = new Emprestimo(
                UUID.randomUUID(),
                CPF,
                valorParcela,
                quantidadeParcelas,
                LocalDate.now(),
                Status.Ativo
        );

        emprestimoRepository.save(emprestimo);
        return emprestimo;
    }

    @Override
    public int updateEmprestimo(Emprestimo emprestimo) {
        emprestimoRepository.save(emprestimo);
        return 0;
    }

    @Override
    public List<Emprestimo> getEmprestimosPorCPF(String CPF) {
        List<Emprestimo> emprestimos = emprestimoRepository.findEmprestimoByCPFAndStatus(CPF, Status.Ativo);
        return emprestimos;
    }

    @Override
    public Emprestimo getEmprestimoPorId(UUID idEmprestimo) {
        return emprestimoRepository.findById(idEmprestimo).get();
    }
}
