package br.com.dbc.desafiotecnico.votacao;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

import br.com.dbc.desafiotecnico.associado.Associado;
import br.com.dbc.desafiotecnico.associado.AssociadoRepository;
import br.com.dbc.desafiotecnico.pauta.Pauta;
import br.com.dbc.desafiotecnico.pauta.PautaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
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
class RegistraVotoControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper mapper;
  @Autowired private PautaRepository pautaRepository;
  @Autowired private SessaoRepository sessaoRepository;
  @Autowired private AssociadoRepository associadoRepository;

  @Test
  @DisplayName("Deve registrar um voto")
  void teste1() throws Exception {
    var associado = associadoRepository.save(new Associado("nome", "85493335069"));
    var pauta = new Pauta("titulo", "descricao");
    pautaRepository.save(pauta);
    var novaSessaoRequest = new NovaSessaoRequest(LocalDateTime.now().plusMinutes(10));
    var sessao = pauta.abreSessao(novaSessaoRequest);
    sessaoRepository.save(sessao);

    var registraVotoRequest = new RegistraVotoRequest(TipoVoto.SIM);
    var requestJson = mapper.writeValueAsString(registraVotoRequest);

    mockMvc
        .perform(
            post("/sessao/{sessaoId}/voto", sessao.getId())
                .param("associadoId", associado.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
        .andExpect(status().isCreated());

    var votos = sessao.getVotos();
    assertFalse(votos.isEmpty());
    var voto = votos.get(0);
    assertEquals(registraVotoRequest.tipoVoto(), voto.getTipoVoto());
  }

  @Test
  @DisplayName("Não deve registrar voto quando Associado já votou")
  void teste2() throws Exception {
    var associado = associadoRepository.save(new Associado("nome", "85493335069"));
    var pauta = new Pauta("titulo", "descricao");
    pautaRepository.save(pauta);
    var novaSessaoRequest = new NovaSessaoRequest(LocalDateTime.now().plusMinutes(10));
    var sessao = pauta.abreSessao(novaSessaoRequest);
    sessaoRepository.save(sessao);

    var registraVotoRequest = new RegistraVotoRequest(TipoVoto.SIM);
    var requestJson = mapper.writeValueAsString(registraVotoRequest);

    mockMvc
        .perform(
            post("/sessao/{sessaoId}/voto", sessao.getId())
                .param("associadoId", associado.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
        .andExpect(status().isCreated());

    var votos = sessao.getVotos();
    assertEquals(1, votos.size());

    mockMvc
        .perform(
            post("/sessao/{sessaoId}/voto", sessao.getId())
                .param("associadoId", associado.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
        .andExpect(status().isBadRequest());
    assertEquals(1, votos.size());
  }

  @Test
  @DisplayName("Não deve registrar voto quando Sessão já encerrada")
  void teste3() throws Exception {
    var associado = associadoRepository.save(new Associado("nome", "85493335069"));
    var pauta = new Pauta("titulo", "descricao");
    pautaRepository.save(pauta);
    var novaSessaoRequest = new NovaSessaoRequest(LocalDateTime.now().plusSeconds(1));
    var sessao = pauta.abreSessao(novaSessaoRequest);
    sessaoRepository.save(sessao);

    await().until(() -> !sessao.isAberta());

    var registraVotoRequest = new RegistraVotoRequest(TipoVoto.SIM);
    var requestJson = mapper.writeValueAsString(registraVotoRequest);

    mockMvc
        .perform(
            post("/sessao/{sessaoId}/voto", sessao.getId())
                .param("associadoId", associado.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
        .andExpect(status().isBadRequest());

    var votos = sessao.getVotos();
    assertTrue(votos.isEmpty());
  }
}
