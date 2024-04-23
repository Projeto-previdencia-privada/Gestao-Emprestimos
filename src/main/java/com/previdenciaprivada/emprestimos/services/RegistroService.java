package com.previdenciaprivada.emprestimos.services;

import com.previdenciaprivada.emprestimos.dao.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RegistroService {

    private final EmprestimoService emprestimoService;
    private final RegistroRepository registroRepository;
    private final InstituicaoDAO instituicaoDAO;
    private final InstituicaoRepository instituicaoRepository;

    public RegistroService(EmprestimoService emprestimoService, RegistroRepository registroRepository, InstituicaoDAO instituicaoDAO, InstituicaoRepository instituicaoRepository) {
        this.emprestimoService = emprestimoService;
        this.registroRepository = registroRepository;
        this.instituicaoDAO = instituicaoDAO;
        this.instituicaoRepository = instituicaoRepository;
    }

    @Transactional
    public void transferirDados(long idInstituicao, String chave) {
        Instituicao instituicao = instituicaoDAO.getInstituicaoPorChave(chave).orElseThrow();
        instituicaoRepository.deleteById(idInstituicao);
        emprestimoService.getEmprestimosPorInstituicao(idInstituicao).forEach(emprestimo -> {
            Registro registro = new Registro();
            registro.setCNPJ(instituicao.getCNPJ());
            registro.setNomeInstituicao(instituicao.getNome());
            registro.setCPF(emprestimo.getCPF());
            registro.setValorEmprestimo(emprestimo.getValorParcela().multiply(BigDecimal.valueOf(emprestimo.getQuantidadeParcelas())));
            registro.setQuantidadeParcela(emprestimo.getQuantidadeParcelas());
            registro.setDataEmprestimo(emprestimo.getDataEmprestimo());

            registroRepository.save(registro);
        });

    }
}
