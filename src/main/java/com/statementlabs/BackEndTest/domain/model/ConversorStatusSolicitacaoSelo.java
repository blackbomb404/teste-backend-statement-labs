package com.statementlabs.BackEndTest.domain.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class ConversorStatusSolicitacaoSelo implements AttributeConverter<StatusSolicitacaoSelo, String> {

    @Override
    public String convertToDatabaseColumn(StatusSolicitacaoSelo statusSolicitacaoSelo) {
        if (statusSolicitacaoSelo == null) {
            return null;
        }
        return statusSolicitacaoSelo.getCodigo();
    }

    @Override
    public StatusSolicitacaoSelo convertToEntityAttribute(String codigo) {
        if (codigo == null) {
            return null;
        }
        return Stream.of(StatusSolicitacaoSelo.values())
                .filter(value -> value.getCodigo().equals(codigo))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
