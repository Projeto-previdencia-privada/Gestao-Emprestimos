package com.previdenciaprivada.emprestimos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BeneficioDTOResponse(
        @JsonProperty(value = "valor-total", required=true) double valorTotal
) {
}
