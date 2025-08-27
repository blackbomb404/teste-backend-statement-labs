package com.statementlabs.BackEndTest.application.repositories;

import com.statementlabs.BackEndTest.domain.model.SeloFiscal;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface SeloFiscalRepository extends JpaRepository<SeloFiscal, UUID> {
    Slice<SeloFiscal> findByEmpresaId(UUID id, Pageable pageable);

    @Query("""
        SELECT COUNT(s) > 0
        FROM SeloFiscal s
        WHERE s.empresa.id = :empresaId
        AND s.estado IN ('EMITIDO', 'PENDENTE')
        AND s.dataEmissao < :dataLimite
        """)
    boolean existemSelosNaoValidadosApos30Dias(
            @Param("empresaId") UUID empresaId,
            @Param("dataLimite") LocalDateTime dataLimite
    );
}
