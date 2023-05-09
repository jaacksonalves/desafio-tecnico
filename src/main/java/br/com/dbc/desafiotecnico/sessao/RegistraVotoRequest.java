package br.com.dbc.desafiotecnico.sessao;

import jakarta.validation.constraints.NotNull;

public record RegistraVotoRequest(@NotNull TipoVoto tipoVoto) {}
