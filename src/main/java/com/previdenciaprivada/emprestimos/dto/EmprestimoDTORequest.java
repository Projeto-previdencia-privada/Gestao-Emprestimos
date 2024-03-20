package com.previdenciaprivada.emprestimos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EmprestimoDTORequest(
      @JsonProperty("cpf") String CPF,
      @JsonProperty("qtd-parcelas") int quantidadeParcelas,
      @JsonProperty("valor-parcela") double valorParcela
) {
}
