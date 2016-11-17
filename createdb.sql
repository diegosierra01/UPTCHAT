--CREACION TABLA USUARIO
CREATE TABLE USUARIO (
		idusuario      VARCHAR(10)  NOT NULL, 
		nick           VARCHAR(45)  NOT NULL,
		password       VARCHAR(50)  NOT NULL,
		CONSTRAINT usu_pk_idu PRIMARY KEY (idusuario)
);

--CREACION TABLA GRUPO
CREATE TABLE GRUPO (
		idgrupo        VARCHAR(10)  NOT NULL, 
		nombre         VARCHAR(10)  NOT NULL,
		CONSTRAINT gru_pk_idg PRIMARY KEY (idgrupo)
);

--CREACION TABLA MENSAJE
CREATE TABLE MENSAJE (
		idmensaje      INT   	     NOT NULL AUTO_INCREMENT, 
		cadena         VARCHAR(255) NOT NULL,
		destinatario   VARCHAR(10)       NOT NULL,
		fecha          TIMESTAMP     DEFAULT LOCALTIME,
		remitente      VARCHAR(10)  NOT NULL,
		CONSTRAINT men_pk_idm PRIMARY KEY (idmensaje)
);

--CREACION TABLA USUARIO_GRUPO
CREATE TABLE USUARIO_GRUPO (
	idgrupo      	 VARCHAR(10)   NOT NULL,
	idusuario      	 VARCHAR(10)   NOT NULL
);

--REGLAS DE NEGOCIO TABLA USUARIO
ALTER TABLE USUARIO ADD UNIQUE INDEX (nick ASC);

--REGLAS DE NEGOCIO TABLA MENSAJE
ALTER TABLE MENSAJE ADD(
	CONSTRAINT men_fk_idu FOREIGN KEY (remitente) REFERENCES USUARIO(idusuario)
);

--REGLAS DE NEGOCIO TABLA GRUPO
ALTER TABLE GRUPO ADD UNIQUE INDEX (nombre ASC);

--REGLAS DE NEGOCIO TABLA USUARIO_GRUPO
ALTER TABLE USUARIO_GRUPO ADD(
	CONSTRAINT usug_fk_idg FOREIGN KEY (idgrupo) REFERENCES GRUPO(idgrupo),
	CONSTRAINT usug_fk_idu FOREIGN KEY (idusuario) REFERENCES USUARIO(idusuario)
);

--INSERCION A TABLA GRUPO

INSERT INTO GRUPO (idgrupo, nombre) VALUES
('G1', 'grupo1'),
('G2', 'grupo2'),
('G3', 'grupo3');

--INSERCION TABLA USUARIO

INSERT INTO USUARIO (idusuario, nick, password) VALUES
('U1', 'loco', 'a123'),
('U2', 'Emili', 'a123'),
('U3', 'jose', 'qw13'),
('U4', 'dieho', '1234'),
('U5', 'hugo', '1234567');

--INSERCION TABLA USUARIO_GRUPO

INSERT INTO USUARIO_GRUPO (idgrupo, idusuario) VALUES
('G1', 'U5'),
('G2', 'U4'),
('G3', 'U5'),
('G2', 'U2');

--INSERCION A TABLA MENSAJE

INSERT INTO MENSAJE (idmensaje, cadena, destinatario, fecha, remitente) VALUES
(1, 'msj quemado', 'U1', CURRENT_TIMESTAMP, 'U2'),
(2, 'Hola', 'U1', CURRENT_TIMESTAMP, 'U2'),
(3, 'sin embargo', 'U2', CURRENT_TIMESTAMP, 'U1'),
(5, 'Perro', 'U1', CURRENT_TIMESTAMP, 'U1'),
(6, 'Gato', 'U1', CURRENT_TIMESTAMP, 'U2'),
(7, 'Hola XYZ', 'U1', CURRENT_TIMESTAMP, 'U2'),
(11, 'nada', 'U1', CURRENT_TIMESTAMP, 'U2'),
(13, 'Hi', 'U1', CURRENT_TIMESTAMP, 'U2'),
(14, 'buenas', 'U1', CURRENT_TIMESTAMP, 'U1');