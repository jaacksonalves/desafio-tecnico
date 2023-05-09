package br.com.dbc.desafiotecnico.votacao;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SessaoRepository extends JpaRepository<Sessao, Long> {
  @Query("SELECT s FROM Sessao s WHERE s.instanteEncerramento <= ?1")
  List<Sessao> buscaSessoesEncerradasAgora(LocalDateTime agora);
}
