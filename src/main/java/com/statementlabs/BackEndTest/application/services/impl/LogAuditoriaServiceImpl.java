package com.statementlabs.BackEndTest.application.services.impl;

import com.statementlabs.BackEndTest.application.repositories.LogAuditoriaRepository;
import com.statementlabs.BackEndTest.application.services.LogAuditoriaService;
import com.statementlabs.BackEndTest.domain.model.LogAuditoria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LogAuditoriaServiceImpl implements LogAuditoriaService {
    private final LogAuditoriaRepository logAuditoriaRepository;

    @Transactional
    public void registrar(String entidade, String accao, String usuario, Map<String, String> detalhes) {
        var log = new LogAuditoria();
        log.setEntidade(entidade);
        log.setAccao(accao);
        log.setUsuario(usuario);
        log.setDetalhes(detalhes);
        log.setDataHora(LocalDateTime.now());

        logAuditoriaRepository.save(log);
    }

    @Override
    public Slice<LogAuditoria> listarLogs(short pageNumber, short pageCapacity) {
        return logAuditoriaRepository.findAll(PageRequest.of(pageNumber, pageCapacity));
    }

    /*public void registrar2(String entidade, String acao, String usuario, Object detalhesObj) {
        try {
            var mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String detalhesJson = mapper.writeValueAsString(detalhesObj);

            registrar(entidade, acao, usuario, detalhesJson);
        } catch (Exception e) {
            // Fallback: registrar sem detalhes JSON
            registrar(entidade, acao, usuario, "Erro ao serializar detalhes: " + e.getMessage());
        }
    }*/
}
