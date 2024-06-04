package com.previdenciaprivada.emprestimos.dao;

import com.previdenciaprivada.emprestimos.services.Auth;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public class InstituicaoDAO {

    private final InstituicaoRepository instituicaoRepository;

    public InstituicaoDAO(InstituicaoRepository instituicaoRepository) {
        this.instituicaoRepository = instituicaoRepository;
    }

    public String addInstituicao(String nome, String cnpj) {
        String chave = Auth.gerarChave();
        Instituicao instituicao = new Instituicao();
        instituicao.setNome(nome);
        instituicao.setCNPJ(cnpj);
        instituicao.setChaveAPI(Auth.hashChave(chave));

        instituicaoRepository.save(instituicao);
        return chave;
    }

    public List<Instituicao> getAllInstituicao() {
        return instituicaoRepository.findAll();
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

    public void deleteInstituicao(String cnpj) {
        instituicaoRepository.deleteInstituicaoByCNPJ(cnpj);
    }


}
