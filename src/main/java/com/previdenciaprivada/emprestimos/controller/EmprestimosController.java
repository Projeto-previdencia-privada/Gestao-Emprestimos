package com.previdenciaprivada.emprestimos.controller;

import com.previdenciaprivada.emprestimos.dto.EmprestimoDTORequest;
import com.previdenciaprivada.emprestimos.dto.EmprestimoDTOView;
import com.previdenciaprivada.emprestimos.services.Auth;
import com.previdenciaprivada.emprestimos.services.EmprestimoService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("api/v1/emprestimos")
public class EmprestimosController {

    private final EmprestimoService emprestimoService;
    private final Auth auth;

    public EmprestimosController(EmprestimoService emprestimoService, Auth auth) {
        this.emprestimoService = emprestimoService;
        this.auth = auth;
    }

    @PostMapping("")
    public ResponseEntity<Map<String, String>> cadastrarEmprestimo(
            @RequestBody EmprestimoDTORequest emprestimoInfo,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String apiKey
            )
    {
        try {
            long idInstituicao = auth.validarCadastro(apiKey);

            String idEmprestimo = emprestimoService.addEmprestimo(emprestimoInfo, idInstituicao);
            if (!idEmprestimo.isEmpty()) {
                return new ResponseEntity<>(Collections.singletonMap("id-emprestimo", idEmprestimo), HttpStatus.CREATED);
            }
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        catch (NumberFormatException err) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de CPF inválido", err);
        }
        catch (NoSuchElementException err) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    };

    @PatchMapping("{id-emprestimo}")
    public ResponseEntity<HttpStatus>  finalizarEmprestimo(
            @PathVariable(value="id-emprestimo") String idEmprestimo,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String apiKey
        )
    {
        try {
            if(auth.autenticarOperacaoEmprestimo(apiKey, idEmprestimo)) {
                emprestimoService.finalizarEmprestimo(UUID.fromString(idEmprestimo));
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        catch (NoSuchElementException err) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empréstimo não encontrado", err);
        }
    }

    @GetMapping("{cpf}")
    public ResponseEntity<List<EmprestimoDTOView>> vizualizarEmprestimos(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String apiKey,
            @PathVariable(value="cpf") String cpf,
            @RequestParam(name="modo") String modo
    ) {
            if(modo.equals("pessoa")) {
                // TODO: AUTHENTICATION
                try {
                    List<EmprestimoDTOView> emprestimosView = emprestimoService.emprestimoToEmprestimoDTO(emprestimoService.getEmprestimosPorCPF(cpf, apiKey));
                    return new ResponseEntity<>(emprestimosView, HttpStatus.OK);
                }
                catch (NoSuchElementException err) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                }
            }


            if(modo.equals("instituicao")) {
                try {
                    List<EmprestimoDTOView> emprestimosView = emprestimoService.emprestimoToEmprestimoDTO(emprestimoService.getEmprestimosPorCPF(cpf, apiKey));
                    if (auth.autenticarOperacaoEmprestimo( apiKey, emprestimosView.get(0).idEmprestimo().toString() )) {
                        return new ResponseEntity<>(emprestimosView, HttpStatus.OK);
                    }
                    return new ResponseEntity<>(emprestimosView, HttpStatus.UNAUTHORIZED);
                }
                catch (NoSuchElementException err) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}

