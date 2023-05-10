# API para Gerenciamento de Sessões de Votação em Cooperativas

Esta é uma solução back-end que gerencia sessões de votação em cooperativas. A API REST foi desenvolvida para ser
executada na nuvem e oferece as seguintes funcionalidades:

- Cadastrar uma nova pauta;
- Abrir uma sessão de votação em uma pauta (a sessão de votação pode ficar aberta por um tempo determinado na chamada de
  abertura ou 1 minuto por padrão);
- Receber votos dos associados em pautas (os votos são apenas 'Sim'/'Não'. Cada associado é identificado por um ID único
  e pode votar apenas uma vez por pauta);
- Contabilizar os votos e dar o resultado da votação na pauta.

## Configuração

Para executar a API, é necessário ter Docker instalado na máquina. Para iniciar o banco basta rodar:

```bash
docker-compose up -d
```

Os testes rodam a partir de TestContainers, basta rodá-los com o Docker aberto no PC.