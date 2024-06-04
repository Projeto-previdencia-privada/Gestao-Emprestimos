package com.previdenciaprivada.emprestimos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EmprestimoDTORequest(
      @JsonProperty(value = "cpf", required=true) String CPF,
      @JsonProperty(value = "qtd-parcelas", required=true) int quantidadeParcelas,
      @JsonProperty(value = "valor-parcela", required=true) double valorParcela,
      @JsonProperty(value = "cnpj", required = true) String cnpj
) {
}
