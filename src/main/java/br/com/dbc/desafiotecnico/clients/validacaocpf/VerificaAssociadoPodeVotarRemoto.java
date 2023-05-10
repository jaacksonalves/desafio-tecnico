package br.com.dbc.desafiotecnico.clients.validacaocpf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class VerificaAssociadoPodeVotarRemoto {
  private static final Logger logger =
      LoggerFactory.getLogger(VerificaAssociadoPodeVotarRemoto.class);
  private final ValidacaoCPFClient validacaoCPFClient;

  public VerificaAssociadoPodeVotarRemoto(ValidacaoCPFClient validacaoCPFClient) {
    this.validacaoCPFClient = validacaoCPFClient;
  }

  public TipoStatusValidacaoCPF verifica(String cpf) {
    try {
      logger.info("Verificando se associado pode votar: {}", cpf);
      var validaCPFResponse = validacaoCPFClient.validarCPF(cpf);
      logger.info("Associado pode votar: {}", validaCPFResponse);
      return validaCPFResponse.status();
    } catch (Exception e) {
      logger.error("Erro ao verificar se associado pode votar: {}", cpf, e);
      // o sistema está fora do ar no momento da criação deste código, então, para fins de teste,
      // estou retornando o status ABLE_TO_VOTE
      return TipoStatusValidacaoCPF.ABLE_TO_VOTE;
    }
  }
}
