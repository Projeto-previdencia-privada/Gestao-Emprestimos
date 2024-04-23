package com.previdenciaprivada.emprestimos.dao;

import com.previdenciaprivada.emprestimos.services.Status;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class EmprestimoDAOImpl implements EmprestimoDAO{

    private final EmprestimoRepository emprestimoRepository;
    private final InstituicaoRepository instituicaoRepository;

    public EmprestimoDAOImpl(EmprestimoRepository emprestimoRepository, InstituicaoRepository instituicaoRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.instituicaoRepository = instituicaoRepository;
    }

    @Override
    public Emprestimo addEmprestimo(String CPF, BigDecimal valorParcela, int quantidadeParcelas, long id) {
        Emprestimo emprestimo = new Emprestimo();

        emprestimo.setIdEmprestimo(UUID.randomUUID());
        emprestimo.setCPF(CPF);
        emprestimo.setValorParcela(valorParcela);
        emprestimo.setQuantidadeParcelas(quantidadeParcelas);
        emprestimo.setDataEmprestimo(LocalDate.now());
        emprestimo.setStatus(Status.Ativo);
        emprestimo.setInstituicao(instituicaoRepository.findById(id).orElseThrow());
        emprestimoRepository.save(emprestimo);
        return emprestimo;
    }

    @Override
    public void updateEmprestimo(Emprestimo emprestimo) {
        emprestimoRepository.save(emprestimo);
    }

    @Override
    public Optional<List<Emprestimo>> getEmprestimosPorCPF(String CPF) {
        return emprestimoRepository.findEmprestimoByCPFAndStatus(CPF, Status.Ativo);
    }

    @Override
    public Optional<Emprestimo> getEmprestimoPorId(UUID idEmprestimo) {
        return emprestimoRepository.findById(idEmprestimo);
    }

    @Override
    public Optional<List<Emprestimo>> getEmprestimosPorInstituicaoECPF(String CPF, long instituicao) {
        return emprestimoRepository.findEmprestimoByCPFAndInstituicaoId(CPF, instituicao);
    }

    public Optional<List<Emprestimo>> getEmprestimosPorInstituicao(long instituicao) {
        return emprestimoRepository.findEmprestimoByInstituicaoId(instituicao);
    }
}
