package com.previdenciaprivada.emprestimos.controller;

import com.previdenciaprivada.emprestimos.dao.Instituicao;
import com.previdenciaprivada.emprestimos.dao.InstituicaoDAO;
import com.previdenciaprivada.emprestimos.dao.InstituicaoRepository;
import com.previdenciaprivada.emprestimos.dto.InstituicaoDTORequest;
import com.previdenciaprivada.emprestimos.services.Auth;
import com.previdenciaprivada.emprestimos.services.EmprestimoService;
import com.previdenciaprivada.emprestimos.services.InstituicaoService;
import com.previdenciaprivada.emprestimos.services.RegistroService;
import jakarta.transaction.Transactional;
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
    private final EmprestimoService emprestimoService;
    private final Auth auth;
    private final RegistroService registroService;
    private final InstituicaoRepository instituicaoRepository;

    public InstituicoesController(InstituicaoDAO instituicaoDAO, InstituicaoService instituicaoService, InstituicaoService instituicaoService1, EmprestimoService emprestimoService, Auth auth, RegistroService registroService, InstituicaoRepository instituicaoRepository) {
        this.instituicaoDAO = instituicaoDAO;
        this.instituicaoService = instituicaoService1;
        this.emprestimoService = emprestimoService;
        this.auth = auth;
        this.registroService = registroService;
        this.instituicaoRepository = instituicaoRepository;
    }


    @PostMapping("cadastro")
    public ResponseEntity<String> cadastrarInstituicao(@RequestBody InstituicaoDTORequest instituicaoInfo) {
        try {
            Instituicao instituicao = instituicaoDAO.addInstituicao(instituicaoInfo.nome(), instituicaoInfo.CNPJ());
            System.out.println(instituicao);
            return new ResponseEntity<>(instituicao.getChaveAPI(), HttpStatus.CREATED);

        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("");
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

    @PutMapping("/{cnpj}/chave")
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
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
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
               registroService.transferirDados(instituicaoService.ChaveParaId(apiKey), apiKey);
               return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        catch (NoSuchElementException err) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
