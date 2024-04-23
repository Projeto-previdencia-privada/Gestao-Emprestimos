package com.previdenciaprivada.emprestimos.dao;

import com.previdenciaprivada.emprestimos.services.Auth;
import com.previdenciaprivada.emprestimos.services.InstituicaoService;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class InstituicaoDAO {

    private final InstituicaoRepository instituicaoRepository;

    public InstituicaoDAO(InstituicaoRepository instituicaoRepository) {
        this.instituicaoRepository = instituicaoRepository;
    }

    public Instituicao addInstituicao(String nome, String cnpj) {
        Instituicao instituicao = new Instituicao();
        instituicao.setNome(nome);
        instituicao.setCNPJ(cnpj);
        instituicao.setChaveAPI(Auth.gerarChave());

        return instituicaoRepository.save(instituicao);
    }

    public Optional<Instituicao> getInstituicaoPorChave(String chave) {
        return instituicaoRepository.findByChaveAPI(chave);
    }

    public Optional<Instituicao> getInstituicaoPorCNPJ(String cnpj) {
        return instituicaoRepository.findByCNPJ(cnpj);
    }

    public void updateInstituicao(Instituicao instituicao) {
        instituicaoRepository.save(instituicao);
    }


}
