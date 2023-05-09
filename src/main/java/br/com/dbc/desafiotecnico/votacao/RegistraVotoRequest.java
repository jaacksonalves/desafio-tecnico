package br.com.dbc.desafiotecnico.votacao;

import jakarta.validation.constraints.NotNull;

public record RegistraVotoRequest(@NotNull TipoVoto tipoVoto) {}
