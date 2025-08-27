package com.statementlabs.BackEndTest.application.repositories;

import com.statementlabs.BackEndTest.domain.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmpresaRepository extends JpaRepository<Empresa, UUID> {
    boolean existsByNif(String nif);
    Optional<Empresa> findByNif(String nif);
}
