CREATE TABLE IF NOT EXISTS public.emprestimos
(
    data_emprestimo date,
    quantidade_parcelas integer NOT NULL,
    valor_parcela numeric(8,2) NOT NULL,
    cpf character varying(11) COLLATE pg_catalog."default" NOT NULL,
    id_emprestimo uuid NOT NULL,
    status character varying(20) COLLATE pg_catalog."default",
    CONSTRAINT emprestimos_pkey PRIMARY KEY (id_emprestimo),
    CONSTRAINT emprestimos_status_check CHECK (status::text = ANY (ARRAY['Ativo'::character varying, 'Concluido'::character varying]::text[]))
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.emprestimos
    OWNER to test;