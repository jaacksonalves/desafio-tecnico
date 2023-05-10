package br.com.dbc.desafiotecnico.pauta;

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
class NovaPautaControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper mapper;
  @Autowired private PautaRepository pautaRepository;

  @Test
  @DisplayName("deve criar uma nova pauta")
  void teste1() throws Exception {
    var novaPautaRequest = new NovaPautaRequest("titulo", "descricao");
    var requestJson = mapper.writeValueAsString(novaPautaRequest);

    mockMvc
        .perform(post("/pauta").contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andExpect(status().isCreated());

    var pautas = pautaRepository.findAll();
    assertFalse(pautas.isEmpty());

    var pauta = pautas.get(0);
    assertEquals(novaPautaRequest.titulo(), pauta.getTitulo());
  }

  @Test
  @DisplayName("não deve criar uma nova pauta com o titulo de uma existente")
  void teste2() throws Exception {
    var novaPautaRequest = new NovaPautaRequest("titulo", "descricao");
    var requestJson = mapper.writeValueAsString(novaPautaRequest);

    mockMvc
        .perform(post("/pauta").contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andExpect(status().isCreated());

    var pautas = pautaRepository.findAll();
    assertEquals(1, pautas.size());

    mockMvc
        .perform(post("/pauta").contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andExpect(status().isBadRequest());
    assertEquals(1, pautas.size());
  }
}
