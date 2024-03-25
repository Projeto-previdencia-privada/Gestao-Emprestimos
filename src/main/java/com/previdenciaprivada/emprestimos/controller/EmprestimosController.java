package com.previdenciaprivada.emprestimos.controller;

import com.previdenciaprivada.emprestimos.dto.EmprestimoDTORequest;
import com.previdenciaprivada.emprestimos.dto.EmprestimoDTOTerminate;
import com.previdenciaprivada.emprestimos.dto.EmprestimoDTOUpdate;
import com.previdenciaprivada.emprestimos.models.Emprestimo;
import com.previdenciaprivada.emprestimos.services.EmprestimoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/emprestimos")
public class EmprestimosController {

    private final EmprestimoService emprestimoService;

    public EmprestimosController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @PostMapping("cadastraremprestimo")
    public ResponseEntity<String> cadastrarEmprestimo(@RequestBody EmprestimoDTORequest emprestimoInfo) {

        try {
            String idEmprestimo = emprestimoService.addEmprestimo(emprestimoInfo);
            if (!idEmprestimo.isEmpty()) {
                return new ResponseEntity<>(idEmprestimo, HttpStatus.CREATED);
            }
            return new ResponseEntity<>(idEmprestimo, HttpStatus.OK);
        }
        catch (NumberFormatException err) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de CPF inválido", err);
        }
    };

    @GetMapping("vizualizaremprestimo")
    @ResponseBody
    public ResponseEntity<Emprestimo> vizualizarEmprestimo(@RequestParam(name="id-emprestimo") UUID idEmprestimo) {

        try {
            Emprestimo emprestimo = emprestimoService.getEmprestimo(idEmprestimo);
            return new ResponseEntity<>(emprestimo, HttpStatus.OK);
        }
        catch (NoSuchElementException err) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empréstimo não encontrado", err);
        }
    }

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

    @PutMapping("finalizaremprestimo")
    public ResponseEntity<HttpStatus>  finalizarEmprestimo(@RequestBody EmprestimoDTOTerminate emprestimoUpdate) {

        try {
            emprestimoService.finalizarEmprestimo(emprestimoUpdate.idEmprestimo());
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch (NoSuchElementException err) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empréstimo não encontrado", err);
        }
    }
}

