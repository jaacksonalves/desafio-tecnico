package br.com.dbc.desafiotecnico.clients.validacaocpf;

import org.springframework.stereotype.Service;

@Service
public class VerificaAssociadoPodeVotarRemoto {
  private final ValidacaoCPFClient validacaoCPFClient;

  public VerificaAssociadoPodeVotarRemoto(ValidacaoCPFClient validacaoCPFClient) {
    this.validacaoCPFClient = validacaoCPFClient;
  }

  public TipoStatusValidacaoCPF verifica(String cpf) {
    var validaCPFResponse = validacaoCPFClient.validarCPF(cpf);
    return validaCPFResponse.status();
  }
}
