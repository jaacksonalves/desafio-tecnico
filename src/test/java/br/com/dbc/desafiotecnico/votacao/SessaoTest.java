package br.com.dbc.desafiotecnico.votacao;

import static org.junit.jupiter.api.Assertions.*;

import br.com.dbc.desafiotecnico.associado.Associado;
import br.com.dbc.desafiotecnico.pauta.Pauta;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class SessaoTest {
  @Nested
  class GetResultadoVotacaoTest {
    @Test
    @DisplayName("deve retornar APROVADO quando tem mais votos SIM")
    void teste1() {
      var associado = new Associado("nome", "85493335069");
      var associado2 = new Associado("nome2", "38309668066");
      var associado3 = new Associado("nome3", "87247846054");
      var pauta = new Pauta("titulo", "descricao");
      var sessao = pauta.abreSessao(new NovaSessaoRequest(LocalDateTime.now().plusMinutes(1)));
      sessao.registraVoto(TipoVoto.SIM, associado);
      sessao.registraVoto(TipoVoto.SIM, associado2);
      sessao.registraVoto(TipoVoto.NAO, associado3);

      ReflectionTestUtils.setField(
          sessao, "instanteEncerramento", LocalDateTime.now().minusNanos(1));
      assertEquals(TipoResultadoVotacao.APROVADO, sessao.getResultadoVotacao());
    }

    @Test
    @DisplayName("deve retornar REPROVADO quando tem mais votos NAO")
    void teste2() {
      var associado = new Associado("nome", "85493335069");
      var associado2 = new Associado("nome2", "38309668066");
      var associado3 = new Associado("nome3", "87247846054");
      var pauta = new Pauta("titulo", "descricao");
      var sessao = pauta.abreSessao(new NovaSessaoRequest(LocalDateTime.now().plusMinutes(1)));
      sessao.registraVoto(TipoVoto.SIM, associado);
      sessao.registraVoto(TipoVoto.NAO, associado2);
      sessao.registraVoto(TipoVoto.NAO, associado3);

      ReflectionTestUtils.setField(
          sessao, "instanteEncerramento", LocalDateTime.now().minusNanos(1));
      assertEquals(TipoResultadoVotacao.REPROVADO, sessao.getResultadoVotacao());
    }

    @Test
    @DisplayName("deve retornar EMPATE quando tem votos iguais pra sim e nao")
    void teste3() {
      var associado = new Associado("nome", "85493335069");
      var associado2 = new Associado("nome2", "38309668066");
      var pauta = new Pauta("titulo", "descricao");
      var sessao = pauta.abreSessao(new NovaSessaoRequest(LocalDateTime.now().plusMinutes(1)));
      sessao.registraVoto(TipoVoto.SIM, associado);
      sessao.registraVoto(TipoVoto.NAO, associado2);

      ReflectionTestUtils.setField(
              sessao, "instanteEncerramento", LocalDateTime.now().minusNanos(1));
      assertEquals(TipoResultadoVotacao.EMPATE, sessao.getResultadoVotacao());
    }
  }
}
