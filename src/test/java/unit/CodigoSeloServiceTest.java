package unit;

// ============================================================================
// TESTES PARA GERAÇÃO DE CÓDIGO SEQUENCIAL
// ============================================================================

import com.statementlabs.BackEndTest.application.repositories.SeloFiscalRepository;
import com.statementlabs.BackEndTest.application.services.impl.CodigoSeloServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CodigoSeloServiceTest {

    @Mock
    private SeloFiscalRepository seloFiscalRepository;

    @InjectMocks
    private CodigoSeloServiceImpl codigoSeloService;

    private static int anoActual;

    @BeforeAll
    public static void setUp() {
        anoActual = LocalDateTime.now().getYear();
    }

    @Test
    void deveGerarCodigoComSequenciaCorreta() {
        // Arrange

        // Act
        String codigo1 = codigoSeloService.gerarCodigo();
        String codigo2 = codigoSeloService.gerarCodigo();

        // Assert
        assertEquals(String.format("PROSEFA-%d-000001", anoActual), codigo1);
        assertEquals(String.format("PROSEFA-%d-000002", anoActual), codigo2);
    }

    @Test
    void deveReiniciarSequenciaParaNovoAno() {
        // Arrange

        // Act

        // Assert
    }
}