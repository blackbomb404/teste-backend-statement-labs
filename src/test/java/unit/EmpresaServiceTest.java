package unit;

import com.statementlabs.BackEndTest.application.exceptions.NifAlreadyInUseException;
import com.statementlabs.BackEndTest.application.repositories.EmpresaRepository;
import com.statementlabs.BackEndTest.application.services.LogAuditoriaService;
import com.statementlabs.BackEndTest.application.services.impl.EmpresaServiceImpl;
import com.statementlabs.BackEndTest.domain.model.Empresa;
import com.statementlabs.BackEndTest.domain.model.StatusEmpresa;
import com.statementlabs.BackEndTest.domain.model.TipoEmpresa;
import com.statementlabs.BackEndTest.presentation.dtos.Empresa.EmpresaCreationDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmpresaServiceTest {
    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private LogAuditoriaService auditoriaService;

    @InjectMocks
    private EmpresaServiceImpl empresaService;

    private EmpresaCreationDto empresaCreationDto;
    private Empresa empresa;

    @BeforeEach
    void setUp() {
        empresaCreationDto = new EmpresaCreationDto("Empresa Teste Lda", "123456789", TipoEmpresa.FABRICANTE);

        empresa = new Empresa();
        empresa.setId(UUID.randomUUID());
        empresa.setNome("Empresa Teste Lda");
        empresa.setNif("123456789");
        empresa.setTipo(TipoEmpresa.FABRICANTE);
        empresa.setStatus(StatusEmpresa.ATIVA);
        empresa.setDataRegistro(LocalDateTime.now());
    }

    @Test
    void deveCriarEmpresaComSucesso() {
        // Arrange
        when(empresaRepository.existsByNif(empresaCreationDto.nif())).thenReturn(false);
        when(empresaRepository.save(any(Empresa.class))).thenReturn(empresa);

        // Act
        var result = empresaService.registrarEmpresa(empresaCreationDto);

        // Assert
        assertNotNull(result);
        assertEquals("Empresa Teste Lda", result.nome());
        assertEquals("123456789", result.nif());
        assertEquals(TipoEmpresa.FABRICANTE, result.tipo());
        assertEquals(StatusEmpresa.ATIVA, result.status());

        verify(empresaRepository).save(any(Empresa.class));
        verify(auditoriaService).registrar(eq("Empresa"), eq("EMPRESA_CRIADA"), anyString(), any());
    }

    @Test
    void naoDevePermitirNifDuplicado() {
        // Arrange
        when(empresaRepository.existsByNif(empresaCreationDto.nif())).thenReturn(true);
        //when(empresaRepository.save(empresa)).thenReturn(empresa);

        // Act & Assert
        var exception = assertThrows(NifAlreadyInUseException.class,
                () -> empresaService.registrarEmpresa(empresaCreationDto));

        verify(empresaRepository, never()).save(any(Empresa.class));
    }
}
