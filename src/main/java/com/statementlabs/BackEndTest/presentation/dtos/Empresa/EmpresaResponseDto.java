package com.statementlabs.BackEndTest.presentation.dtos.Empresa;

import com.statementlabs.BackEndTest.domain.model.StatusEmpresa;
import com.statementlabs.BackEndTest.domain.model.TipoEmpresa;

import java.time.LocalDateTime;
import java.util.UUID;

public record EmpresaResponseDto(
        UUID id,
        String nome,
        String nif,
        TipoEmpresa tipo,
        StatusEmpresa status,
        LocalDateTime dataRegistro
) {}