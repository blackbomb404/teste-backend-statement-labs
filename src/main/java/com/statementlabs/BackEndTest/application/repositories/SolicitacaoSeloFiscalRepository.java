package com.statementlabs.BackEndTest.application.repositories;

import com.statementlabs.BackEndTest.domain.model.SolicitacaoSeloFiscal;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface SolicitacaoSeloFiscalRepository extends JpaRepository<SolicitacaoSeloFiscal, UUID> {
    Slice<SolicitacaoSeloFiscal> findByEmpresaId(UUID id, Pageable pageable);
}
