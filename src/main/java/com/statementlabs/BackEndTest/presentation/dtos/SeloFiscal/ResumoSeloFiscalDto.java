package com.statementlabs.BackEndTest.presentation.dtos.SeloFiscal;

import com.statementlabs.BackEndTest.domain.model.EstadoSeloFiscal;

import java.time.LocalDateTime;
import java.util.UUID;

public record ResumoSeloFiscalDto(
        UUID id,
        String codigo,
        String produto,
        LocalDateTime dataEmissao,
        EstadoSeloFiscal estado
) {}
