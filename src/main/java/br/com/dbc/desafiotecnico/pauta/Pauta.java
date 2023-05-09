package br.com.dbc.desafiotecnico.pauta;

import jakarta.persistence.*;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Entity
@Table(name = "pautas")
public class Pauta {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String titulo;
  private String descricao;

  public Pauta(String titulo, String descricao) {
    Assert.state(StringUtils.hasText(titulo), "Título é obrigatório");
    Assert.state(StringUtils.hasText(descricao), "Descrição é obrigatória");

    this.titulo = titulo;
    this.descricao = descricao;
  }

  @Deprecated(since = "JPA ONLY")
  /**
   * @deprecated jpa only
   */
  public Pauta() {}

  public Long getId() {
    return id;
  }
}
