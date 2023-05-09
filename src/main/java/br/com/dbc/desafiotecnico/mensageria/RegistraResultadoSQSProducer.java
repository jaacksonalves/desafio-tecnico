package br.com.dbc.desafiotecnico.mensageria;

import org.springframework.stereotype.Service;
//adicionando @Service aqui pois não implementei a Interface ainda, apenas para simular
@Service
public interface RegistraResultadoSQSProducer {

  void send(String message);
}
