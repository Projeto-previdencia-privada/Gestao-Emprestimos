package com.previdenciaprivada.emprestimos.services;

import com.previdenciaprivada.emprestimos.dao.EmprestimoDAO;
import com.previdenciaprivada.emprestimos.models.Emprestimo;
import com.previdenciaprivada.emprestimos.models.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

// Classe dedicada a testes unicamente unitários da camada de serviço

@ExtendWith(MockitoExtension.class)
class EmprestimoServiceTest {

    @Mock
    BeneficiosConnection beneficiosConnection;
    @Mock
    EmprestimoDAO emprestimoDAO;

    @InjectMocks
    EmprestimoService emprestimoService;



    @Test
    void verificarEmprestimo() {
        List<Emprestimo> listaEmprestimo = List.of(
                new Emprestimo(UUID.fromString("fa81f244-d11d-47ed-946d-e0165482cb1e"), "07304600020", new BigDecimal("123.25"), 12, LocalDate.parse("2024-02-14"), Status.Ativo),
                new Emprestimo(UUID.fromString("f931354c-6fc8-4147-8194-678d28bd8271"), "07304600020", new BigDecimal("720.80"), 24, LocalDate.parse("2023-11-09"), Status.Ativo),
                new Emprestimo(UUID.fromString("47d69d68-668f-4757-b4f9-bb9f57172da3"), "07304600020", new BigDecimal("540.00"), 32, LocalDate.parse("2024-02-10"), Status.Ativo)
        );

        Mockito.when(beneficiosConnection.getSomaBeneficios(Mockito.anyString())).thenReturn(3000.00);
        Mockito.when(emprestimoDAO.getEmprestimosPorCPF(Mockito.anyString())).thenReturn(Optional.of(listaEmprestimo));

        assertTrue(emprestimoService.verificarEmprestimo(new BigDecimal("300.00"), "07304600020"));
    }


}