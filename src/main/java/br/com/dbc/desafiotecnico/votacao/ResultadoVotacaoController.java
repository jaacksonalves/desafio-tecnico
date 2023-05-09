package br.com.dbc.desafiotecnico.votacao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class ResultadoVotacaoController {
  private static final Logger logger = LoggerFactory.getLogger(ResultadoVotacaoController.class);

  private final SessaoRepository sessaoRepository;

  public ResultadoVotacaoController(SessaoRepository sessaoRepository) {
    this.sessaoRepository = sessaoRepository;
  }

  @GetMapping("/sessao/{sessaoId}/resultado")
  public ResultadoVotacaoResponse resultadoVotacao(@PathVariable Long sessaoId) {
    var sessao =
        sessaoRepository
            .findById(sessaoId)
            .orElseThrow(
                () -> {
                  logger.info("Sessão não encontrada: {}", sessaoId);
                  return new ResponseStatusException(HttpStatus.NOT_FOUND, "Sessão não encontrada");
                });

    if (sessao.isAberta()) {
      logger.info("Sessão ainda aberta: {}", sessao);
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Sessão ainda aberta, favor aguardar término da votação");
    }

    return new ResultadoVotacaoResponse(sessao);
  }
}
