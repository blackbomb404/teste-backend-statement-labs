package unit;

// ============================================================================
// TESTES PARA LOG DE AUDITORIA SERVICE
// ============================================================================

import com.statementlabs.BackEndTest.application.repositories.LogAuditoriaRepository;
import com.statementlabs.BackEndTest.application.services.impl.LogAuditoriaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogAuditoriaServiceTest {

    @Mock
    private LogAuditoriaRepository logRepository;

    @InjectMocks
    private LogAuditoriaServiceImpl logAuditoriaService;

    @Test
    void deveRegistrarLogComTexto() {
        // Arrange
        String entidade = "SeloFiscal";
        String acao = "SELO_VALIDADO";
        String usuario = "admin";
        Map<String, String> detalhes = new HashMap<>();

        // Act
        logAuditoriaService.registrar(entidade, acao, usuario, detalhes);

        // Assert
        verify(logRepository).save(argThat(log ->
                log.getEntidade().equals(entidade) &&
                        log.getAccao().equals(acao) &&
                        log.getUsuario().equals(usuario) &&
                        log.getDetalhes().equals(detalhes) &&
                        log.getDataHora() != null
        ));
    }
}
