package com.previdenciaprivada.emprestimos.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.List;

public interface InstituicaoRepository extends CrudRepository<Instituicao, Long> {
    Optional<Instituicao> findByChaveAPI(String apiKey);
    Optional<Instituicao> findByCNPJ(String cnpj);
    @Modifying
    @Query(value = "delete from Instituicao i where i.CNPJ = ?1")
    void deleteInstituicaoByCNPJ(String cnpj);

    @Override
    List<Instituicao> findAll();
}
