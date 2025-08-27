package unit;

// ============================================================================
// TESTES PARA SELO FISCAL SERVICE
// ============================================================================

import com.statementlabs.BackEndTest.application.repositories.SeloFiscalRepository;
import com.statementlabs.BackEndTest.application.repositories.SolicitacaoSeloFiscalRepository;
import com.statementlabs.BackEndTest.application.services.CodigoSeloService;
import com.statementlabs.BackEndTest.application.services.LogAuditoriaService;
import com.statementlabs.BackEndTest.application.services.impl.SeloFiscalServiceImpl;
import com.statementlabs.BackEndTest.domain.model.*;
import com.statementlabs.BackEndTest.presentation.dtos.SeloFiscal.SeloCreationDto;
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
class SeloFiscalServiceTest {

    @Mock
    private SeloFiscalRepository seloFiscalRepository;

    @Mock
    private SolicitacaoSeloFiscalRepository solicitacaoRepository;

    @Mock
    private LogAuditoriaService auditoriaService;

    @Mock
    private CodigoSeloService codigoSeloService;

    @InjectMocks
    private SeloFiscalServiceImpl seloFiscalService;

    private UUID solicitacaoId;
    private UUID solicitacaoId2;
    private UUID seloId;
    private UUID seloId2;
    private Empresa empresa;
    private SolicitacaoSeloFiscal solicitacao;
    private SolicitacaoSeloFiscal solicitacao2;
    private SeloFiscal seloEmitido;
    private SeloFiscal seloEmitido2;
    private SeloFiscal seloValidado;
    private SeloCreationDto seloCreationDto;
    private SeloCreationDto seloCreationDto2;

    @BeforeEach
    void setUp() {
        solicitacaoId = UUID.randomUUID();
        solicitacaoId2 = UUID.randomUUID();
        seloId = UUID.randomUUID();
        seloId2 = UUID.randomUUID();

        empresa = new Empresa();
        empresa.setId(UUID.randomUUID());
        empresa.setNome("Empresa Teste");
        empresa.setStatus(StatusEmpresa.ATIVA);
        empresa.setTipo(TipoEmpresa.FABRICANTE);

        solicitacao = new SolicitacaoSeloFiscal();
        solicitacao.setId(solicitacaoId);
        solicitacao.setEmpresa(empresa);
        solicitacao.setProduto("Whisky Premium");
        solicitacao.setStatus(StatusSolicitacaoSelo.PENDENTE);

        solicitacao2 = new SolicitacaoSeloFiscal();
        solicitacao2.setId(solicitacaoId2);
        solicitacao2.setEmpresa(empresa);
        solicitacao2.setProduto("Whisky Premium");
        solicitacao2.setStatus(StatusSolicitacaoSelo.PENDENTE);

        seloEmitido = new SeloFiscal();
        seloEmitido.setId(seloId);
        seloEmitido.setEmpresa(empresa);
        seloEmitido.setProduto("Whisky Premium");
        seloEmitido.setCodigo("PROSEFA-2025-000001");
        seloEmitido.setEstado(EstadoSeloFiscal.EMITIDO);
        seloEmitido.setDataEmissao(LocalDateTime.now());

        seloEmitido2 = new SeloFiscal();
        seloEmitido2.setId(seloId2);
        seloEmitido2.setEmpresa(empresa);
        seloEmitido2.setProduto("Whisky Premium");
        seloEmitido2.setCodigo("PROSEFA-2025-000002");
        seloEmitido2.setEstado(EstadoSeloFiscal.EMITIDO);
        seloEmitido2.setDataEmissao(LocalDateTime.now());

        seloValidado = new SeloFiscal();
        seloValidado.setId(seloId);
        seloValidado.setEstado(EstadoSeloFiscal.VALIDADO);

        seloCreationDto = new SeloCreationDto(solicitacaoId);
        seloCreationDto2 = new SeloCreationDto(solicitacaoId2);
    }

    @Test
    void deveEmitirSelosComSucesso() {
        // Arrange
        when(solicitacaoRepository.findById(solicitacaoId)).thenReturn(Optional.of(solicitacao));
        when(codigoSeloService.gerarCodigo()).thenReturn("PROSEFA-2025-000001");
        when(seloFiscalRepository.save(any(SeloFiscal.class))).thenReturn(seloEmitido);

        // Act
        var result = seloFiscalService.emitirSelo(seloCreationDto);

        // Assert
        assertNotNull(result);
        assertNotNull(result.id());
        verify(seloFiscalRepository).save(any(SeloFiscal.class));
        verify(solicitacaoRepository).save(argThat(s -> s.getStatus() == StatusSolicitacaoSelo.APROVADA));
        verify(auditoriaService).registrar(eq("SeloFiscal"), eq("SELO_EMITIDO"), anyString(), any());
    }

    @Test
    void naoDeveEmitirSeloParaSolicitacaoJaProcessada() {
        // Arrange
        solicitacao.setStatus(StatusSolicitacaoSelo.APROVADA);
        when(solicitacaoRepository.findById(solicitacaoId)).thenReturn(Optional.of(solicitacao));

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> seloFiscalService.emitirSelo(seloCreationDto));

        //assertEquals("Solicitação já foi processada", exception.getMessage());
        verify(seloFiscalRepository, never()).saveAll(anyList());
    }

    @Test
    void deveGerarCodigoSequencial() {
        // Arrange
        when(solicitacaoRepository.findById(solicitacaoId)).thenReturn(Optional.of(solicitacao));
        when(solicitacaoRepository.findById(solicitacaoId2)).thenReturn(Optional.of(solicitacao2));
        when(codigoSeloService.gerarCodigo())
                .thenReturn("PROSEFA-2025-000001")
                .thenReturn("PROSEFA-2025-000002");
        when(seloFiscalRepository.save(any(SeloFiscal.class)))
                .thenReturn(seloEmitido)
                .thenReturn(seloEmitido2);

        // Act
        var result1 = seloFiscalService.emitirSelo(seloCreationDto);
        var result2 = seloFiscalService.emitirSelo(seloCreationDto2);

        // Assert
        verify(codigoSeloService, times(2)).gerarCodigo();
        // Verificar se os códigos seguem o padrão PROSEFA-{ano}-{sequencia}
        assertTrue(result1.codigo().matches("PROSEFA-\\d{4}-\\d{6}"));
        assertTrue(result2.codigo().matches("PROSEFA-\\d{4}-\\d{6}"));
    }

    @Test
    void deveValidarSeloUmaVez() {
        // Arrange
        when(seloFiscalRepository.findById(seloId)).thenReturn(Optional.of(seloEmitido));
        when(seloFiscalRepository.save(any(SeloFiscal.class))).thenReturn(seloValidado);

        // Act
        var result = seloFiscalService.validarSelo(seloId);

        // Assert
        assertNotNull(result);
        verify(seloFiscalRepository).save(argThat(s -> s.getEstado() == EstadoSeloFiscal.VALIDADO));
        verify(auditoriaService).registrar(eq("SeloFiscal"), eq("SELO_VALIDADO"), anyString(), any());
    }

    @Test
    void naoDeveValidarSeloJaValidado() {
        // Arrange
        seloEmitido.setEstado(EstadoSeloFiscal.VALIDADO);
        when(seloFiscalRepository.findById(seloId)).thenReturn(Optional.of(seloEmitido));

        // Act & Assert
        var exception = assertThrows(IllegalStateException.class,
                () -> seloFiscalService.validarSelo(seloId));

        //assertEquals("Selo já foi validado anteriormente", exception.getMessage());
        verify(seloFiscalRepository, never()).save(any(SeloFiscal.class));
    }

    @Test
    void deveLancarExcecaoParaSeloInexistente() {
        // Arrange
        when(seloFiscalRepository.findById(seloId)).thenReturn(Optional.empty());

        // Act & Assert
        var exception = assertThrows(EntityNotFoundException.class,
                () -> seloFiscalService.validarSelo(seloId));

        //assertEquals("Selo não encontrado", exception.getMessage());
    }
}
