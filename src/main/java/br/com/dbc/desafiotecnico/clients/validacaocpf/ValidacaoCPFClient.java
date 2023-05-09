package br.com.dbc.desafiotecnico.clients.validacaocpf;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "validacao-cpf", url = "{$aplicacao.host-validacao-cpf}")
public interface ValidacaoCPFClient {

  @GetMapping("/users/{cpf}")
  ValidaCPFResponse validarCPF(@PathVariable String cpf);
}
