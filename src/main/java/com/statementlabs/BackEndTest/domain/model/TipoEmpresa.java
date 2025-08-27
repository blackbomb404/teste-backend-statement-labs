package com.statementlabs.BackEndTest.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TipoEmpresa {
    FABRICANTE("F"),
    IMPORTADOR("I");

    private final String codigo;
}
