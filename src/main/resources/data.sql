-- Garante que o usuário Admin exista e atenda aos requisitos (senha > 10 chars)
INSERT INTO usuarios (nome, email, cpf, matricula, senha, tipo_de_conta, status, data_nascimento)
SELECT 'Admin Bibliotecário',
       'admin@scripta.com',
       '00000000000',
       'admin',
       '$2a$12$T5U3MDGdtOO0IwlDH//Oy.msRvpmxrXbFg1cpZoC94wFc/n2K17mG',
       'BIBLIOTECARIO',
       'ATIVO',
       '2000-01-01'
    WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE matricula = 'admin');