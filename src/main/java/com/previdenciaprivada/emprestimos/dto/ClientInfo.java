package com.previdenciaprivada.emprestimos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record ClientInfo(
        @JsonProperty("credito-total") BigDecimal creditoTotal,
        @JsonProperty("renda-total") BigDecimal rendaTotal,
        @JsonProperty("credito-disponivel") BigDecimal creditoDisponivel
) {
}
