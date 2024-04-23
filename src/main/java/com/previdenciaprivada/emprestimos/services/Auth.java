package com.previdenciaprivada.emprestimos.services;

import com.previdenciaprivada.emprestimos.dao.Emprestimo;
import com.previdenciaprivada.emprestimos.dao.EmprestimoDAO;
import com.previdenciaprivada.emprestimos.dao.Instituicao;
import com.previdenciaprivada.emprestimos.dao.InstituicaoDAO;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

@Component
public class Auth {

    private final InstituicaoDAO instituicaoDAO;
    private final EmprestimoDAO emprestimoDAO;

    public Auth(InstituicaoDAO instituicaoDAO, EmprestimoDAO emprestimoDAO) {
        this.instituicaoDAO = instituicaoDAO;
        this.emprestimoDAO = emprestimoDAO;
    }

    public static String gerarChave() {
        SecureRandom number = new SecureRandom();
        byte[] bytes = new byte[32];
        number.nextBytes(bytes);

        return Base64.getEncoder().encodeToString(bytes);
    }

    public long validarCadastro(String apiKey) {
        Instituicao instituicao = instituicaoDAO.getInstituicaoPorChave(apiKey).orElseThrow();
        return  instituicao.getId();
    }

    public boolean autenticarOperacaoEmprestimo(String apiKey, String idEmprestimo) {
        Emprestimo emprestimo = emprestimoDAO.getEmprestimoPorId(UUID.fromString(idEmprestimo)).orElseThrow();

        return emprestimo.getInstituicao().getChaveAPI().equals(apiKey);
    }

    public boolean autenticarOperacaoInstituicao(String apiKey, String cnpj) {
        Instituicao instituicao = instituicaoDAO.getInstituicaoPorCNPJ(cnpj).orElseThrow();
        return instituicao.getChaveAPI().equals(apiKey);
    }
}
