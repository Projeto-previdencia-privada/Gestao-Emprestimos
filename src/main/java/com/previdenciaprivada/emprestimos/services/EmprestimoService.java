package com.previdenciaprivada.emprestimos.services;

import com.previdenciaprivada.emprestimos.dao.EmprestimoDAO;
import com.previdenciaprivada.emprestimos.dto.EmprestimoDTORequest;
import com.previdenciaprivada.emprestimos.models.Emprestimo;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class EmprestimoService {
    double somaBeneficios;
    final double taxaRestricao = 0.3;
    private final EmprestimoDAO emprestimoDAO;

    public EmprestimoService(EmprestimoDAO emprestimoDAO) {
        this.emprestimoDAO = emprestimoDAO;
    }

    public String addEmprestimo(EmprestimoDTORequest emprestimoInfo) {
        BigDecimal taxa = new BigDecimal(taxaRestricao);
        BigDecimal restricao = new BigDecimal(somaBeneficios).multiply(taxa);
        BigDecimal valorParcela = new BigDecimal(emprestimoInfo.valorParcela());
        Emprestimo idEmprestimo =  emprestimoDAO.addEmprestimo(emprestimoInfo.CPF(), new BigDecimal(emprestimoInfo.valorParcela()), emprestimoInfo.quantidadeParcelas());
        return idEmprestimo.getIdEmprestimo().toString();
    }

    public List<Emprestimo> getEmprestimo(String CPF) {
        return emprestimoDAO.getEmprestimo(CPF);
    }
}
