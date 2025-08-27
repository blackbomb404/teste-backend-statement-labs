package com.statementlabs.BackEndTest.presentation.dtos.SolicitacaoSeloFiscal;

import com.statementlabs.BackEndTest.domain.model.Empresa;
import com.statementlabs.BackEndTest.domain.model.StatusSolicitacaoSelo;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record SolicitacaoSeloFiscalRequestDto(
        @NotNull(message = "O ID da empresa é obrigatório")
        UUID idEmpresa,
        @NotBlank(message = "O produto é obrigatório")
        String produto
) {}