package br.com.dbc.desafiotecnico.mensageria;

public interface RegistraResultadoSQSProducer {

  void send(String message);
}
