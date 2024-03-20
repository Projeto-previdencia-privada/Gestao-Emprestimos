package com.previdenciaprivada.emprestimos.controller;

import com.previdenciaprivada.emprestimos.dto.EmprestimoDTORequest;
import com.previdenciaprivada.emprestimos.models.Emprestimo;
import com.previdenciaprivada.emprestimos.services.EmprestimoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/emprestimos")
public class EmprestimosController {

    private final EmprestimoService emprestimoService;

    public EmprestimosController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @PostMapping("cadastraremprestimo")
    public ResponseEntity<String> cadastrarEmprestimo(@RequestBody EmprestimoDTORequest emprestimoInfo) {
        String idEmprestimo = emprestimoService.addEmprestimo(emprestimoInfo);
        return new ResponseEntity<>(idEmprestimo, HttpStatus.CREATED);
    };
    /*
    @PutMapping("finalizaremprestimo")
    public ResponseEntity<String> finalizarEmprestimo() {

    }
    @GetMapping("vizualizaremprestimo")
    public ResponseEntity<String> vizualizarEmprestimo() {
    }

    @PutMapping("alterarvalor")
    public ResponseEntity<String>  alterarValor() {
    }

    @PutMapping("alterarparcela")
    public ResponseEntity<String> alterarParcela() {
    }
 */
    @GetMapping("vizualizaremprestimo/{cpf}")
    public List<Emprestimo> vizualizarEmprestimo(@PathVariable("cpf") String cpf) {
        return emprestimoService.getEmprestimo(cpf);
    }
}

