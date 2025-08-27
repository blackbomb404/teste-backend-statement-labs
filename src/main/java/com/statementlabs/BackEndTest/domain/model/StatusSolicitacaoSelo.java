package com.statementlabs.BackEndTest.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusSolicitacaoSelo {
    PENDENTE("P"),
    APROVADA("A"),
    REJEITADA("R");

    private final String codigo;
}
