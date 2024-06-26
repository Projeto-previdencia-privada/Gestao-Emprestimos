package com.previdenciaprivada.emprestimos.services;

import com.previdenciaprivada.emprestimos.dao.EmprestimoDAO;
import com.previdenciaprivada.emprestimos.dao.Instituicao;
import com.previdenciaprivada.emprestimos.dto.EmprestimoDTORequest;
import com.previdenciaprivada.emprestimos.dao.Emprestimo;
import com.previdenciaprivada.emprestimos.dto.EmprestimoDTOView;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmprestimoService {
    private static final BigDecimal taxaRestricao = new BigDecimal("0.3");

    private final EmprestimoDAO emprestimoDAO;
    private final BeneficiosConnection beneficiosConnection;

    public EmprestimoService(EmprestimoDAO emprestimoDAO, BeneficiosConnection beneficiosConnection, InstituicaoService instituicaoService) {
        this.emprestimoDAO = emprestimoDAO;
        this.beneficiosConnection = beneficiosConnection;
    }

    public String addEmprestimo(EmprestimoDTORequest emprestimoInfo, Instituicao instituicao) throws NumberFormatException{
            if(!this.validarCPF( emprestimoInfo.CPF() )) {
                throw new NumberFormatException("CPF invalido");
            }

            if(this.verificarEmprestimo( BigDecimal.valueOf(emprestimoInfo.valorParcela()), emprestimoInfo.CPF()) ){
                    return "";
            }

            Emprestimo idEmprestimo = emprestimoDAO.addEmprestimo(emprestimoInfo.CPF(), BigDecimal.valueOf(emprestimoInfo.valorParcela()), emprestimoInfo.quantidadeParcelas(), instituicao);
            return idEmprestimo.getIdEmprestimo().toString();
    }

    public List<Emprestimo> getEmprestimos(String CPF) {
        return emprestimoDAO.getEmprestimosPorCPF(CPF).orElseThrow();
    }

    public Emprestimo getEmprestimo(UUID idEmprestimo) {

        return emprestimoDAO.getEmprestimoPorId(idEmprestimo).orElseThrow();
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
                .reduce(new BigDecimal(0), (somaAcumulada, parcela) -> somaAcumulada.add(parcela));

        BigDecimal novoTotal = somaParcelas.add(valorParcela);
        BigDecimal totalDisponivel = valorTotalBeneficios.multiply(taxaRestricao);

        /* Se a subtração entre o valor da nova parcela, acrescido do total de parcelas ativas, e o total de crédito disponivel for maior que zero
        , o usuário não possui crédito sufuciente logo a comparação será verdadeira */

        return novoTotal.compareTo(totalDisponivel) > 0;

   }

    public boolean validarCPF(String CPF) {
        if(CPF.length() == 11) {
            try {
                Long.valueOf(CPF);
                return true;
            }
            catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    public List<Emprestimo> getEmprestimosPorCPF(String cpf) throws NoSuchElementException {
        return emprestimoDAO.getEmprestimoPorCPF(cpf).orElseThrow();
    }

    public List<EmprestimoDTOView> emprestimoToEmprestimoDTO(List<Emprestimo> emprestimos) {
        List<EmprestimoDTOView> emprestimosView = emprestimos.stream()
                .map( (emprestimo) -> EmprestimoDTOView.valueOf(emprestimo)).collect(Collectors.toList());
        return emprestimosView;
    }

    public BigDecimal getTotalCredito(String cpf) {
        return BigDecimal.valueOf(beneficiosConnection.getSomaBeneficios(cpf)).multiply(taxaRestricao);
    }

    public BigDecimal getCreditoDisponivel(String cpf) {
        List<Emprestimo> emprestimoList = emprestimoDAO.getEmprestimosPorCPF(cpf).orElseThrow();

        BigDecimal somaParcelas = emprestimoList.stream()
                .map(emprestimo -> emprestimo.getValorParcela())
                .reduce(new BigDecimal(0), (somaAcumulada, parcela) -> somaAcumulada.add(parcela));

        return this.getTotalCredito(cpf).subtract(somaParcelas);
    }

    public BigDecimal getRendaTotal(String cpf) {
        return BigDecimal.valueOf(beneficiosConnection.getSomaBeneficios(cpf));
    }
}
