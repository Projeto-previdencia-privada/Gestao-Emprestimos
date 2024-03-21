package com.previdenciaprivada.emprestimos.services;

import com.previdenciaprivada.emprestimos.dao.EmprestimoDAO;
import com.previdenciaprivada.emprestimos.dto.EmprestimoDTORequest;
import com.previdenciaprivada.emprestimos.models.Emprestimo;
import com.previdenciaprivada.emprestimos.models.Status;
import org.springframework.stereotype.Service;


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

    public List<Emprestimo> getEmprestimos(String CPF) {
        return emprestimoDAO.getEmprestimosPorCPF(CPF);
    }

    public Emprestimo getEmprestimo(UUID idEmprestimo) {
        return emprestimoDAO.getEmprestimoPorId(idEmprestimo);
    }

    public int alterarValorParcela(UUID idEmprestimo, double valorParcela) {
        BigDecimal valorCorrigido = new BigDecimal(valorParcela);

        Emprestimo emprestimo = emprestimoDAO.getEmprestimoPorId(idEmprestimo);
        emprestimo.setValorParcela(valorCorrigido);

        emprestimoDAO.updateEmprestimo(emprestimo);
        return 0;
    }

    public int alterarQtdParcelas(UUID idEmprestimo, int quantidadeParcelas) {
        Emprestimo emprestimo = emprestimoDAO.getEmprestimoPorId(idEmprestimo);
        emprestimo.setQuantidadeParcelas(quantidadeParcelas);

        emprestimoDAO.updateEmprestimo(emprestimo);
        return 0;
    }


    public int finalizarEmprestimo(UUID idEmprestimo) {
        Emprestimo emprestimo = emprestimoDAO.getEmprestimoPorId(idEmprestimo);
        emprestimo.setStatus(Status.Concluido);

        emprestimoDAO.updateEmprestimo(emprestimo);

        return 0;
    }
   // public boolean verificarEmprestimo(BigDecimal valorParcela, String CPF) {

   // }
}
