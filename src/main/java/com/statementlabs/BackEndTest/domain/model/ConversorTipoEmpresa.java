package com.statementlabs.BackEndTest.domain.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class ConversorTipoEmpresa implements AttributeConverter<TipoEmpresa, String> {

    @Override
    public String convertToDatabaseColumn(TipoEmpresa tipoEmpresa) {
        if (tipoEmpresa == null) {
            return null;
        }
        return tipoEmpresa.getCodigo();
    }

    @Override
    public TipoEmpresa convertToEntityAttribute(String codigo) {
        if (codigo == null) {
            return null;
        }
        return Stream.of(TipoEmpresa.values())
                .filter(value -> value.getCodigo().equals(codigo))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
