CREATE TABLE associados
(
    id               BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    nome             VARCHAR(255)                            NOT NULL,
    cpf              VARCHAR(11)                             NOT NULL,
    instante_criacao TIMESTAMP                               NOT NULL,
    CONSTRAINT pk_associados PRIMARY KEY (id)
);

CREATE TABLE pautas
(
    id               BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    titulo           VARCHAR(255)                            NOT NULL,
    descricao        VARCHAR(255)                            NOT NULL,
    instante_criacao TIMESTAMP                               NOT NULL,
    CONSTRAINT pk_pautas PRIMARY KEY (id)
);

CREATE TABLE sessoes
(
    id                    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    pauta_id              BIGINT                                  NOT NULL,
    instante_criacao      TIMESTAMP                               NOT NULL,
    instante_encerramento TIMESTAMP                               NOT NULL,
    CONSTRAINT pk_sessoes PRIMARY KEY (id)
);

CREATE TABLE votos
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    sessao_id    BIGINT                                  NOT NULL,
    associado_id BIGINT                                  NOT NULL,
    tipo_voto    VARCHAR(255),
    CONSTRAINT pk_votos PRIMARY KEY (id)
);

ALTER TABLE associados
    ADD CONSTRAINT uc_associados_cpf UNIQUE (cpf);

ALTER TABLE pautas
    ADD CONSTRAINT uc_pautas_titulo UNIQUE (titulo);

ALTER TABLE sessoes
    ADD CONSTRAINT uc_sessoes_pauta_id UNIQUE (pauta_id);

ALTER TABLE sessoes
    ADD CONSTRAINT FK_SESSOES_ON_PAUTA FOREIGN KEY (pauta_id) REFERENCES pautas (id);

ALTER TABLE votos
    ADD CONSTRAINT FK_VOTOS_ON_ASSOCIADO FOREIGN KEY (associado_id) REFERENCES associados (id);

ALTER TABLE votos
    ADD CONSTRAINT FK_VOTOS_ON_SESSAO FOREIGN KEY (sessao_id) REFERENCES sessoes (id);