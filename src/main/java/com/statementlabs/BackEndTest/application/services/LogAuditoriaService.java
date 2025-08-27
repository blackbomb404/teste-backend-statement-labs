package com.statementlabs.BackEndTest.application.services;

import com.statementlabs.BackEndTest.domain.model.LogAuditoria;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Map;

public interface LogAuditoriaService {
    void registrar(String entidade, String accao, String usuario, Map<String, String> detalhes);
    Slice<LogAuditoria> listarLogs(short pageNumber, short pageCapacity);
}
