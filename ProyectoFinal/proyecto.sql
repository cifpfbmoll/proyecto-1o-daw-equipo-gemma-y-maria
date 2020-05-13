CREATE DATABASE programacion;
USE programacion;

DROP TABLE IF EXISTS USUARIO;
CREATE TABLE USUARIO (
  id int(11) NOT NULL,
  password varchar(30) NOT NULL,
  discriminador varchar(10) NOT NULL CHECK (discriminador = 'entrenador' or discriminador = 'alumno'),
  nombre varchar(30) DEFAULT NULL,
  apellido1 varchar(30) DEFAULT NULL,
  apellido2 varchar(30) DEFAULT NULL,
  DNI varchar(9) DEFAULT NULL,
  email varchar(50) DEFAULT NULL,
  telefono int DEFAULT NULL,
  direccion text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS EJERCICIO;
CREATE TABLE EJERCICIO (
  ex_code varchar(4) NOT NULL,
  nombre varchar(50) NOT NULL,
  descripcion text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS TIPO;
CREATE TABLE TIPO (
  tipo_code varchar(3) NOT NULL,
  nombre varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS TIPO_EJERCICIO;
CREATE TABLE TIPO_EJERCICIO (
  ej_code varchar(4) NOT NULL,
  tipo varchar(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS ENTRENAMIENTO;
CREATE TABLE ENTRENAMIENTO (
  train_code int NOT NULL,
  id_entrenador int(11) NOT NULL,
  id_alumno int(11) NOT NULL,
  fecha_creacion date
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS LINEA_ENTRENAMIENTO;
CREATE TABLE LINEA_ENTRENAMIENTO (
  line_num serial,
  codigo_entreno int NOT NULL,
  codigo_ejercicio varchar(4) NOT NULL,
  repeticiones int(3),
  tiempo_min int(3)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

ALTER TABLE USUARIO
  ADD PRIMARY KEY (id);

ALTER TABLE EJERCICIO
  ADD PRIMARY KEY (ex_code);

ALTER TABLE TIPO
  ADD PRIMARY KEY (tipo_code);

ALTER TABLE ENTRENAMIENTO
  ADD PRIMARY KEY (train_code),
  ADD CONSTRAINT train_ex_fk1 FOREIGN KEY (id_entrenador) REFERENCES USUARIO (id),
  ADD CONSTRAINT train_ex_fk2 FOREIGN KEY (id_alumno) REFERENCES USUARIO (id);


ALTER TABLE TIPO_EJERCICIO
  ADD PRIMARY KEY (ej_code,tipo),
  ADD CONSTRAINT tipo_ex_fk1 FOREIGN KEY (ej_code) REFERENCES EJERCICIO (ex_code),
  ADD CONSTRAINT tipo_ex_fk2 FOREIGN KEY (tipo) REFERENCES TIPO (tipo_code);

ALTER TABLE LINEA_ENTRENAMIENTO
  ADD PRIMARY KEY (line_num),
  ADD CONSTRAINT line_ex_fk1 FOREIGN KEY (codigo_entreno) REFERENCES ENTRENAMIENTO (train_code),
  ADD CONSTRAINT line_ex_fk2 FOREIGN KEY (codigo_ejercicio) REFERENCES EJERCICIO (ex_code);

INSERT INTO USUARIO (id, password, discriminador, nombre, apellido1, apellido2, DNI, email, telefono, direccion) VALUES
(001, 'usuario001', 'entrenador', 'Bruce', 'Wayne', 'Batman', '11111111A', 'brucewayne@gmail.com', 600000001, 'Mansion Wayne Road s/n Gotham City  KT13 8XQ' ),
(002, 'usuario002', 'entrenador', 'Clark', 'Kent', 'Superman', '22222222B', 'clarkkent@gmail.com', 600000002, 'Long Field St. 24 Kansas  KT13 8XL' ),
(003, 'usuario003', 'alumno', 'Edward', 'Nygma', 'Enigma', '33333333C', 'edwardnygma@gmail.com', 600000003, 'The Riddler St. 48 Gotham City  KH13 8XQ' ),
(004, 'usuario004', 'alumno', 'Alexander', 'Luthor', null, '44444444D', 'lexluthor@gmail.com', 600000004, 'Kryptonite Road 67 Metropolis  KT13 9XQ' ),
(005, 'usuario005', 'entrenador', 'Selina', 'Kyle', 'Catwoman', '55555555E', 'catwoman@gmail.com', 600000005, 'Rooftop Ave. 44 Gotham City  KH13 8XQ');

INSERT INTO EJERCICIO (ex_code, nombre, descripcion) VALUES
('DL', 'DEAD LIFT', 'Peso muerto con la barra olímpica desde el suelo'),
('CL', 'CLEAN & JERK', 'Arrancada desde el suelo con la barra olímpica acabando con un split'),
('PU', 'PULL UPS', 'Peso muerto con la barra olímpica desde el suelo'),
('LE', 'LEG EXTENSION', 'Extensiones de pierna'),
('WB', 'WALL BALLS', 'Lazamiento de pelota de 5kg a la linea de la pared'),
('HP', 'HAND STAND PUSH UPS', 'Flexiones haciendo el pino'),
('CP', 'COBRA POSITION', 'Tumbado boca abajo flexionando la espalda atrás con apoyo de brazos y piernas estirados'),
('GP', 'WARRIOR POSITION', 'Pierna izquierda en tensión, peso en pierna derecha; brazos estirados'),
('DP', 'DOG POSITION', 'V invertida con sólo manos y pies de apoyo.');

INSERT INTO TIPO (tipo_code, nombre) VALUES
('WF', 'WEIGHT_LIFTING'),
('YG', 'YOGA'),
('RH', 'REHABILITATION'),
('GM', 'GYMNASTICS'),
('ST', 'STRONGMAN');

INSERT INTO TIPO_EJERCICIO (ej_code, tipo) VALUES
('CL', 'WF'),
('PU', 'GM'),
('HP', 'ST'),
('HP', 'WF'),
('CP', 'YG'),
('GP', 'YG'),
('DP', 'YG');

INSERT INTO ENTRENAMIENTO (train_code, id_entrenador, id_alumno, fecha_creacion) VALUES
(1, 001, 003, '2020-05-10'),
(2, 005, 001, '2020-05-11'),
(3, 001, 004, '2020-05-11'),
(4, 002, 004, '2020-05-12');

INSERT INTO LINEA_ENTRENAMIENTO (codigo_entreno, codigo_ejercicio, repeticiones, tiempo_min) VALUES
(1, 'PU', 10, 4),
(1, 'LE', 20, null),
(2, 'CP', null, 3),
(2, 'GP', null, 5),
(2, 'DP', null, 5),
(2, 'CP', null, 2),
(3, 'PU', 10, 4),
(3, 'LE', 20, null),
(4, 'PU', 100, null);
