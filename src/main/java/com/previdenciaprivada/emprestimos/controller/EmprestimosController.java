package com.previdenciaprivada.emprestimos.controller;

import com.previdenciaprivada.emprestimos.dto.EmprestimoDTORequest;
import com.previdenciaprivada.emprestimos.dto.EmprestimoDTOUpdate;
import com.previdenciaprivada.emprestimos.dto.EmprestimoDTOView;
import com.previdenciaprivada.emprestimos.dao.Emprestimo;
import com.previdenciaprivada.emprestimos.services.Auth;
import com.previdenciaprivada.emprestimos.services.EmprestimoService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/emprestimos")
public class EmprestimosController {

    private final EmprestimoService emprestimoService;
    private final Auth auth;

    public EmprestimosController(EmprestimoService emprestimoService, Auth auth) {
        this.emprestimoService = emprestimoService;
        this.auth = auth;
    }

    @PostMapping("cadastro")
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

    @GetMapping("{id-emprestimo}")
    @ResponseBody
    public ResponseEntity<EmprestimoDTOView> vizualizarEmprestimo(
            @PathVariable(value="id-emprestimo") String idEmprestimo,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String apiKey
            )
    {
        try {
            if(auth.autenticarOperacaoEmprestimo(apiKey, idEmprestimo)) {
                Emprestimo emprestimo = emprestimoService.getEmprestimo(UUID.fromString(idEmprestimo));
                EmprestimoDTOView emprestimoView = new EmprestimoDTOView(
                        emprestimo.getIdEmprestimo(),
                        emprestimo.getCPF(),
                        emprestimo.getValorParcela(),
                        emprestimo.getQuantidadeParcelas(),
                        emprestimo.getDataEmprestimo(),
                        emprestimo.getStatus()
                );
                return new ResponseEntity<>(emprestimoView, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        catch (NoSuchElementException err) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empréstimo não encontrado", err);
        }
    }

    @Deprecated
    @PutMapping("alterarvalor")
    public ResponseEntity<?>  alterarValor(@RequestBody EmprestimoDTOUpdate emprestimoUpdate) {

        try {
            emprestimoService.alterarValorParcela(emprestimoUpdate.idEmprestimo(), emprestimoUpdate.valor());
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch (Exception err) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empréstimo não encontrado", err);
        }
    }

    @Deprecated
    @PutMapping("alterarparcela")
    public ResponseEntity<HttpStatus> alterarParcela(@RequestBody EmprestimoDTOUpdate empretimoUpdate) {

        try {
            emprestimoService.alterarQtdParcelas(empretimoUpdate.idEmprestimo(), (int) empretimoUpdate.valor());
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch (NoSuchElementException err) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empréstimo não encontrado", err);
        }
    }

    @PutMapping("{id-emprestimo}/concluido")
    public ResponseEntity<HttpStatus>  finalizarEmprestimo(
            @PathVariable(value="id-emprestimo") String idEmprestimo,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String apiKey
        )
    {
        try {
            if(auth.autenticarOperacaoEmprestimo(apiKey, idEmprestimo)) {
                emprestimoService.finalizarEmprestimo(UUID.fromString(idEmprestimo));
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        catch (NoSuchElementException err) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empréstimo não encontrado", err);
        }
    }

    @GetMapping("{cpf}/lista")
    public  ResponseEntity<List<EmprestimoDTOView>> vizualizarTodosEmprestimos(
            @PathVariable(value="cpf") String  cpf,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String apiKey
    )
    {
        try {

            List<Emprestimo> emprestimos = emprestimoService.getEmprestimosPorCPF(cpf, apiKey);
            List<EmprestimoDTOView> emprestimosView = emprestimos.stream()
                    .map( (emprestimo) -> new EmprestimoDTOView(
                            emprestimo.getIdEmprestimo(),
                            emprestimo.getCPF(),
                            emprestimo.getValorParcela(),
                            emprestimo.getQuantidadeParcelas(),
                            emprestimo.getDataEmprestimo(),
                            emprestimo.getStatus()) ).collect(Collectors.toList());

            if (auth.autenticarOperacaoEmprestimo( apiKey, emprestimosView.get(0).idEmprestimo().toString() )) {
                return new ResponseEntity<>(emprestimosView, HttpStatus.OK);
            }
            return new ResponseEntity<>(emprestimosView, HttpStatus.FORBIDDEN);
        }
        catch (NoSuchElementException err) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}

