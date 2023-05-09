package br.com.dbc.desafiotecnico.associado;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;

public record NovoAssociadoRequest(
    @NotBlank @Length(max = 255) String nome, @CPF @NotNull @Length(max = 11) String cpf) {
  public Associado toModel() {
    return new Associado(nome, cpf);
  }
}
