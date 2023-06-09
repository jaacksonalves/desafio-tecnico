package br.com.dbc.desafiotecnico.votacao;

import br.com.dbc.desafiotecnico.associado.Associado;
import br.com.dbc.desafiotecnico.pauta.Pauta;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.springframework.util.Assert;

/** jackson: Classe representa uma sessão de votação de uma {@link Pauta}. */
@Entity
@Table(name = "sessoes")
public class Sessao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull @OneToOne private Pauta pauta;

  @OneToMany(
      mappedBy = "sessao",
      cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private Set<Voto> votos = new HashSet<>();

  private LocalDateTime instanteCriacao = LocalDateTime.now();
  @Future private LocalDateTime instanteEncerramento;

  public Sessao(Pauta pauta, LocalDateTime instanteEncerramento) {
    /*
    jackson: Asserts inseridos como forma de programação defensiva a evitar que um objeto de domínio seja criado
    em um estado inválido.
     */
    Assert.state(Objects.nonNull(pauta), "Pauta é obrigatória");
    Assert.state(Objects.nonNull(instanteEncerramento), "Instante de encerramento é obrigatório");
    Assert.state(
        instanteEncerramento.isAfter(LocalDateTime.now()), "Instante de encerramento é inválido");

    this.pauta = pauta;
    this.instanteEncerramento = instanteEncerramento;
  }

  @Deprecated(since = "1.0")
  /**
   * @deprecated hibernate only
   */
  public Sessao() {}

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    var sessao = (Sessao) o;
    return Objects.equals(pauta, sessao.pauta);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pauta);
  }

  public Long getId() {
    return id;
  }

  public String getTituloPauta() {
    return this.pauta.getTitulo();
  }

  public boolean isAberta() {
    return instanteEncerramento.isAfter(LocalDateTime.now());
  }

  public boolean jaVotou(Associado associado) {
    return this.votos.stream().anyMatch(voto -> voto.pertenceAoAssociado(associado));
  }

  public Voto registraVoto(TipoVoto tipoVoto, Associado associado) {
    Assert.state(isAberta(), "Sessão já encerrada, por isso não pode mais receber votos");
    var novoVoto = new Voto(this, associado, tipoVoto);
    Assert.state(this.votos.add(novoVoto), "Associado provavelmente já votou nesta sessão");
    return novoVoto;
  }

  public TipoResultadoVotacao getResultadoVotacao() {
    Assert.state(
        !this.isAberta(), "Não é possível coletar o resultado antes do término da votação");

    var totalVotos = this.votos.size();
    var totalVotosSim = this.votos.stream().filter(Voto::isVotoSim).count();
    var totalVotosNao = totalVotos - totalVotosSim;

    if (totalVotosSim == totalVotosNao) {
      return TipoResultadoVotacao.EMPATE;
    }
    return totalVotosSim > totalVotosNao
        ? TipoResultadoVotacao.APROVADO
        : TipoResultadoVotacao.REPROVADO;
  }

  public List<Voto> getVotos() {
    return List.copyOf(this.votos);
  }
}
