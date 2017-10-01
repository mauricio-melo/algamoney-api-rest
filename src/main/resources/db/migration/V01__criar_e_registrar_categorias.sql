CREATE TABLE categoria(
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO categoria (nome) VALUES ('Biblioteca');
INSERT INTO categoria (nome) VALUES ('Loja');
INSERT INTO categoria (nome) VALUES ('Bar');
INSERT INTO categoria (nome) VALUES ('Balada');