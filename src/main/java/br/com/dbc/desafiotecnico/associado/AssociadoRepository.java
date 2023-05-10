package br.com.dbc.desafiotecnico.associado;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AssociadoRepository extends JpaRepository<Associado, Long> {
  boolean existsByCpf(String cpf);
}
