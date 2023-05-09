package br.com.dbc.desafiotecnico.associado;

import static org.slf4j.LoggerFactory.*;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class NovoAssociadoController {
  private static final Logger logger = getLogger(NovoAssociadoController.class);

  /*
  Aqui existia ainda a possibilidade de criar uma Service e injetar o repository nela, mas como foi solicitado
  não realizar over engineering, optei por injetar o Repository direto no Controller até pra não gerar uma
  camada a mais de indireção sem necessidade.
    */
  private final AssociadoRepository associadoRepository;

  public NovoAssociadoController(AssociadoRepository associadoRepository) {
    this.associadoRepository = associadoRepository;
  }

  @Transactional
  @PostMapping("/associado")
  public ResponseEntity<?> novoAssociado(
      @Valid @RequestBody NovoAssociadoRequest request, UriComponentsBuilder uriBuilder) {
    logger.info("Recebendo request para criar novo associado: {}", request);
    var novoAssociado = request.toModel();
    associadoRepository.save(novoAssociado);
    logger.info("Associado criado com sucesso: {}", novoAssociado);

    var locationURI =
        uriBuilder.path("/associado/{id}").buildAndExpand(novoAssociado.getId()).toUri();
    return ResponseEntity.created(locationURI).build();
  }
}
