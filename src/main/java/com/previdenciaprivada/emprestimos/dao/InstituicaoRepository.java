package com.previdenciaprivada.emprestimos.dao;

import org.hibernate.mapping.List;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface InstituicaoRepository extends CrudRepository<Instituicao, Long> {
    Optional<Instituicao> findByChaveAPI(String apiKey);
    Optional<Instituicao> findByCNPJ(String cnpj);
    Long deleteInstituicaoByCNPJ(String cnpj);
}
