package br.com.dbc.desafiotecnico.votacao;

import br.com.dbc.desafiotecnico.associado.Associado;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import org.springframework.util.Assert;

@Entity
@Table(name = "votos")
public class Voto {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull @ManyToOne private Sessao sessao;
  @NotNull @ManyToOne private Associado associado;

  @NotNull
  @Enumerated(EnumType.STRING)
  private TipoVoto tipoVoto;

  public Voto(Sessao sessao, Associado associado, TipoVoto tipoVoto) {
    /*
    jackson: Asserts inseridos como forma de programação defensiva a evitar que um objeto de domínio seja criado
     em um estado inválido.
     */
    Assert.state(Objects.nonNull(sessao), "Sessão é obrigatória");
    Assert.state(Objects.nonNull(associado), "Associado é obrigatório");
    Assert.state(Objects.nonNull(tipoVoto), "Tipo de voto é obrigatório");

    this.sessao = sessao;
    this.associado = associado;
    this.tipoVoto = tipoVoto;
  }

  @Deprecated(since = "1.0")
  /**
   * @deprecated hibernate only
   */
  public Voto() {}

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    var voto = (Voto) o;
    return Objects.equals(sessao, voto.sessao) && Objects.equals(associado, voto.associado);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sessao, associado);
  }

  public Long getId() {
    return id;
  }

  public boolean pertenceAoAssociado(Associado associado) {
    return this.associado.equals(associado);
  }

  public boolean isVotoSim() {
    return this.tipoVoto.equals(TipoVoto.SIM);
  }
}
