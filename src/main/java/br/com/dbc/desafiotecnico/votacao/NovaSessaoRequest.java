package br.com.dbc.desafiotecnico.votacao;

import br.com.dbc.desafiotecnico.pauta.Pauta;
import jakarta.validation.constraints.Future;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

public record NovaSessaoRequest(
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss") @Future LocalDateTime dataEncerramento) {
  public Sessao toModel(Pauta pauta, int minutoPadraoCasoNaoInformado) {
    var dataEncerramento =
        this.dataEncerramento != null
            ? this.dataEncerramento
            : LocalDateTime.now().plusMinutes(minutoPadraoCasoNaoInformado);
    return new Sessao(pauta, dataEncerramento);
  }
}
