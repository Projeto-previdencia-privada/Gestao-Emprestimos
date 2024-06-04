CREATE TABLE IF NOT EXISTS public.instituicoes
(
    id int,
    cnpj         varchar(14) UNIQUE,
    chave_acesso varchar(255),
    nome         varchar(100),
    nome_imagem character varying(255),
    CONSTRAINT instituicoes_pkey PRIMARY KEY (id)
);

alter table instituicoes
    owner to test;

CREATE TABLE IF NOT EXISTS public.emprestimos
(
    data_emprestimo date,
    quantidade_parcelas integer NOT NULL,
    valor_parcela numeric(8,2) NOT NULL,
    cpf character varying(11) COLLATE pg_catalog."default" NOT NULL,
    id_emprestimo uuid NOT NULL,
    status character varying(20) COLLATE pg_catalog."default",
    instituicao_id bigint NOT NULL,
    CONSTRAINT emprestimos_pkey PRIMARY KEY (id_emprestimo),
    CONSTRAINT emprestimos_status_check CHECK (status::text = ANY (ARRAY['Ativo'::character varying, 'Concluido'::character varying]::text[])),
    CONSTRAINT emprestimos_fk FOREIGN KEY (instituicao_id) REFERENCES instituicoes(id) ON DELETE CASCADE
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.emprestimos
    OWNER to test;



