package com.previdenciaprivada.emprestimos.controller;

import com.previdenciaprivada.emprestimos.dao.Instituicao;
import com.previdenciaprivada.emprestimos.dao.InstituicaoDAO;
import com.previdenciaprivada.emprestimos.dto.InstituicaoDTORequest;
import com.previdenciaprivada.emprestimos.services.Auth;
import com.previdenciaprivada.emprestimos.services.InstituicaoService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/v1/instituicoes")
public class InstituicoesController {

    private final InstituicaoDAO instituicaoDAO;
    private final InstituicaoService instituicaoService;
    private final Auth auth;

    public InstituicoesController(InstituicaoDAO instituicaoDAO, InstituicaoService instituicaoService, Auth auth) {
        this.instituicaoDAO = instituicaoDAO;
        this.instituicaoService = instituicaoService;
        this.auth = auth;
    }


    @PostMapping()
    public ResponseEntity<Map<String, String>> cadastrarInstituicao(@RequestBody InstituicaoDTORequest instituicaoInfo) {
        try {
            Instituicao instituicao = instituicaoDAO.addInstituicao(instituicaoInfo.nome(), instituicaoInfo.CNPJ());
            System.out.println(instituicao);
            return new ResponseEntity<>(Collections.singletonMap("chave-api",instituicao.getChaveAPI()), HttpStatus.CREATED);

        }
        catch (DataIntegrityViolationException err) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("{cnpj}")
    public ResponseEntity<InstituicaoDTORequest> vizualizarInstituicao(
            @PathVariable("cnpj") String cnpj
    )
    {
        try {
            Instituicao instituicao = instituicaoDAO.getInstituicaoPorCNPJ(cnpj).orElseThrow();
            InstituicaoDTORequest instituicaoView = new InstituicaoDTORequest(
                    instituicao.getNome(),
                    instituicao.getCNPJ()
            );

            return new ResponseEntity<>(instituicaoView, HttpStatus.OK);
        }
        catch (NoSuchElementException err) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("{cnpj}")
    public ResponseEntity<Map<String,String>> gerarNovaChave(
            @PathVariable("cnpj") String cnpj,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String apiKey
    )
    {
        try {
            String novaChave = Auth.gerarChave();
            instituicaoService.atualizarChave(apiKey, novaChave);
            return ResponseEntity.ok(Collections.singletonMap("chave-api", novaChave));
        }
        catch (NoSuchElementException err) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("{cnpj}")
    public ResponseEntity<HttpStatus> deletetarInstituicao(
            @PathVariable("cnpj") String cnpj,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String apiKey
    )
    {
        try {
            if (auth.autenticarOperacaoInstituicao(apiKey, cnpj)) {
               instituicaoService.deletarInstituicao(apiKey, cnpj);
               return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        catch (NoSuchElementException err) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
