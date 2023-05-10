package br.com.dbc.desafiotecnico.pauta;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PautaRepository extends JpaRepository<Pauta, Long> {
  boolean existsByTitulo(String titulo);
}
