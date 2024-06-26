package com.previdenciaprivada.emprestimos.services;

import com.previdenciaprivada.emprestimos.dto.BeneficioDTOResponse;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Objects;

@Component
public class BeneficiosConnectionImpl implements BeneficiosConnection{
    @Override
    public double getSomaBeneficios(String CPF) {
        Dotenv dotenv = Dotenv.load();
        String URL =  "http://" + dotenv.get("SERVER_URL")  + "/api/concessao/soma/" + CPF;
        RestTemplate connection = new RestTemplate();

        try {
            ResponseEntity<String> response = connection.getForEntity(URL, String.class);
            if(response.getStatusCode().is2xxSuccessful()) {
                return Double.parseDouble(Objects.requireNonNull(response.getBody()));
            }
            return 0;
        }
        // DEVE SER REFATORADO
        catch (RestClientException error) {
            return 0;
        }
    }
}
