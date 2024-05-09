package com.previdenciaprivada.emprestimos.services;

import com.previdenciaprivada.emprestimos.dao.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.util.List;

@Service
public class InstituicaoService {

    private final InstituicaoDAO instituicaoDAO;
    private final EmprestimoDAOImpl emprestimoDAOImpl;
    private final RegistroRepository registroRepository;

    public InstituicaoService(InstituicaoDAO instituicaoDAO, EmprestimoDAOImpl emprestimoDAOImpl, RegistroRepository registroRepository) {
        this.instituicaoDAO = instituicaoDAO;
        this.emprestimoDAOImpl = emprestimoDAOImpl;
        this.registroRepository = registroRepository;
    }

    public long chaveParaId(String apiKey) {
        Instituicao instituicao = instituicaoDAO.getInstituicaoPorChave(apiKey).orElseThrow();
        return instituicao.getId();
    }

    public void atualizarChave(String apiKey, String novaChave) {
        Instituicao instituicao = instituicaoDAO.getInstituicaoPorChave(apiKey).orElseThrow();

        instituicao.setChaveAPI(novaChave);
        instituicaoDAO.updateInstituicao(instituicao);
    }

    @Transactional
    public void deletarInstituicao(String apiKey, String cnpj) {
        this.transferirDados(this.chaveParaId(apiKey), apiKey);
        instituicaoDAO.deleteInstituicao(cnpj);
    }

    public List<Emprestimo> getEmprestimosPorInstituicao(long idInstituicao) {
        return emprestimoDAOImpl.getEmprestimosPorInstituicao(idInstituicao).orElseThrow();
    }

    public void transferirDados(long idInstituicao, String chave) {
        Instituicao instituicao = instituicaoDAO.getInstituicaoPorChave(chave).orElseThrow();
        List<Emprestimo> emprestimoList = this.getEmprestimosPorInstituicao(idInstituicao);

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