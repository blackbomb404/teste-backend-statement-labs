package com.statementlabs.BackEndTest.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EstadoSeloFiscal {
    PENDENTE("P"),
    EMITIDO("E"),
    VALIDADO("V"),
    INVALIDADO("I");

    private final String codigo;
}
