package com.infnet.empresa_crud.service;

import com.infnet.empresa_crud.entity.Funcionario;
import com.infnet.empresa_crud.repository.FuncionarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FuncionarioServiceTest {

    @Mock
    private FuncionarioRepository repository;

    @InjectMocks
    private FuncionarioService service;

    private Funcionario funcionario;

    @BeforeEach
    void setUp() {
        funcionario = new Funcionario();
        funcionario.setId(1L);
        funcionario.setNome("João Silva");
        funcionario.setCpf("12345678901");
        funcionario.setEmail("joao@empresa.com");
        funcionario.setCargo("Desenvolvedor");
        funcionario.setSalario(new BigDecimal("5000.00"));
        funcionario.setDataAdmissao(LocalDate.now());
        funcionario.setTelefone("11999999999");
    }

    @Test
    void testCreateFuncionario_Success() {
        // Arrange
        when(repository.findByCpf(anyString())).thenReturn(Optional.empty());
        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(repository.save(any(Funcionario.class))).thenReturn(funcionario);

        // Act
        Funcionario result = service.create(funcionario);

        // Assert
        assertNotNull(result);
        assertEquals("João Silva", result.getNome());
        assertEquals("12345678901", result.getCpf());
        verify(repository, times(1)).save(funcionario);
    }

    @Test
    void testCreateFuncionario_CpfJaExiste() {
        // Arrange
        when(repository.findByCpf(anyString())).thenReturn(Optional.of(funcionario));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.create(funcionario);
        });
        assertEquals("CPF já cadastrado: 12345678901", exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void testCreateFuncionario_EmailJaExiste() {
        // Arrange
        when(repository.findByCpf(anyString())).thenReturn(Optional.empty());
        when(repository.findByEmail(anyString())).thenReturn(Optional.of(funcionario));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.create(funcionario);
        });
        assertEquals("Email já cadastrado: joao@empresa.com", exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void testGetById_Success() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(funcionario));

        // Act
        Funcionario result = service.getById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("João Silva", result.getNome());
    }

    @Test
    void testGetById_NotFound() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.getById(1L);
        });
        assertEquals("Funcionário não encontrado com ID: 1", exception.getMessage());
    }

    @Test
    void testGetAll_Success() {
        // Arrange
        Funcionario funcionario2 = new Funcionario();
        funcionario2.setId(2L);
        funcionario2.setNome("Maria Silva");

        List<Funcionario> funcionarios = Arrays.asList(funcionario, funcionario2);
        when(repository.findAll()).thenReturn(funcionarios);

        // Act
        List<Funcionario> result = service.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("João Silva", result.get(0).getNome());
        assertEquals("Maria Silva", result.get(1).getNome());
    }

    @Test
    void testUpdate_Success() {
        // Arrange
        Funcionario funcionarioAtualizado = new Funcionario();
        funcionarioAtualizado.setNome("João Santos");
        funcionarioAtualizado.setSalario(new BigDecimal("6000.00"));

        when(repository.findById(1L)).thenReturn(Optional.of(funcionario));
        when(repository.save(any(Funcionario.class))).thenReturn(funcionario);

        // Act
        Funcionario result = service.update(1L, funcionarioAtualizado);

        // Assert
        assertNotNull(result);
        verify(repository, times(1)).save(funcionario);
    }

    @Test
    void testDelete_Success() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(funcionario));

        // Act
        service.delete(1L);

        // Assert
        verify(repository, times(1)).delete(funcionario);
    }

    @Test
    void testDelete_NotFound() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.delete(1L);
        });
        assertEquals("Funcionário não encontrado com ID: 1", exception.getMessage());
        verify(repository, never()).delete(any());
    }

    @Test
    void testGetByCpf_Success() {
        // Arrange
        when(repository.findByCpf("12345678901")).thenReturn(Optional.of(funcionario));

        // Act
        Funcionario result = service.getByCpf("12345678901");

        // Assert
        assertNotNull(result);
        assertEquals("12345678901", result.getCpf());
    }

    @Test
    void testExistsById_True() {
        // Arrange
        when(repository.existsById(1L)).thenReturn(true);

        // Act
        boolean result = service.existsById(1L);

        // Assert
        assertTrue(result);
    }

    @Test
    void testExistsById_False() {
        // Arrange
        when(repository.existsById(1L)).thenReturn(false);

        // Act
        boolean result = service.existsById(1L);

        // Assert
        assertFalse(result);
    }
}
