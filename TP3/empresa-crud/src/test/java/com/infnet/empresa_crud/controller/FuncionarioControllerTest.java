package com.infnet.empresa_crud.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infnet.empresa_crud.entity.Funcionario;
import com.infnet.empresa_crud.service.FuncionarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FuncionarioControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FuncionarioService funcionarioService;

    @InjectMocks
    private FuncionarioController funcionarioController;

    private Funcionario funcionario1;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(funcionarioController).build();

        funcionario1 = new Funcionario();
        funcionario1.setId(1L);
        funcionario1.setNome("João Silva");
        funcionario1.setCpf("12345678901");
        funcionario1.setEmail("joao@empresa.com");
        funcionario1.setCargo("Desenvolvedor");
        funcionario1.setSalario(new BigDecimal("5000.00"));
        funcionario1.setDataAdmissao(LocalDate.now());
        funcionario1.setTelefone("11999999999");

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    }

    @Test
    void testCreateFuncionario() throws Exception {
        when(funcionarioService.create(any(Funcionario.class))).thenReturn(funcionario1);

        mockMvc.perform(post("/api/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(funcionario1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.cpf").value("12345678901"));

        verify(funcionarioService, times(1)).create(any(Funcionario.class));
    }

    @Test
    void testCreateFuncionario_BadRequest() throws Exception {
        when(funcionarioService.create(any(Funcionario.class)))
                .thenThrow(new RuntimeException("CPF já cadastrado"));

        mockMvc.perform(post("/api/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(funcionario1)))
                .andExpect(status().isBadRequest());

        verify(funcionarioService, times(1)).create(any(Funcionario.class));
    }

    @Test
    void testGetAllFuncionarios() throws Exception {
        Funcionario funcionario2 = new Funcionario();
        funcionario2.setId(2L);
        funcionario2.setNome("Maria Silva");

        List<Funcionario> funcionarios = Arrays.asList(funcionario1, funcionario2);
        when(funcionarioService.getAll()).thenReturn(funcionarios);

        mockMvc.perform(get("/api/funcionarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nome").value("João Silva"))
                .andExpect(jsonPath("$[1].nome").value("Maria Silva"));

        verify(funcionarioService, times(1)).getAll();
    }

    @Test
    void testGetFuncionarioById() throws Exception {
        when(funcionarioService.getById(1L)).thenReturn(funcionario1);

        mockMvc.perform(get("/api/funcionarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.cpf").value("12345678901"));

        verify(funcionarioService, times(1)).getById(1L);
    }

    @Test
    void testGetFuncionarioById_NotFound() throws Exception {
        when(funcionarioService.getById(1L))
                .thenThrow(new RuntimeException("Funcionário não encontrado"));

        mockMvc.perform(get("/api/funcionarios/1"))
                .andExpect(status().isNotFound());

        verify(funcionarioService, times(1)).getById(1L);
    }

    @Test
    void testUpdateFuncionario() throws Exception {
        Funcionario funcionarioAtualizado = new Funcionario();
        funcionarioAtualizado.setId(1L);
        funcionarioAtualizado.setNome("João Santos");
        funcionarioAtualizado.setCargo("Desenvolvedor Senior");

        when(funcionarioService.update(anyLong(), any(Funcionario.class)))
                .thenReturn(funcionarioAtualizado);

        mockMvc.perform(put("/api/funcionarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(funcionarioAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Santos"))
                .andExpect(jsonPath("$.cargo").value("Desenvolvedor Senior"));

        verify(funcionarioService, times(1)).update(anyLong(), any(Funcionario.class));
    }

    @Test
    void testDeleteFuncionario() throws Exception {
        doNothing().when(funcionarioService).delete(1L);

        mockMvc.perform(delete("/api/funcionarios/1"))
                .andExpect(status().isNoContent());

        verify(funcionarioService, times(1)).delete(1L);
    }

    @Test
    void testDeleteFuncionario_NotFound() throws Exception {
        doThrow(new RuntimeException("Funcionário não encontrado"))
                .when(funcionarioService).delete(1L);

        mockMvc.perform(delete("/api/funcionarios/1"))
                .andExpect(status().isNotFound());

        verify(funcionarioService, times(1)).delete(1L);
    }

    @Test
    void testHealthCheck() throws Exception {
        mockMvc.perform(get("/api/funcionarios/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Funcionario API is working!"));

        // Health check não usa o service, então não verificamos
    }
}