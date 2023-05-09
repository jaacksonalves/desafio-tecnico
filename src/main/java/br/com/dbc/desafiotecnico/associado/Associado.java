package br.com.dbc.desafiotecnico.associado;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Entity
@Table(name = "associados")
public class Associado {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nome;

  @Column(unique = true)
  private String cpf;

  private LocalDateTime instanteCriacao = LocalDateTime.now();

  public Associado(String nome, String cpf) {
    /*
    jackson: Asserts inseridos como forma de programação defensiva a evitar que um objeto de domínio seja criado em um estado inválido.
     */
    Assert.state(StringUtils.hasText(nome), "Nome é obrigatório");
    Assert.state(StringUtils.hasText(cpf), "CPF é obrigatório");

    this.nome = nome;
    this.cpf = cpf;
  }

  @Deprecated(since = "1.0")
  /**
   * @deprecated hibernate only
   */
  public Associado() {}

  public Long getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    var associado = (Associado) o;
    return Objects.equals(cpf, associado.cpf);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cpf);
  }
}
