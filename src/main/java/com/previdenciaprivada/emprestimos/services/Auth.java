package com.previdenciaprivada.emprestimos.services;

import com.previdenciaprivada.emprestimos.dao.Emprestimo;
import com.previdenciaprivada.emprestimos.dao.EmprestimoDAO;
import com.previdenciaprivada.emprestimos.dao.Instituicao;
import com.previdenciaprivada.emprestimos.dao.InstituicaoDAO;
import org.springframework.security.crypto.bcrypt.BCrypt;
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

        return Base64.getUrlEncoder().encodeToString(bytes);
    }

    public static String hashChave(String chave) {
        return BCrypt.hashpw(chave, BCrypt.gensalt(12));
    }

    public boolean validarCadastro(String cnpj, String apiKey) {
        Instituicao instituicao = instituicaoDAO.getInstituicaoPorCNPJ(cnpj).orElseThrow();
        return BCrypt.checkpw(apiKey, instituicao.getChaveAPI());
    }

    public boolean autenticarOperacaoEmprestimo(String apiKey, String idEmprestimo) {
        Emprestimo emprestimo = emprestimoDAO.getEmprestimoPorId(UUID.fromString(idEmprestimo)).orElseThrow();
        return BCrypt.checkpw(apiKey, emprestimo.getInstituicao().getChaveAPI());
    }

    public boolean autenticarOperacaoInstituicao(String apiKey, String cnpj) {
        Instituicao instituicao = instituicaoDAO.getInstituicaoPorCNPJ(cnpj).orElseThrow();
        return BCrypt.checkpw(apiKey, instituicao.getChaveAPI());
    }
}
