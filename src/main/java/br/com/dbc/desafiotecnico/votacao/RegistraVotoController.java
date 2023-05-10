package br.com.dbc.desafiotecnico.votacao;

import br.com.dbc.desafiotecnico.associado.AssociadoRepository;
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
public class RegistraVotoController {
  private static final Logger logger = LoggerFactory.getLogger(RegistraVotoController.class);

  private final RegistraVoto registraVoto;
  private final SessaoRepository sessaoRepository;
  private final AssociadoRepository associadoRepository;

  public RegistraVotoController(
      RegistraVoto registraVoto,
      SessaoRepository sessaoRepository,
      AssociadoRepository associadoRepository) {
    this.registraVoto = registraVoto;
    this.sessaoRepository = sessaoRepository;
    this.associadoRepository = associadoRepository;
  }

  @Transactional
  @PostMapping("/sessao/{sessaoId}/voto")
  public ResponseEntity<?> novoVoto(
      @PathVariable Long sessaoId,
      @Valid @RequestBody RegistraVotoRequest request,
      UriComponentsBuilder uriBuilder,
      @RequestParam Long associadoId) {
    /*
    normalmente o associado estaria logado e receberia de alguma forma ele pronto com o @AuthenticationPrincipal do
    Spring Security ou algo do tipo. Para fins de simplificação, optei por receber o id do associado como parâmetro
     */
    var associado =
        associadoRepository
            .findById(associadoId)
            .orElseThrow(
                () -> {
                  // aqui apenas mantive o padrão, mas o Associado já chegaria pronto aqui
                  // normalmente
                  logger.info("Associado não encontrado: {}", associadoId);
                  return new ResponseStatusException(
                      HttpStatus.NOT_FOUND, "Associado não encontrado");
                });

    var sessao =
        sessaoRepository
            .findById(sessaoId)
            .orElseThrow(
                () -> {
                  logger.info("Sessão não encontrada: {}", sessaoId);
                  return new ResponseStatusException(HttpStatus.NOT_FOUND, "Sessão não encontrada");
                });

    var voto = registraVoto.executa(sessao, associado, request.tipoVoto());
    logger.info("Voto registrado com sucesso: {}", voto);

    var locationURI = uriBuilder.path("/voto/{votoId}").buildAndExpand(voto.getId()).toUri();
    return ResponseEntity.created(locationURI).build();
  }
}
