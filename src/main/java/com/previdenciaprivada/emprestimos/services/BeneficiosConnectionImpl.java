package com.previdenciaprivada.emprestimos.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
public class BeneficiosConnectionImpl implements BeneficiosConnection{
    @Override
    public double getSomaBeneficios(String CPF) {
        String URL =  "http://localhost:9000/concessao/soma?id=" + CPF;
        RestTemplate connection = new RestTemplate();

        ResponseEntity<String> response = connection.getForEntity(URL, String.class);
        System.out.println(response.getBody());
        return Double.parseDouble(Objects.requireNonNull(response.getBody().replaceAll("\\p{P}", "")));
    }
}
