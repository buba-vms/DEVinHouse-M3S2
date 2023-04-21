-- Arquivo que contêm inserts para carga inicial de dados para a aplicação

-- Insert na tabela de roles
INSERT INTO public.role(nome_role) VALUES ('ROLE_ADMINISTRADOR');
INSERT INTO public.role(nome_role) VALUES ('ROLE_CADASTRADOR');
INSERT INTO public.role(nome_role) VALUES ('ROLE_COLABORADOR');

-- Insert na tabela de usuários
INSERT INTO public.usuario(login, nome, senha) VALUES ('brunomoura', 'Bruno Moura', '$2a$10$pFrtqxDwOYw0rFq1VQtEx.pBgN6A8tUlVzfPnCs3Aior5NXmMes1m');
INSERT INTO public.usuario(login, nome, senha) VALUES ('danielamartins', 'Daniela Martins', '$2a$10$1/tzkPLEbpU/FQWKcp9C5u19nARxtc.3Zge8LPWkatj/ub922CAaa');
INSERT INTO public.usuario(login, nome, senha) VALUES ('colaborador', 'Colaborador', '$2a$10$1/tzkPLEbpU/FQWKcp9C5u19nARxtc.3Zge8LPWkatj/ub922CAaa');

-- Insert na tabela de usuarios_role (associa o(s) usuário(s) com suas permissões)
INSERT INTO public.usuarios_role(usuario_id, role_id) VALUES (1, 1);
INSERT INTO public.usuarios_role(usuario_id, role_id) VALUES (1, 2);
INSERT INTO public.usuarios_role(usuario_id, role_id) VALUES (2, 2);
INSERT INTO public.usuarios_role(usuario_id, role_id) VALUES (3, 3);
