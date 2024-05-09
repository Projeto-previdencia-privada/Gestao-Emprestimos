package com.previdenciaprivada.emprestimos.services;

import com.previdenciaprivada.emprestimos.dao.EmprestimoDAO;
import com.previdenciaprivada.emprestimos.dto.EmprestimoDTORequest;
import com.previdenciaprivada.emprestimos.dao.Emprestimo;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class EmprestimoService {
    private static final BigDecimal taxaRestricao = new BigDecimal("0.3");

    private final EmprestimoDAO emprestimoDAO;
    private final BeneficiosConnection beneficiosConnection;
    private final InstituicaoService instituicaoService;

    public EmprestimoService(EmprestimoDAO emprestimoDAO, BeneficiosConnection beneficiosConnection, InstituicaoService instituicaoService) {
        this.emprestimoDAO = emprestimoDAO;
        this.beneficiosConnection = beneficiosConnection;
        this.instituicaoService = instituicaoService;
    }

    public String addEmprestimo(EmprestimoDTORequest emprestimoInfo, long idInstituicao) throws NumberFormatException{
            this.validarCPF(emprestimoInfo.CPF());

            if(this.verificarEmprestimo( BigDecimal.valueOf(emprestimoInfo.valorParcela()), emprestimoInfo.CPF()) ){
                    return "";
            }

            Emprestimo idEmprestimo = emprestimoDAO.addEmprestimo(emprestimoInfo.CPF(), BigDecimal.valueOf(emprestimoInfo.valorParcela()), emprestimoInfo.quantidadeParcelas(), idInstituicao);
            return idEmprestimo.getIdEmprestimo().toString();
    }

    public List<Emprestimo> getEmprestimos(String CPF) {
        return emprestimoDAO.getEmprestimosPorCPF(CPF).orElseThrow();
    }

    public Emprestimo getEmprestimo(UUID idEmprestimo) {

        return emprestimoDAO.getEmprestimoPorId(idEmprestimo).orElseThrow();
    }

    public void alterarValorParcela(UUID idEmprestimo, double valorParcela) {
        BigDecimal valorCorrigido = new BigDecimal(valorParcela);

        Emprestimo emprestimo = emprestimoDAO.getEmprestimoPorId(idEmprestimo).orElseThrow();
        emprestimo.setValorParcela(valorCorrigido);

        emprestimoDAO.updateEmprestimo(emprestimo);
    }

    public void alterarQtdParcelas(UUID idEmprestimo, int quantidadeParcelas) {
        Emprestimo emprestimo = emprestimoDAO.getEmprestimoPorId(idEmprestimo).orElseThrow();
        emprestimo.setQuantidadeParcelas(quantidadeParcelas);

        emprestimoDAO.updateEmprestimo(emprestimo);
    }


    public void finalizarEmprestimo(UUID idEmprestimo) {
        Emprestimo emprestimo = emprestimoDAO.getEmprestimoPorId(idEmprestimo).orElseThrow();
        emprestimo.setStatus(Status.Concluido);

        emprestimoDAO.updateEmprestimo(emprestimo);
    }
   public boolean verificarEmprestimo(BigDecimal valorParcela, String CPF) {
        BigDecimal valorTotalBeneficios = BigDecimal.valueOf(beneficiosConnection.getSomaBeneficios(CPF));

        List<Emprestimo> emprestimos = emprestimoDAO.getEmprestimosPorCPF(CPF).orElseThrow();
        BigDecimal somaParcelas = emprestimos.stream()
                .map(emprestimo -> emprestimo.getValorParcela())
                .reduce(new BigDecimal(0), (a, b) -> a.add(b));

        BigDecimal novoTotal = somaParcelas.add(valorParcela);
        BigDecimal totalDisponivel = valorTotalBeneficios.multiply(taxaRestricao);

        /* Se a subtração entre o valor da nova parcela, acrescido do total de parcelas ativas, e o total de crédito disponivel for maior que zero
        , o usuário não possui crédito sufuciente logo a comparação será verdadeira */

        return novoTotal.compareTo(totalDisponivel) > 0;

   }
    public void validarCPF(String CPF) throws NumberFormatException {
        if(CPF.length() == 11) {
            Long.valueOf(CPF);
            return;
        }
        throw new NumberFormatException();
    }

    public List<Emprestimo> getEmprestimosPorCPF(String CPF, String apiKey) {
        long idInstituicao = instituicaoService.chaveParaId(apiKey);

        return emprestimoDAO.getEmprestimosPorInstituicaoECPF(CPF, idInstituicao).orElseThrow();
    }

}
