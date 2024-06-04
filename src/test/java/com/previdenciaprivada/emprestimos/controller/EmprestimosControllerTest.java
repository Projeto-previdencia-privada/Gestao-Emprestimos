package com.previdenciaprivada.emprestimos.controller;

import com.previdenciaprivada.emprestimos.dto.EmprestimoDTORequest;
import com.previdenciaprivada.emprestimos.dto.EmprestimoDTOTerminate;
import com.previdenciaprivada.emprestimos.dto.EmprestimoDTOUpdate;
import com.previdenciaprivada.emprestimos.dao.Emprestimo;
import com.previdenciaprivada.emprestimos.services.Status;
import com.previdenciaprivada.emprestimos.services.EmprestimoService;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.utility.MountableFile;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.UUID;

/* Os testes abaixo tem como propósito testar todos os end points da API tentando ser o mais fiel possível a um ambiente real. Os testes verificam
   se as URL's retornam valores válidos e se os dados são realmente gravados no banco de dados. */

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class EmprestimosControllerTest {

    @Autowired
    private EmprestimoService emprestimoService;

    @Autowired
    private EmprestimosController emprestimosController;
    @Autowired
    private MockMvc mockMvc;



    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:alpine3.19")
                    .withUsername("test")
                    .withPassword("test")
                    .withDatabaseName("gerenciamento")
                    .withCopyFileToContainer(
                            MountableFile.forClasspathResource("sql/init-schema.sql"),
                            "/docker-entrypoint-initdb.d/init-schema.sql")
                    .withCopyFileToContainer(
                            MountableFile.forClasspathResource("sql/seed-data.sql"),
                            "/docker-entrypoint-initdb.d/seed-data.sql");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry resgistry) {
        resgistry.add("spring.datasource.url", postgres::getJdbcUrl);
        resgistry.add("spring.datasource.username", postgres::getUsername);
        resgistry.add("spring.datasource.password", postgres::getPassword);
    }




    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.close();
    }

    @Test
    void vizualizarEmprestimo() throws Exception {
        //Given
        RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/emprestimos/vizualizaremprestimo?id-emprestimo=fa81f244-d11d-47ed-946d-e0165482cb1e");
        //Then
        MvcResult result = mockMvc.perform(request).andReturn();
        JSONObject object = new JSONObject(result.getResponse().getContentAsString());
        //Assert
        assertEquals(object.get("id-emprestimo"), "fa81f244-d11d-47ed-946d-e0165482cb1e");
    }

    @Test
    void cadastrarEmprestimo() throws Exception {
        //Given
        RequestBuilder request = MockMvcRequestBuilders.post("/api/v1/emprestimos/cadastraremprestimo")
            .content(new ObjectMapper().writeValueAsString(new EmprestimoDTORequest("12345678910", 12, 340, "00000000000")))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);
        //Then
        MvcResult result = mockMvc.perform(request).andReturn();
        //Assert
        UUID idResult = UUID.fromString(result.getResponse().getContentAsString());
        assertEquals(emprestimoService.getEmprestimo(idResult).getIdEmprestimo().toString(), idResult.toString());
    }

    @Test
    void alterarValor() throws Exception{
        //Given
        RequestBuilder request = MockMvcRequestBuilders.put("/api/v1/emprestimos/alterarvalor")
                .content(new ObjectMapper().writeValueAsString(new EmprestimoDTOUpdate(UUID.fromString("6787b214-ee87-4898-a650-d2b3ea19577e"), 500.00)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        //Then
        MvcResult result = mockMvc.perform(request).andReturn();
        //Assert
        Emprestimo emprestimo = emprestimoService.getEmprestimo(UUID.fromString("6787b214-ee87-4898-a650-d2b3ea19577e"));
        assertEquals(new BigDecimal("500.00") , emprestimo.getValorParcela());
    }

    @Test
    void alterarParcela() throws Exception{
        //Given
        RequestBuilder request = MockMvcRequestBuilders.put("/api/v1/emprestimos/alterarparcela")
                .content(new ObjectMapper().writeValueAsString(new EmprestimoDTOUpdate(UUID.fromString("47d69d68-668f-4757-b4f9-bb9f57172da3"), 24)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        //Then
        MvcResult result = mockMvc.perform(request).andReturn();
        //Assert
        Emprestimo emprestimo = emprestimoService.getEmprestimo(UUID.fromString("47d69d68-668f-4757-b4f9-bb9f57172da3"));
        assertEquals(24 , emprestimo.getQuantidadeParcelas());
    }

    @Test
    void finalizarEmprestimo() throws Exception{
        //Given
        RequestBuilder request = MockMvcRequestBuilders.put("/api/v1/emprestimos/finalizaremprestimo")
                .content(new ObjectMapper().writeValueAsString(new EmprestimoDTOTerminate(UUID.fromString("f931354c-6fc8-4147-8194-678d28bd8271"))))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        //Then
        MvcResult result = mockMvc.perform(request).andReturn();
        //Assert
        Emprestimo emprestimo = emprestimoService.getEmprestimo(UUID.fromString("f931354c-6fc8-4147-8194-678d28bd8271"));
        assertEquals(Status.Concluido , emprestimo.getStatus());
    }
}