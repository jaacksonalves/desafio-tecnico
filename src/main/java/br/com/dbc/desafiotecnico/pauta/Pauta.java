package br.com.dbc.desafiotecnico.pauta;

import br.com.dbc.desafiotecnico.votacao.NovaSessaoRequest;
import br.com.dbc.desafiotecnico.votacao.Sessao;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.validation.constraints.NotBlank;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Entity
@Table(name = "pautas")
public class Pauta {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  private String titulo;

  @NotBlank
  private String descricao;

  @OneToOne(mappedBy = "pauta")
  private Sessao sessao;

  private LocalDateTime instanteCriacao = LocalDateTime.now();

  public Pauta(String titulo, String descricao) {
    /*
    jackson: Asserts inseridos como forma de programação defensiva a evitar que um objeto de domínio seja criado em um estado inválido.
     */
    Assert.state(StringUtils.hasText(titulo), "Título é obrigatório");
    Assert.state(StringUtils.hasText(descricao), "Descrição é obrigatória");

    this.titulo = titulo;
    this.descricao = descricao;
  }

  @Deprecated(since = "1.0")
  /**
   * @deprecated hibernate only
   */
  public Pauta() {}

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    var pauta = (Pauta) o;
    return Objects.equals(titulo, pauta.titulo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(titulo);
  }

  public Long getId() {
    return id;
  }

  public String getTitulo() {
    return this.titulo;
  }

  public Sessao abreSessao(NovaSessaoRequest request) {
    this.sessao = request.toModel(this);
    return this.sessao;
  }
}
