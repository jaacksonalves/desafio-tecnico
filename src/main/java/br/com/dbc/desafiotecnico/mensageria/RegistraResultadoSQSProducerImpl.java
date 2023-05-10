package br.com.dbc.desafiotecnico.mensageria;

import org.springframework.stereotype.Service;

@Service
public class RegistraResultadoSQSProducerImpl implements RegistraResultadoSQSProducer {
  @Override
  public void send(String message) {
    // lógica de enviar ao mensageiro
  }
}
