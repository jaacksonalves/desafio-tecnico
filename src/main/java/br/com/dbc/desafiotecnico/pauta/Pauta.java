package br.com.dbc.desafiotecnico.pauta;

import jakarta.persistence.*;
import java.time.Instant;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Entity
@Table(name = "pautas")
public class Pauta {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String titulo;

  private String descricao;

  private Instant instanteCriacao = Instant.now();

  public Pauta(String titulo, String descricao) {
    /*
    jackson: Asserts inseridos como forma de programação defensiva a evitar que um objeto de domínio seja criado em um estado inválido.
     */
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
