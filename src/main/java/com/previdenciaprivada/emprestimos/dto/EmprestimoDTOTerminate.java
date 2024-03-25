package com.previdenciaprivada.emprestimos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record EmprestimoDTOTerminate(
        @JsonProperty("id-emprestimo") UUID idEmprestimo
) {
}
