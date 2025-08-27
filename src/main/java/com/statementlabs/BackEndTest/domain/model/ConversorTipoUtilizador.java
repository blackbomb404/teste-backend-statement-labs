package com.statementlabs.BackEndTest.domain.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class ConversorTipoUtilizador implements AttributeConverter<TipoUtilizador, String> {

    @Override
    public String convertToDatabaseColumn(TipoUtilizador tipoUtilizador) {
        if (tipoUtilizador == null) {
            return null;
        }
        return tipoUtilizador.getCodigo();
    }

    @Override
    public TipoUtilizador convertToEntityAttribute(String codigo) {
        if (codigo == null) {
            return null;
        }
        return Stream.of(TipoUtilizador.values())
                .filter(value -> value.getCodigo().equals(codigo))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
