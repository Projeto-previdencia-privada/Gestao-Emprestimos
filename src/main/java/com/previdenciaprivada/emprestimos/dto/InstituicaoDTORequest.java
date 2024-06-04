package com.previdenciaprivada.emprestimos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record InstituicaoDTORequest(
        @JsonProperty("nome") String nome,
        @JsonProperty("cnpj") String CNPJ
) {
}
