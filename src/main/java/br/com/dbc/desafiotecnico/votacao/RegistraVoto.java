package br.com.dbc.desafiotecnico.votacao;

import static br.com.dbc.desafiotecnico.clients.validacaocpf.TipoStatusValidacaoCPF.UNABLE_TO_VOTE;

import br.com.dbc.desafiotecnico.associado.Associado;
import br.com.dbc.desafiotecnico.clients.validacaocpf.VerificaAssociadoPodeVotarRemoto;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/** jackson: classe que registra o voto de um associado em uma sessão */
@Service
public class RegistraVoto {
  private static final Logger logger = LoggerFactory.getLogger(RegistraVoto.class);

  private final VerificaAssociadoPodeVotarRemoto verificaAssociadoPodeVotarRemoto;

  public RegistraVoto(VerificaAssociadoPodeVotarRemoto verificaAssociadoPodeVotarRemoto) {
    this.verificaAssociadoPodeVotarRemoto = verificaAssociadoPodeVotarRemoto;
  }

  /**
   * jackson: registra o voto de um associado em uma sessão
   *
   * @param sessao Sessao a ser votada
   * @param associado Associado que está votando
   * @param tipoVoto Tipo do voto do Associado
   * @return Voto registrado
   */
  @Transactional
  public Voto executa(Sessao sessao, Associado associado, TipoVoto tipoVoto) {
    /*
    jackson: verifica se o associado pode votar no sistema externo de verificação de CPF
     */
    try {
      var statusVerificacaoCPF = verificaAssociadoPodeVotarRemoto.verifica(associado.getCpf());
      if (UNABLE_TO_VOTE.equals(statusVerificacaoCPF)) {
        logger.info("Associado não pode votar: {}", associado);
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, "Associado não pode votar pois o CPF é inválido");
      }

    } catch (Exception e) {
      logger.error("Erro ao consultar serviço externo de validação de CPF", e);
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno, favor tentar novamente em instantes");
    }

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
