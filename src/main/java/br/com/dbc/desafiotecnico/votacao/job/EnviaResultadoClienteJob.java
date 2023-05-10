package br.com.dbc.desafiotecnico.votacao.job;

import br.com.dbc.desafiotecnico.mensageria.RegistraResultadoSQSProducer;
import br.com.dbc.desafiotecnico.votacao.SessaoRepository;
import java.time.LocalDateTime;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/** jackson: Job que envia o resultado da votação para o cliente */
@Component
public class EnviaResultadoClienteJob {

  private final RegistraResultadoSQSProducer registraResultadoSQSProducer;
  private final SessaoRepository sessaoRepository;

  public EnviaResultadoClienteJob(
      RegistraResultadoSQSProducer registraResultadoSQSProducer,
      SessaoRepository sessaoRepository) {
    this.registraResultadoSQSProducer = registraResultadoSQSProducer;
    this.sessaoRepository = sessaoRepository;
  }

  @Scheduled(fixedDelay = 60000)
  public void execute() {
    var sessoesEncerradas = sessaoRepository.buscaSessoesEncerradasAgora(LocalDateTime.now());

    sessoesEncerradas.forEach(
        sessao -> {
          var mensagem =
              String.format(
                  "Sessão de id: %s; Resultado: %s", sessao.getId(), sessao.getResultadoVotacao());
          registraResultadoSQSProducer.send(mensagem);
        });
  }
}
