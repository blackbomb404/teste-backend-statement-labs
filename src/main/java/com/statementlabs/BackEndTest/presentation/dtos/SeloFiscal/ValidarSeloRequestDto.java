package com.statementlabs.BackEndTest.presentation.dtos.SeloFiscal;

import jakarta.validation.constraints.NotBlank;

public record ValidarSeloRequestDto(
        @NotBlank(message = "O código do selo é obrigatório")
        String codigo
) {}