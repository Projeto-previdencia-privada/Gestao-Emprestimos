package com.previdenciaprivada.emprestimos.controller;

import com.previdenciaprivada.emprestimos.dao.Instituicao;
import com.previdenciaprivada.emprestimos.dao.InstituicaoDAO;
import com.previdenciaprivada.emprestimos.dto.InstituicaoDTORequest;
import com.previdenciaprivada.emprestimos.services.Auth;
import com.previdenciaprivada.emprestimos.services.InstituicaoService;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;

@RestController
@RequestMapping("api/v1/instituicoes")
@CrossOrigin(origins = "*")
public class InstituicoesController {
    private final InstituicaoDAO instituicaoDAO;
    private final InstituicaoService instituicaoService;
    private final Auth auth;

    public InstituicoesController(InstituicaoDAO instituicaoDAO, InstituicaoService instituicaoService, Auth auth) {
        this.instituicaoDAO = instituicaoDAO;
        this.instituicaoService = instituicaoService;
        this.auth = auth;
    }

    public final Dotenv dotenv = Dotenv.load();


    @PostMapping()
    public ResponseEntity<Map<String, String>> cadastrarInstituicao(@RequestBody InstituicaoDTORequest instituicaoInfo) {
        try {
            String chave = instituicaoDAO.addInstituicao(instituicaoInfo.nome(), instituicaoInfo.CNPJ());
            return new ResponseEntity<>(Collections.singletonMap("chave-api",chave), HttpStatus.CREATED);

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

    @GetMapping()
    public ResponseEntity<List<InstituicaoDTORequest>> vizualizarTodasInstituicoes() {
        return new ResponseEntity<>(instituicaoService.getAllInstituicooes(), HttpStatus.OK);
    }

    @GetMapping(value = {"{cnpj}/imagem"}, produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getImagemInstituicao(@PathVariable String cnpj) throws IOException {
        try {
            FileInputStream fileInputStream = new FileInputStream("assets/" + cnpj + ".jpg");
            return new ResponseEntity<>(fileInputStream.readAllBytes(), HttpStatus.OK);
        }
        catch (FileNotFoundException err) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("{cnpj}/imagem")
    public ResponseEntity<HttpStatus> cadastrarImagemInstituicao(@PathVariable String cnpj, @RequestBody String imageURI) throws IOException {
        byte[] data = Base64.getDecoder().decode(imageURI.split(",")[1].replace("\n", ""));
        FileOutputStream fileOutputStream = new FileOutputStream("assets/"+cnpj+".jpg", false);
        fileOutputStream.write(data);
        fileOutputStream.close();

        try {
            Instituicao instituicao = instituicaoService.getInstituicao(cnpj);
            instituicao.setImage_path(cnpj+".jpg");
            instituicaoService.atualizarInstituicao(instituicao);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (NoSuchElementException err) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
