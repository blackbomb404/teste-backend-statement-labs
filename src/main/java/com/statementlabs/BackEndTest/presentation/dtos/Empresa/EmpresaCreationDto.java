package com.statementlabs.BackEndTest.presentation.dtos.Empresa;

import com.statementlabs.BackEndTest.domain.model.TipoEmpresa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EmpresaCreationDto(
        @NotBlank(message = "O nome é obrigatório")
        String nome,
        @NotBlank(message = "O NIF é obrigatório")
        String nif,
        @NotNull(message = "O tipo de empresa é obrigatório")
        TipoEmpresa tipo
) {}