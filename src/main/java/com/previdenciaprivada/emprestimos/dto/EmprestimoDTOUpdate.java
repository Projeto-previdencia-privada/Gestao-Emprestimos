package com.previdenciaprivada.emprestimos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.previdenciaprivada.emprestimos.models.Emprestimo;

import java.util.UUID;

public record EmprestimoDTOUpdate(
        @JsonProperty("id-emprestimo") UUID idEmprestimo,
        @JsonProperty("novo-valor") double valor
) {
}
