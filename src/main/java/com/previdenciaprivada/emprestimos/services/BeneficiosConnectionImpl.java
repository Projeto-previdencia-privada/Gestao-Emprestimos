package com.previdenciaprivada.emprestimos.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;



@Repository
public class BeneficiosConnectionImpl implements BeneficiosConnection{
    @Override
    public double getSomaBeneficios(String CPF) {
        String URL =  "http://www.randomnumberapi.com/api/v1.0/random?min=200&max=10000";
        RestTemplate connection = new RestTemplate();

        ResponseEntity<String> response = connection.getForEntity(URL, String.class);
        //return Double.parseDouble(Objects.requireNonNull(response.getBody().replaceAll("\\p{P}", "")));
        return 12275.90;
    }
}
