package com.previdenciaprivada.emprestimos.controller;

import com.previdenciaprivada.emprestimos.dto.EmprestimoDTORequest;
import com.previdenciaprivada.emprestimos.dto.EmprestimoDTOTerminate;
import com.previdenciaprivada.emprestimos.dto.EmprestimoDTOUpdate;
import com.previdenciaprivada.emprestimos.models.Emprestimo;
import com.previdenciaprivada.emprestimos.services.EmprestimoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        String idEmprestimo = emprestimoService.addEmprestimo(emprestimoInfo);
        return new ResponseEntity<>(idEmprestimo, HttpStatus.CREATED);
    };

    @GetMapping("vizualizaremprestimo")
    @ResponseBody
    public Emprestimo vizualizarEmprestimo(@RequestParam(name="id-emprestimo") UUID idEmprestimo) {
        return emprestimoService.getEmprestimo(idEmprestimo);
    }

    @PutMapping("alterarvalor")
    public ResponseEntity<HttpStatus>  alterarValor(@RequestBody EmprestimoDTOUpdate emprestimoUpdate) {
        emprestimoService.alterarValorParcela(emprestimoUpdate.idEmprestimo(), emprestimoUpdate.valor());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("alterarparcela")
    public ResponseEntity<HttpStatus> alterarParcela(@RequestBody EmprestimoDTOUpdate empretimoUpdate) {
        emprestimoService.alterarQtdParcelas(empretimoUpdate.idEmprestimo(), (int) empretimoUpdate.valor());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("finalizaremprestimo")
    public ResponseEntity<HttpStatus>  finalizarEmprestimo(@RequestBody EmprestimoDTOTerminate emprestimoUpdate) {
        emprestimoService.finalizarEmprestimo(emprestimoUpdate.idEmprestimo());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

