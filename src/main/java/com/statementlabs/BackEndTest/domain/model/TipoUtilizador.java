package com.statementlabs.BackEndTest.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TipoUtilizador {
    ADMIN("A"),
    OPERADOR("O");

    private final String codigo;
}
