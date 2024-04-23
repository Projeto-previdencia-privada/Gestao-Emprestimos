package com.previdenciaprivada.emprestimos.services;

import com.previdenciaprivada.emprestimos.dao.Instituicao;
import com.previdenciaprivada.emprestimos.dao.InstituicaoDAO;
import com.previdenciaprivada.emprestimos.dao.InstituicaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class InstituicaoService {

    private final InstituicaoDAO instituicaoDAO;
    private final InstituicaoRepository instituicaoRepository;

    public InstituicaoService(InstituicaoDAO instituicaoDAO, InstituicaoRepository instituicaoRepository) {
        this.instituicaoDAO = instituicaoDAO;
        this.instituicaoRepository = instituicaoRepository;
    }

    public long ChaveParaId(String apiKey) {
        Instituicao instituicao = instituicaoDAO.getInstituicaoPorChave(apiKey).orElseThrow();
        return instituicao.getId();
    }

    public void atualizarChave(String apiKey, String novaChave) {
        Instituicao instituicao = instituicaoDAO.getInstituicaoPorChave(apiKey).orElseThrow();

        instituicao.setChaveAPI(novaChave);
        instituicaoDAO.updateInstituicao(instituicao);
    }

    @Transactional
    public void deletar() {
        instituicaoRepository.deleteAll();
    }
}