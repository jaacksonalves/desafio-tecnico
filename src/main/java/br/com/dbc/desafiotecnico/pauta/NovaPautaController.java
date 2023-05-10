package br.com.dbc.desafiotecnico.pauta;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class NovaPautaController {
  private final Logger logger = LoggerFactory.getLogger(NovaPautaController.class);

  /*
  Aqui existia ainda a possibilidade de criar uma Service e injetar o repository nela, mas como foi solicitado
  não realizar over engineering, optei por injetar o Repository direto no Controller até pra não gerar uma
  camada a mais de indireção sem necessidade.
    */
  private final PautaRepository pautaRepository;

  public NovaPautaController(PautaRepository pautaRepository) {
    this.pautaRepository = pautaRepository;
  }

  @Transactional
  @PostMapping("/pauta")
  public ResponseEntity<?> novaPauta(
      @Valid @RequestBody NovaPautaRequest request, UriComponentsBuilder uriBuilder) {
    if (pautaRepository.existsByTitulo(request.titulo())) {
      logger.info("Pauta já cadastrada: {}", request);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pauta já cadastrada");
    }

    logger.info("Recebendo request para criar nova pauta: {}", request);
    var novaPauta = request.toModel();
    pautaRepository.save(novaPauta);
    logger.info("Pauta criada com sucesso: {}", novaPauta);

    var locationURI = uriBuilder.path("/pauta/{id}").buildAndExpand(novaPauta.getId()).toUri();
    return ResponseEntity.created(locationURI).build();
  }
}
