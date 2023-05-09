package br.com.dbc.desafiotecnico.associado;

import jakarta.persistence.*;
import java.time.Instant;
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

  private Instant instanteCriacao = Instant.now();

  public Associado(String nome, String cpf) {
    /*
    jackson: Asserts inseridos como forma de programação defensiva a evitar que um objeto de domínio seja criado em um estado inválido.
     */
    Assert.state(StringUtils.hasText(nome), "Nome é obrigatório");
    Assert.state(StringUtils.hasText(cpf), "CPF é obrigatório");

    this.nome = nome;
    this.cpf = cpf;
  }

  @Deprecated(since = "JPA ONLY")
  /**
   * @deprecated jpa only
   */
  public Associado() {}

  public Long getId() {
    return id;
  }
}
