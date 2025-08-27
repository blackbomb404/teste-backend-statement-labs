package com.statementlabs.BackEndTest.presentation.dtos.SeloFiscal;

import com.statementlabs.BackEndTest.domain.model.EstadoSeloFiscal;

import java.time.LocalDateTime;
import java.util.UUID;

public record SeloFiscalResponseDto(
        UUID id,
        String codigo,
        UUID empresaId,
        String nomeEmpresa,
        String produto,
        LocalDateTime dataEmissao,
        EstadoSeloFiscal estado
) {}
