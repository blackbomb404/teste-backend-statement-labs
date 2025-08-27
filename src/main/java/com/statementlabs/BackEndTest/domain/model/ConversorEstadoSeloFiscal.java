package com.statementlabs.BackEndTest.domain.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class ConversorEstadoSeloFiscal implements AttributeConverter<EstadoSeloFiscal, String> {

    @Override
    public String convertToDatabaseColumn(EstadoSeloFiscal estadoSeloFiscal) {
        if (estadoSeloFiscal == null) {
            return null;
        }
        return estadoSeloFiscal.getCodigo();
    }

    @Override
    public EstadoSeloFiscal convertToEntityAttribute(String codigo) {
        if (codigo == null) {
            return null;
        }
        return Stream.of(EstadoSeloFiscal.values())
                .filter(value -> value.getCodigo().equals(codigo))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
