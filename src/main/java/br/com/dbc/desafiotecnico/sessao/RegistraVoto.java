package br.com.dbc.desafiotecnico.sessao;

import br.com.dbc.desafiotecnico.associado.Associado;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RegistraVoto {
  private static final Logger logger = LoggerFactory.getLogger(RegistraVoto.class);

  @Transactional
  public Voto executa(Sessao sessao, Associado associado, TipoVoto tipoVoto) {

    if (!sessao.isAberta()) {
      logger.info("Sessão já encerrada: {}", sessao);
      // aqui poderia ser um erro mais específico, mas para fins de simplificação, mantive a
      // exception pronta do Spring
      // lembrando ainda que essa Exception serve especificamente para erros com status http
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sessão já encerrada");
    }

    if (sessao.jaVotou(associado)) {
      logger.info("Associado já votou: {}", associado);
      // aqui poderia ser um erro mais específico, mas para fins de simplificação, mantive a
      // exception pronta do Spring
      // lembrando ainda que essa Exception serve especificamente para erros com status http
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Associado já votou");
    }

    logger.info("Registrando voto: {}", tipoVoto);
    var novoVoto = sessao.registraVoto(tipoVoto, associado);
    logger.info("Voto registrado com sucesso: {}", novoVoto);

    return novoVoto;
  }
}
