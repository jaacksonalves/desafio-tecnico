package br.com.dbc.desafiotecnico.votacao;

public record ResultadoVotacaoResponse(
    String tituloDaPauta, TipoResultadoVotacao resultadoVotacao) {

  public ResultadoVotacaoResponse(Sessao sessao) {
    this(sessao.getTituloPauta(), sessao.getResultadoVotacao());
  }
}
