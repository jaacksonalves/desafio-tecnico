package br.com.dbc.desafiotecnico.sessao;

import br.com.dbc.desafiotecnico.pauta.PautaRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class NovaSessaoController {
  private static final Logger logger = LoggerFactory.getLogger(NovaSessaoController.class);

  /*
  Aqui existia ainda a possibilidade de criar uma Service e injetar o repository nela, mas como foi solicitado
  não realizar over engineering, optei por injetar o Repository direto no Controller até pra não gerar uma
  camada a mais de indireção sem necessidade.
    */
  private final PautaRepository pautaRepository;
  private final SessaoRepository sessaoRepository;

  public NovaSessaoController(PautaRepository pautaRepository, SessaoRepository sessaoRepository) {
    this.pautaRepository = pautaRepository;
    this.sessaoRepository = sessaoRepository;
  }

  @Transactional
  @PostMapping("/pauta/{pautaId}/sessao")
  public ResponseEntity<?> novaSessao(
      @PathVariable Long pautaId,
      @Valid @RequestBody NovaSessaoRequest novaSessaoRequest,
      UriComponentsBuilder uriBuilder) {
    var pauta =
        pautaRepository
            .findById(pautaId)
            .orElseThrow(
                () -> {
                  logger.info("Pauta não encontrada: {}", pautaId);
                  return new ResponseStatusException(HttpStatus.NOT_FOUND, "Pauta não encontrada");
                });

    logger.info("Recebendo request para criar nova sessão: {}", novaSessaoRequest);
    Sessao novaSessao = pauta.abreSessao(novaSessaoRequest);
    sessaoRepository.save(novaSessao);
    logger.info("Sessão criada com sucesso: {}", novaSessao);

    var locationURI =
        uriBuilder
            .path("/pauta/{pautaId}/sessao/{sessaoId}")
            .buildAndExpand(pautaId, novaSessao.getId())
            .toUri();
    return ResponseEntity.created(locationURI).build();
  }
}
