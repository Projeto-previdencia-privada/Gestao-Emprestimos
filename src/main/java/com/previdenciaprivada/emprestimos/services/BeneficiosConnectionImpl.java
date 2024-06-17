package com.previdenciaprivada.emprestimos.services;

import com.previdenciaprivada.emprestimos.dto.BeneficioDTOResponse;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

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
            System.out.println("\n\n\n\n\n\n\n\n");
            System.out.println(response);
            System.out.println(Double.parseDouble(Objects.requireNonNull(response.getBody().replaceAll("\\p{P}", ""))));
            return Double.parseDouble(Objects.requireNonNull(response.getBody().replaceAll("\\p{P}", "")));
        }
        // DEVE SER REFATORADO
        catch (RestClientException error) {
            return -1;
        }
    }
}
