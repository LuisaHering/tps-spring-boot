package com.infnet.empresa_crud;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infnet.empresa_crud.entity.Funcionario;
import com.infnet.empresa_crud.entity.Produto;
import com.infnet.empresa_crud.entity.Cliente;
import com.infnet.empresa_crud.repository.FuncionarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
class EmpresaCrudIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        funcionarioRepository.deleteAll();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    }

    @Test
    void testFuncionarioCrudCompleto() throws Exception {
        // 1. CREATE - Criar funcionário
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Ana Costa");
        funcionario.setCpf("98765432100");
        funcionario.setEmail("ana@empresa.com");
        funcionario.setCargo("Gerente");
        funcionario.setSalario(new BigDecimal("8000.00"));
        funcionario.setDataAdmissao(LocalDate.now());
        funcionario.setTelefone("11888888888");

        String funcionarioJson = objectMapper.writeValueAsString(funcionario);

        // POST - Criar
        String response = mockMvc.perform(post("/api/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(funcionarioJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Ana Costa"))
                .andExpect(jsonPath("$.cpf").value("98765432100"))
                .andReturn().getResponse().getContentAsString();

        Funcionario funcionarioRetornado = objectMapper.readValue(response, Funcionario.class);
        Long funcionarioId = funcionarioRetornado.getId();

        // 2. READ - Buscar por ID
        mockMvc.perform(get("/api/funcionarios/" + funcionarioId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Ana Costa"))
                .andExpect(jsonPath("$.cargo").value("Gerente"));

        // 3. READ - Listar todos
        mockMvc.perform(get("/api/funcionarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        // 4. UPDATE - Atualizar
        funcionario.setNome("Ana Silva Costa");
        funcionario.setSalario(new BigDecimal("9000.00"));
        funcionarioJson = objectMapper.writeValueAsString(funcionario);

        mockMvc.perform(put("/api/funcionarios/" + funcionarioId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(funcionarioJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Ana Silva Costa"));

        // 5. DELETE - Deletar
        mockMvc.perform(delete("/api/funcionarios/" + funcionarioId))
                .andExpect(status().isNoContent());

        // 6. Verificar se foi deletado
        mockMvc.perform(get("/api/funcionarios/" + funcionarioId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testProdutoCrud() throws Exception {
        // CREATE Produto
        Produto produto = new Produto();
        produto.setNome("Notebook Dell");
        produto.setDescricao("Notebook para desenvolvimento");
        produto.setPreco(new BigDecimal("3500.00"));
        produto.setQuantidadeEstoque(10);
        produto.setCategoria("Informática");
        produto.setCodigoBarras("1234567890123");

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Notebook Dell"))
                .andExpect(jsonPath("$.categoria").value("Informática"));
    }

    @Test
    void testClienteCrud() throws Exception {
        // CREATE Cliente
        Cliente cliente = new Cliente();
        cliente.setNome("Carlos Oliveira");
        cliente.setCpf("11122233344");
        cliente.setEmail("carlos@email.com");
        cliente.setTelefone("11777777777");
        cliente.setEndereco("Rua das Flores, 123");
        cliente.setDataNascimento(LocalDate.of(1990, 5, 15));
        cliente.setDataCadastro(LocalDate.now());

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Carlos Oliveira"))
                .andExpect(jsonPath("$.cpf").value("11122233344"));
    }

    @Test
    void testValidacaoConstraints() throws Exception {
        // Teste com CPF duplicado
        Funcionario funcionario1 = new Funcionario();
        funcionario1.setNome("João Silva");
        funcionario1.setCpf("12345678901");
        funcionario1.setEmail("joao1@empresa.com");
        funcionario1.setCargo("Dev");
        funcionario1.setSalario(new BigDecimal("5000"));

        // Criar primeiro funcionário
        mockMvc.perform(post("/api/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(funcionario1)))
                .andExpect(status().isCreated());

        // Tentar criar segundo com mesmo CPF
        Funcionario funcionario2 = new Funcionario();
        funcionario2.setNome("Pedro Silva");
        funcionario2.setCpf("12345678901"); // CPF duplicado
        funcionario2.setEmail("pedro@empresa.com");
        funcionario2.setCargo("Tester");
        funcionario2.setSalario(new BigDecimal("4500"));

        mockMvc.perform(post("/api/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(funcionario2)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testEndpointsNotFound() throws Exception {
        // Teste buscar funcionário inexistente
        mockMvc.perform(get("/api/funcionarios/999"))
                .andExpect(status().isNotFound());

        // Teste deletar funcionário inexistente
        mockMvc.perform(delete("/api/funcionarios/999"))
                .andExpect(status().isNotFound());

        // Teste atualizar funcionário inexistente
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Teste");

        mockMvc.perform(put("/api/funcionarios/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(funcionario)))
                .andExpect(status().isNotFound());
    }
}