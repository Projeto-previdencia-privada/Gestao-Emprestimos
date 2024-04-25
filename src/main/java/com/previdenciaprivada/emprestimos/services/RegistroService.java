package com.previdenciaprivada.emprestimos.services;

import com.previdenciaprivada.emprestimos.dao.*;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

import java.math.BigDecimal;

@Service
public class RegistroService {

    private final EmprestimoService emprestimoService;
    private final RegistroRepository registroRepository;
    private final InstituicaoDAO instituicaoDAO;
    private final InstituicaoRepository instituicaoRepository;
    private final InstituicaoService instituicaoService;

    public RegistroService(EmprestimoService emprestimoService, RegistroRepository registroRepository, InstituicaoDAO instituicaoDAO, InstituicaoRepository instituicaoRepository, InstituicaoService instituicaoService) {
        this.emprestimoService = emprestimoService;
        this.registroRepository = registroRepository;
        this.instituicaoDAO = instituicaoDAO;
        this.instituicaoRepository = instituicaoRepository;
        this.instituicaoService = instituicaoService;
    }

    public void transferirDados(long idInstituicao, String chave) {
        Instituicao instituicao = instituicaoDAO.getInstituicaoPorChave(chave).orElseThrow();
        List<Emprestimo> emprestimoList = emprestimoService.getEmprestimosPorInstituicao(idInstituicao);

        for(Emprestimo emprestimo : emprestimoList) {
            Registro registro = new Registro();
            registro.setCNPJ(instituicao.getCNPJ());
            registro.setCPF(emprestimo.getCPF());
            registro.setNomeInstituicao(instituicao.getNome());
            registro.setValorEmprestimo(emprestimo.getValorParcela().multiply(BigDecimal.valueOf(emprestimo.getQuantidadeParcelas())));
            registro.setQuantidadeParcela(emprestimo.getQuantidadeParcelas());
            registro.setDataEmprestimo(emprestimo.getDataEmprestimo());

            registroRepository.save(registro);
        }

    }
}
