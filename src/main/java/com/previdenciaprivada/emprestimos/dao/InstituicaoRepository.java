package com.previdenciaprivada.emprestimos.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface InstituicaoRepository extends CrudRepository<Instituicao, Long> {
    Optional<Instituicao> findByChaveAPI(String apiKey);
    Optional<Instituicao> findByCNPJ(String cnpj);
    @Modifying
    @Transactional
    @Query(value = "delete from Instituicao i where i.CNPJ = ?1")
    void deleteInstituicaoByCNPJ(String cnpj);
}
