package com.statementlabs.BackEndTest.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusEmpresa {
    ATIVA("A"),
    SUSPENSA("S"),
    BLOQUEADA("B");

    private final String codigo;
}
