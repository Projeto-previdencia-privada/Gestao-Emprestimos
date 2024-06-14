package com.previdenciaprivada.emprestimos.controller;

import com.previdenciaprivada.emprestimos.dao.Emprestimo;
import com.previdenciaprivada.emprestimos.dto.ClientInfo;
import com.previdenciaprivada.emprestimos.dto.EmprestimoDTORequest;
import com.previdenciaprivada.emprestimos.dto.EmprestimoDTOView;
import com.previdenciaprivada.emprestimos.services.Auth;
import com.previdenciaprivada.emprestimos.services.EmprestimoService;
import com.previdenciaprivada.emprestimos.services.InstituicaoService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("api/v1/emprestimos")
@CrossOrigin(origins = "*")
public class EmprestimosController {

    private final EmprestimoService emprestimoService;
    private final Auth auth;
    private final InstituicaoService instituicaoService;

    public EmprestimosController(EmprestimoService emprestimoService, Auth auth, InstituicaoService instituicaoService) {
        this.emprestimoService = emprestimoService;
        this.auth = auth;
        this.instituicaoService = instituicaoService;
    }

    @PostMapping("")
    public ResponseEntity<Map<String, String>> cadastrarEmprestimo(
            @RequestBody EmprestimoDTORequest emprestimoInfo
//            @RequestHeader(HttpHeaders.AUTHORIZATION) String apiKey
            )
    {
//        if (auth.validarCadastro(emprestimoInfo.cnpj(), apiKey)) {
            try {
                String idEmprestimo = emprestimoService.addEmprestimo(emprestimoInfo, instituicaoService.getInstituicao(emprestimoInfo.cnpj()));
                if (!idEmprestimo.isEmpty()) {
                    return new ResponseEntity<>(Collections.singletonMap("id-emprestimo", idEmprestimo), HttpStatus.CREATED);
                }
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            catch (NumberFormatException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
//        }
//        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PatchMapping("{id-emprestimo}")
    public ResponseEntity<HttpStatus>  finalizarEmprestimo(
            @PathVariable(value="id-emprestimo") String idEmprestimo,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String apiKey
        )
    {
        if(auth.autenticarOperacaoEmprestimo(apiKey, idEmprestimo)) {
            try {
                emprestimoService.finalizarEmprestimo(UUID.fromString(idEmprestimo));
                return new ResponseEntity<>(HttpStatus.OK);
            }
            catch (NoSuchElementException err) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empréstimo não encontrado", err);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("{cpf}")
    public ResponseEntity<List<EmprestimoDTOView>> vizualizarEmprestimos(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String apiKey,
            @PathVariable(value="cpf") String cpf,
            @RequestParam(name="modo") String modo,
            @RequestParam(name="cnpj", required= false, defaultValue="0") String cnpj,
            HttpServletResponse response
    ) {
            if(modo.equals("pessoa")) {
                // TODO: AUTHENTICATION
                try {
                    List<EmprestimoDTOView> emprestimosView = emprestimoService.emprestimoToEmprestimoDTO(emprestimoService.getEmprestimosPorCPF(cpf));
                    response.addCookie(new Cookie("Teste", "Valor"));
                    return new ResponseEntity<>(emprestimosView, HttpStatus.OK);
                }
                catch (NoSuchElementException err) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND,"CPF não encontrado", err);
                }
            }

            if(modo.equals("instituicao")) {
                if(auth.autenticarOperacaoInstituicao(apiKey, cnpj)) {
                    try {
                        List<Emprestimo> emprestimosInstituicao = emprestimoService.getEmprestimos(cpf).stream()
                                .filter( (emprestimo) -> emprestimo.getInstituicao().getCNPJ().equals(cnpj) ).toList();
                        List<EmprestimoDTOView> emprestimosView = emprestimoService.emprestimoToEmprestimoDTO(emprestimosInstituicao);
                        return new ResponseEntity<>(emprestimosView, HttpStatus.OK);
                    } catch (NoSuchElementException err) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CPF não encontrado", err);
                    }
                }
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("teste")
    public ResponseEntity<String> metodoTeste(@CookieValue(value="Teste", required=false) String cookie, HttpServletResponse response) {;
        response.addCookie(new Cookie("Teste", "ADWKOADKAOWDKAODKAOWKDWOK"));
        return new ResponseEntity<>(cookie, HttpStatus.OK);
    }

    @GetMapping("teste/renda")
    public ResponseEntity<BigDecimal> credito() {
        return new ResponseEntity<>(BigDecimal.valueOf(15000), HttpStatus.OK);
    }

    @GetMapping("{cpf}/credito")
    public ResponseEntity<BigDecimal> totalCredito(@PathVariable String cpf, @RequestParam String modo) {

        if(modo.equals("total")) {
            return new ResponseEntity<>(emprestimoService.getTotalCredito(cpf), HttpStatus.OK);
        }

        if(modo.equals("disponivel")) {
            return new ResponseEntity<>(emprestimoService.getCreditoDisponivel(cpf), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("{cpf}/info") public ResponseEntity<ClientInfo> getClientInfo(@PathVariable String cpf) {
        ClientInfo clientInfo = new ClientInfo(
                emprestimoService.getTotalCredito(cpf),
                emprestimoService.getRendaTotal(cpf),
                emprestimoService.getCreditoDisponivel(cpf)
        );

        return !clientInfo.creditoTotal().equals(0) ? new ResponseEntity<>(clientInfo, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

