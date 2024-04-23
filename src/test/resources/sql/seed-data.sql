INSERT INTO "instituicoes" ("id", "cnpj", "chave_acesso", "nome") VALUES
    (1, '01521799000124', 'bsf/ZO863q5jXPXQeUEDr7MjkRbkXt1s2Ut7oE/lj3g=', 'Banco do Brasil');


INSERT INTO "emprestimos" ("id_emprestimo", "cpf", "valor_parcela", "quantidade_parcelas", "data_emprestimo", status, instituicao_id) VALUES
('fa81f244-d11d-47ed-946d-e0165482cb1e'::UUID, '07304600020', 123.25, 12, '2024-02-14', 'Ativo', 1),
('37751763-9807-499c-882c-69b729fdc5f8'::UUID, '44328630059', 420.50, 12, '2024-08-02', 'Ativo', 1),
('f931354c-6fc8-4147-8194-678d28bd8271'::UUID, '91501728083', 720.80, 24, '2023-11-09', 'Ativo', 1),
('47d69d68-668f-4757-b4f9-bb9f57172da3'::UUID, '55135779094', 540.00, 32, '2024-02-10', 'Ativo', 1),
('bbb7e06b-c178-4b5b-9220-8948edd98901'::UUID, '71418129038', 250.00, 12, '2022-04-28', 'Concluido', 1),
('8d65070b-0999-48b5-86ca-d950d2a94c9d'::UUID, '71418129038', 300.00, 12, '2022-04-20', 'Ativo', 1),
('efb7cf3e-db20-412b-9360-45a4c3b23fba'::UUID, '71418129038', 200.00, 8,  '2022-03-28', 'Ativo', 1),
('6787b214-ee87-4898-a650-d2b3ea19577e'::UUID, '45164269007', 250.00, 24, '2023-10-21', 'Ativo', 1);

