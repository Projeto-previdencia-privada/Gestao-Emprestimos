package com.previdenciaprivada.emprestimos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.previdenciaprivada.emprestimos.services.Status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record EmprestimoDTOView(
        @JsonProperty("id-emprestimo") UUID idEmprestimo,
        @JsonProperty("cpf") String cpf,
        @JsonProperty("valor-parcela") BigDecimal valorParcela,
        @JsonProperty("qtd-parcelas") int quantidadeParcelas,
        @JsonProperty("data-emprestimo") LocalDate dataEmprestimo,
        @JsonProperty("instituicao") String instituicao,
        @JsonProperty("status") Status status
) {
}
