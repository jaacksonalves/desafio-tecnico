package br.com.dbc.desafiotecnico.pauta;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record NovaPautaRequest(
    @NotBlank @Length(max = 255) String titulo, @Length(max = 255) @NotBlank String descricao) {
  public Pauta toModel() {
    return new Pauta(titulo, descricao);
  }
}
