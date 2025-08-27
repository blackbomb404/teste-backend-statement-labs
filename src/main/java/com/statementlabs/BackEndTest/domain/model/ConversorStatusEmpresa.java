package com.statementlabs.BackEndTest.domain.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class ConversorStatusEmpresa implements AttributeConverter<StatusEmpresa, String> {

    @Override
    public String convertToDatabaseColumn(StatusEmpresa statusEmpresa) {
        if (statusEmpresa == null) {
            return null;
        }
        return statusEmpresa.getCodigo();
    }

    @Override
    public StatusEmpresa convertToEntityAttribute(String codigo) {
        if (codigo == null) {
            return null;
        }
        return Stream.of(StatusEmpresa.values())
                .filter(value -> value.getCodigo().equals(codigo))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
