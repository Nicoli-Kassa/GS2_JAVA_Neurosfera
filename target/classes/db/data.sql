-- ============================================
--  DADOS INICIAIS - NEUROSFERA
-- ============================================

-- ============================================
--  INSERIR DISTRITOS PADRÃO
-- ============================================
INSERT INTO DISTRITOS (NM_DISTRITO, AREA_DISTRITO, DS_DISTRITO, ST_DISTRITO) VALUES
('Distrito da Saúde', 'Medicina', 'Aprenda cirurgia, diagnóstico e medicina em simulações VR ultrarrealistas com feedback de IA em tempo real.', TRUE),
('Distrito da Engenharia', 'Engenharia', 'Construa, projete e teste estruturas complexas em ambientes 3D imersivos. Desde pontes até naves espaciais.', TRUE),
('Distrito da Criatividade', 'Design', 'Espaço cósmico onírico onde você materializa ideias com voz e gestos. Design, arte e inovação sem limites.', TRUE),
('Distrito da IA', 'Inteligência Artificial', 'Desenvolva algoritmos, treine modelos e experimente com machine learning em cenários práticos.', TRUE),
('Distrito da Sustentabilidade', 'Sustentabilidade', 'Projete cidades verdes, sistemas de energia limpa e soluções para crise climática.', TRUE),
('Distrito dos Negócios', 'Negócios', 'Simulações de gestão empresarial, estratégia, marketing e empreendedorismo digital.', TRUE),
('Distrito de Dados', 'Ciência de Dados', 'Análise de Big Data, visualizações interativas e tomada de decisão baseada em dados.', TRUE),
('Distrito da Logística', 'Logística', 'Otimize cadeias de suprimento, gerencie armazéns inteligentes e rotas de distribuição global.', TRUE);

-- ============================================
--  INSERIR USUÁRIOS DE TESTE
--  ATENÇÃO: Senhas em texto plano apenas para desenvolvimento!
--  Em produção, use BCrypt ou similar
-- ============================================
INSERT INTO USUARIOS (NM_USUARIO, EMAIL_USUARIO, SENHA_USUARIO, TP_USUARIO, DT_CADASTRO_USUARIO, IDIOMA_USUARIO, ST_USUARIO) VALUES
('Admin Neurosfera', 'admin@neurosfera.com', 'senha123', 'ADMINISTRADOR', CURRENT_DATE, 'PT-BR', TRUE),
('João Silva', 'joao.silva@email.com', 'senha123', 'APRENDIZ', CURRENT_DATE, 'PT-BR', TRUE),
('Maria Santos', 'maria.santos@email.com', 'senha123', 'INSTRUTOR', CURRENT_DATE, 'PT-BR', TRUE),
('Pedro Costa', 'pedro.costa@email.com', 'senha123', 'CRIADOR', CURRENT_DATE, 'EN', TRUE),
('Ana Oliveira', 'ana.oliveira@email.com', 'senha123', 'APRENDIZ', CURRENT_DATE, 'PT-BR', TRUE),
('Tech Corp', 'contato@techcorp.com', 'senha123', 'EMPRESA', CURRENT_DATE, 'EN', TRUE);

-- ============================================
--  INSERIR INSCRIÇÕES EM DISTRITOS
-- ============================================
INSERT INTO USUARIO_DISTRITO (ID_USUARIO, ID_DISTRITO, DT_INSCRICAO) VALUES
(2, 1, CURRENT_DATE), -- João Silva -> Distrito da Saúde
(2, 2, CURRENT_DATE), -- João Silva -> Distrito da Engenharia
(3, 1, CURRENT_DATE), -- Maria Santos -> Distrito da Saúde
(4, 3, CURRENT_DATE), -- Pedro Costa -> Distrito da Criatividade
(4, 4, CURRENT_DATE), -- Pedro Costa -> Distrito da IA
(5, 1, CURRENT_DATE), -- Ana Oliveira -> Distrito da Saúde
(5, 3, CURRENT_DATE), -- Ana Oliveira -> Distrito da Criatividade
(6, 6, CURRENT_DATE), -- Tech Corp -> Distrito dos Negócios
(6, 7, CURRENT_DATE); -- Tech Corp -> Distrito de Dados

