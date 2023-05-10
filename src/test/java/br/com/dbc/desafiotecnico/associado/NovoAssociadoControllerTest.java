package br.com.dbc.desafiotecnico.associado;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class NovoAssociadoControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper mapper;
  @Autowired private AssociadoRepository associadoRepository;

  @Test
  @DisplayName("deve criar um novo associado")
  void teste1() throws Exception {
    var novoAssociadoRequest = new NovoAssociadoRequest("Nome", "85493335069");
    var requestJson = mapper.writeValueAsString(novoAssociadoRequest);

    mockMvc
        .perform(post("/associado").contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andExpect(status().isCreated());

    var associados = associadoRepository.findAll();
    assertFalse(associados.isEmpty());
    var associado = associados.get(0);

    assertEquals(novoAssociadoRequest.nome(), associado.getNome());
    assertEquals(novoAssociadoRequest.cpf(), associado.getCpf());
  }

  @Test
  @DisplayName("Não deve cadastrar novo associado quando já existe um com o mesmo CPF")
  void teste2() throws Exception {
    var novoAssociadoRequest = new NovoAssociadoRequest("Nome", "85493335069");
    var requestJson = mapper.writeValueAsString(novoAssociadoRequest);

    mockMvc
        .perform(post("/associado").contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andExpect(status().isCreated());

    var associados = associadoRepository.findAll();
    assertEquals(1, associados.size());

    mockMvc
        .perform(post("/associado").contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andExpect(status().isBadRequest());
    assertEquals(1, associados.size());
  }
}
