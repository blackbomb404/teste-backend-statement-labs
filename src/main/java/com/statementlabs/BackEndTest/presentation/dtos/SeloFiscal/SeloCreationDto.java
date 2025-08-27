package com.statementlabs.BackEndTest.presentation.dtos.SeloFiscal;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SeloCreationDto(
        @NotNull(message = "O ID da solicitação de selo fiscal é obrigatório")
        UUID idSolicitacao
) {}
