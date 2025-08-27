package unit;

// ============================================================================
// TESTES PARA SOLICITACAO SELO SERVICE
// ============================================================================

import com.statementlabs.BackEndTest.application.repositories.EmpresaRepository;
import com.statementlabs.BackEndTest.application.repositories.SolicitacaoSeloFiscalRepository;
import com.statementlabs.BackEndTest.application.services.LogAuditoriaService;
import com.statementlabs.BackEndTest.application.services.impl.SolicitacaoSeloFiscalServiceImpl;
import com.statementlabs.BackEndTest.domain.model.*;
import com.statementlabs.BackEndTest.presentation.dtos.SolicitacaoSeloFiscal.SolicitacaoSeloFiscalRequestDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitacaoSeloFiscalServiceTest {

    @Mock
    private SolicitacaoSeloFiscalRepository solicitacaoRepository;

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private LogAuditoriaService auditoriaService;

    @InjectMocks
    private SolicitacaoSeloFiscalServiceImpl solicitacaoSeloService;

    private UUID empresaId;
    private Empresa empresaAtiva;
    private Empresa empresaSuspensa;
    private SolicitacaoSeloFiscalRequestDto solicitacaoRequest;
    private SolicitacaoSeloFiscal solicitacao;

    @BeforeEach
    void setUp() {
        empresaId = UUID.randomUUID();

        empresaAtiva = new Empresa();
        empresaAtiva.setId(empresaId);
        empresaAtiva.setNome("Empresa Ativa");
        empresaAtiva.setNif("123456789");
        empresaAtiva.setStatus(StatusEmpresa.ATIVA);
        empresaAtiva.setTipo(TipoEmpresa.FABRICANTE);

        empresaSuspensa = new Empresa();
        empresaSuspensa.setId(empresaId);
        empresaSuspensa.setNome("Empresa Suspensa");
        empresaSuspensa.setStatus(StatusEmpresa.SUSPENSA);

        solicitacaoRequest = new SolicitacaoSeloFiscalRequestDto(empresaId, "Whisky Premium");

        solicitacao = new SolicitacaoSeloFiscal();
        solicitacao.setId(UUID.randomUUID());
        solicitacao.setEmpresa(empresaAtiva);
        solicitacao.setProduto("Whisky Premium");
        solicitacao.setStatus(StatusSolicitacaoSelo.PENDENTE);
        solicitacao.setDataSolicitacao(LocalDateTime.now());
    }

    @Test
    void deveCriarSolicitacaoParaEmpresaAtiva() {
        // Arrange
        when(empresaRepository.findById(empresaId)).thenReturn(Optional.of(empresaAtiva));
        when(solicitacaoRepository.save(any(SolicitacaoSeloFiscal.class))).thenReturn(solicitacao);

        // Act
        var result = solicitacaoSeloService.solicitarSelo(solicitacaoRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Whisky Premium", result.produto());
        assertEquals(StatusSolicitacaoSelo.PENDENTE, result.status());

        verify(solicitacaoRepository).save(any(SolicitacaoSeloFiscal.class));
        verify(auditoriaService).registrar(eq("SolicitacaoSeloFiscal"), eq("SOLICITACAO_CRIADA"), anyString(), any());
    }

    @Test
    void naoDevePermitirSolicitacaoParaEmpresaSuspensa() {
        // Arrange
        when(empresaRepository.findById(empresaId)).thenReturn(Optional.of(empresaSuspensa));

        // Act & Assert
        var exception = assertThrows(IllegalStateException.class,
                () -> solicitacaoSeloService.solicitarSelo(solicitacaoRequest));

        verify(solicitacaoRepository, never()).save(any(SolicitacaoSeloFiscal.class));
    }

    @Test
    void naoDevePermitirSolicitacaoParaEmpresaBloqueada() {
        // Arrange
        empresaAtiva.setStatus(StatusEmpresa.BLOQUEADA);
        when(empresaRepository.findById(empresaId)).thenReturn(Optional.of(empresaAtiva));

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> solicitacaoSeloService.solicitarSelo(solicitacaoRequest));
    }

    @Test
    void deveLancarExcecaoParaEmpresaInexistente() {
        // Arrange
        when(empresaRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Act & Assert
        var exception = assertThrows(EntityNotFoundException.class,
                () -> solicitacaoSeloService.solicitarSelo(solicitacaoRequest));

        assertEquals("Empresa não encontrada.", exception.getMessage());
    }

    /*@Test
    void deveBloquearSolicitacaoComSelosNaoValidadosApos30Dias() {
        // Arrange
        var dataMaisDe30Dias = LocalDateTime.now().minusDays(31);

        when(empresaRepository.findById(empresaId)).thenReturn(Optional.of(empresaAtiva));
        when(seloFiscalRepository.existemSelosNaoValidadosApos30Dias(empresaId, dataMaisDe30Dias))
                .thenReturn(true);

        // Act & Assert
        var exception = assertThrows(IllegalStateException.class,
                () -> solicitacaoSeloService.solicitarSelo(solicitacaoRequest));

        assertEquals("Empresa possui selos não validados há mais de 30 dias", exception.getMessage());
    }*/
}
