package com.previdenciaprivada.emprestimos.dao;

import com.previdenciaprivada.emprestimos.services.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmprestimoRepository extends CrudRepository<Emprestimo, UUID> {
    Optional<List<Emprestimo>> findEmprestimoByCPFAndStatus(String CPF, Status status);
    Optional<List<Emprestimo>> findEmprestimoByCPFAndInstituicaoId(String CPF, long instituicaoId);
    Optional<List<Emprestimo>> findEmprestimoByInstituicaoId(long instituicaoId);
}
