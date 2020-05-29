CREATE DATABASE programacion;
USE programacion;

DROP TABLE IF EXISTS USUARIO;
CREATE TABLE USUARIO (
  DNI varchar(9) NOT NULL,
  password varchar(30) NOT NULL,
  discriminador varchar(10) NOT NULL CHECK (discriminador = 'entrenador' or discriminador = 'alumno'),
  nombre varchar(30) DEFAULT NULL,
  apellido1 varchar(30) DEFAULT NULL,
  apellido2 varchar(30) DEFAULT NULL,
  email varchar(50) DEFAULT NULL,
  telefono int DEFAULT NULL,
  direccion text DEFAULT NULL,
  IBAN varchar(22) DEFAULT NULL,
  num_prog_prep int DEFAULT NULL,
  tipo_prog_solicitado varchar(3) NULL
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
  train_code serial NOT NULL,
  dni_entrenador varchar(9)  NOT NULL,
  dni_alumno varchar(9)  NOT NULL,
  tipo_programa varchar(3) NOT NULL,
  fecha_creacion date
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS LINEA_ENTRENAMIENTO;
CREATE TABLE LINEA_ENTRENAMIENTO (
  line_num serial,
  codigo_entreno BIGINT UNSIGNED,
  codigo_ejercicio varchar(4) NOT NULL,
  repeticiones int(3),
  tiempo_min int(3)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

ALTER TABLE TIPO
  ADD PRIMARY KEY (tipo_code);

ALTER TABLE EJERCICIO
  ADD PRIMARY KEY (ex_code);

ALTER TABLE USUARIO
  ADD PRIMARY KEY (DNI),
  ADD CONSTRAINT tipo_prog_fk1 FOREIGN KEY (tipo_prog_solicitado) REFERENCES TIPO (tipo_code);

ALTER TABLE ENTRENAMIENTO
  ADD PRIMARY KEY (train_code),
  ADD CONSTRAINT train_ex_fk1 FOREIGN KEY (dni_entrenador) REFERENCES USUARIO (DNI),
  ADD CONSTRAINT train_ex_fk2 FOREIGN KEY (dni_alumno) REFERENCES USUARIO (DNI),
  ADD CONSTRAINT train_ex_fk3 FOREIGN KEY (tipo_programa) REFERENCES TIPO (tipo_code);

ALTER TABLE TIPO_EJERCICIO
  ADD PRIMARY KEY (ej_code,tipo),
  ADD CONSTRAINT tipo_ex_fk1 FOREIGN KEY (ej_code) REFERENCES EJERCICIO (ex_code),
  ADD CONSTRAINT tipo_ex_fk2 FOREIGN KEY (tipo) REFERENCES TIPO (tipo_code);

ALTER TABLE LINEA_ENTRENAMIENTO 
  ADD PRIMARY KEY (line_num),
  ADD CONSTRAINT line_ex_fk1 FOREIGN KEY (codigo_entreno) REFERENCES ENTRENAMIENTO (train_code),
  ADD CONSTRAINT line_ex_fk2 FOREIGN KEY (codigo_ejercicio) REFERENCES EJERCICIO (ex_code);

INSERT INTO TIPO (tipo_code, nombre) VALUES
('WF', 'WEIGHT_LIFTING'),
('YG', 'YOGA'),
('RH', 'REHABILITATION'),
('GM', 'GYMNASTICS'),
('ST', 'STRONGMAN'),
('MB', 'MOBILITY'),
('RT', 'RUNNING_TECHNIQUE'),
('HT', 'HYPERTROPHY'),
('CD', 'CARDIO');

INSERT INTO USUARIO (DNI, password, discriminador, nombre, apellido1, apellido2, email, telefono, direccion, IBAN, num_prog_prep, tipo_prog_solicitado) VALUES
('11111111A', 'usuario001', 'entrenador', 'Oswald', 'Cobblepot', 'Penguin', 'oswaldc@gmail.com', 600000001, 'Pingu Ave. 66 Gotham City  KT13 8XQ', null, 10, null ),
('22222222B', 'usuario002', 'entrenador', 'Harley', 'Queen', 'Joker', 'harleyqk@gmail.com', 600000002, 'Madness St. 24 Kansas  KT13 8XL', null, 10, null ),
('33333333C', 'usuario003', 'entrenador', 'Edward', 'Nygma', 'Enigma', 'edwardnygma@gmail.com', 600000003, 'The Riddler St. 48 Gotham City  KH13 8XQ', null, 10, null ),
('44444444D', 'usuario004', 'entrenador', 'Alexander', 'Luthor', null, 'lexluthor@gmail.com', 600000004, 'Kryptonite Road 67 Metropolis  KT13 9XQ', null, 10, 'WF' ),
('55555555E', 'usuario005', 'entrenador', 'Alice', 'Kane', 'Beth', 'alicekane@gmail.com', 600000005, 'Mansion Wayne Road s/n Gotham City  KT13 8PQ', null, 10, null ),
('66666666F', 'usuario006', 'alumno', 'Selina', 'Kyle', 'Catwoman', 'catwoman@gmail.com', 600000006, 'The little Rabbit St. 44 Gotham City  KH13 8XQ', 'US22050204050607019675', 10, 'YG'),
('77777777G', 'usuario007', 'alumno', 'Bruce', 'Wayne', 'Batman', 'brucewayne@gmail.com', 600000007, 'Mansion Wayne Road s/n Gotham City  KT13 8XQ', 'US22050204050607019680', null, 'RH' ),
('88888888H', 'usuario008', 'alumno', 'Clark', 'Kent', 'Superman', 'clarkkent@gmail.com', 600000008, 'Long Field St. 24 Kansas  KT13 8XL', 'US22050204050607019685', null, null ),
('99999999I', 'usuario009', 'alumno', 'Kara', 'Danvers', 'Supergirl', 'karadanvers@gmail.com', 600000009, 'The CatCo s/n Central City  KT00 8XQ', 'US22050204060607019685', null, 'WF' ),
('00000000J', 'usuario010', 'alumno', 'Barry', 'Allen', 'Flash', 'barryallen@gmail.com', 600000010, 'Running Home St. 99 Star City  KT13 5XQ', 'US22050204050557019685', null, 'HT' ),
('11111111K', 'usuario011', 'alumno', 'Oliver', 'Queen', 'Arrow', 'oliverqueen@gmail.com', 600000011, 'Mansion Queen Road s/n Starling City  KT13 8IQ', 'US22050204050607019633',  null, 'RT' ),
('22222222L', 'usuario012', 'alumno', 'Sara', 'Lance', 'Canary', 'saralance@gmail.com', 600000012, 'Time Ship Road 33 s/n Time City  KA13 8XQ', 'US22050644050607019685', null, null ),
('33333333M', 'usuario013', 'alumno', 'Kate', 'Kane', 'Batwoman', 'katekane@gmail.com', 600000013, 'Wayne Enterprises Ave. s/n Gotham City  KM13 80Q', 'US22050204050606719685', null, null ),
('44444444N', 'usuario014', 'alumno', 'Helena', 'Bertinelli', 'Huntress', 'helenaberti@gmail.com', 600000014, 'Mansion Bertinelli Road s/n Starling City  KT33 8XQ', 'US22050204050607019573', null, 'GM' ),
('55555555O', 'usuario015', 'alumno', 'Diana', 'Prince', 'Wonder', 'dianaprince@gmail.com', 600000015, 'The Sea s/n Water City  KT10 8UY', 'US22050204050600719685', null, 'CD' ),
('66666666P', 'usuario016', 'alumno', 'Arthur', 'Curry', 'Aquaman', 'arthurcurry@gmail.com', 600000016, 'Mansion Wayne Road s/n Gotham City  KT13 7YY', 'US22050444050607019681', null, 'YG' ),
('77777777Q', 'usuario017', 'alumno', 'Alfred', 'Pennyworth', 'Butler', 'alfredpenny@gmail.com', 600000017, 'Mansion Wayne Road s/n Gotham City  KT13 8PPQ', 'US22050204050607019687', null, 'HT' ), 
('88888888R', 'usuario018', 'alumno', 'Victor', 'Stone', 'Cyborg', 'victorstone@gmail.com', 600000018, 'Technology Ave. s/n Gotham City  KK13 8XQ', 'US22050874070607019685', null, null ),
('99999999S', 'usuario019', 'alumno', 'John', 'Constantine', 'Witcher', 'johnconstantine@gmail.com', 600000019, 'Hellskitchen 23 Starling City  IT13 8XQ', 'US22050204050607019323', null, 'RT' ),
('00000000T', 'usuario020', 'alumno', 'Rachel', 'Roth', 'Raven', 'rachelroth@gmail.com', 600000020, 'The darkest avenur 99 Chicago City  KT00 8XQ', 'US22050204050607819681', null, 'YG' );

INSERT INTO EJERCICIO (ex_code, nombre, descripcion) VALUES
('DL', 'DEAD LIFT', 'Peso muerto con la barra olímpica desde el suelo'),
('CJ', 'CLEAN & JERK', 'Arrancada desde el suelo con la barra olímpica acabando con un split'),
('HC', 'HANG CLEAN', 'Arrancada con la barra olímpica por encima de las rodillas'),
('SS', 'SQUAD SNATCH', 'Levantamiento de la barra desde el suelo acabando por encima de la cabeza con agarre ancho'),
('BS', 'BACK SQUAD', 'Sentadilla con barra olímpica colocada detrás del cuello'),
('FS', 'FRONT SQUAD', 'Sentadilla con barra olímpica colocada delante del cuello, en las clavículas'),
('PU', 'PULL UPS', 'Dominadas en el rack o en las anillas'),
('PK', 'STANDING BOSU', 'Mantener el equilibrio en un bosu con una pierna, trabajo propiocepción'),
('IS', 'ISOMETRICS', 'Ejercicios isometricos de cuadriceps'),
('KF', 'KNEE FLEX', 'Flexión de rodilla desde posición extendida'),
('MU', 'MUSCLE UPS', 'Balanceo del cuerpo hasta lograr dominada a la altura del pecho'),
('DU', 'DOUBLE UNDERS', 'Saltos sobles a la comba'),
('RW', 'ROWING', 'Remar en la máquina de remo'),
('KP', 'KEEPING', 'Balanceo con los hombros colgado de la barra'),
('LE', 'LEG EXTENSION', 'Extensiones de pierna'),
('ST', 'STAIRS', 'Diferentes modalidades de ejercicios con los pies en la escalera'),
('WB', 'WALL BALLS', 'Lazamiento de pelota de 5kg a la linea de la pared'),
('MB', 'MED BALL', 'Lanzamiento de pelota medicinal pesada por encima del hombro'),
('FW', 'FARMER WALK', 'Caminar 50 m con objeto pesado en las muñecas'),
('HP', 'HAND STAND PUSH UPS', 'Flexiones haciendo el pino'),
('CP', 'COBRA POSITION', 'Tumbado boca abajo flexionando la espalda atrás con apoyo de brazos y piernas estirados'),
('GP', 'WARRIOR POSITION', 'Pierna izquierda en tensión, peso en pierna derecha; brazos estirados'),
('BP', 'BENCH PRESS', 'Press de banca'),
('PS', 'PUSH UPS', 'Flexioens con mancuernas'),
('FC', 'FEMORAL CURL', 'Curl femoral con máquina'),
('RR', 'RUNNING', 'Correr 5 km/10 km a elección del entrenador'),
('HB', 'HIPS MONILITY', 'Rotación de caderas desde el suelo levantando rodillas'),
('HW', 'HANDSTAND WALK', 'Caminar haciendo el pino'),
('DP', 'DOG POSITION', 'V invertida con sólo manos y pies de apoyo.');

INSERT INTO TIPO_EJERCICIO (ej_code, tipo) VALUES
('DL', 'WF'),
('CJ', 'WF'),
('HC', 'WF'),
('HP', 'WF'),
('SS', 'WF'),
('BS', 'WF'),
('BS', 'HT'),
('FS', 'WF'),
('FS', 'HT'),
('PU', 'HT'),
('PU', 'GM'),
('PK', 'RH'),
('IS', 'RH'),
('KF', 'RH'),
('MU', 'GM'),
('DU', 'GM'),
('DU', 'CD'),
('RW', 'CD'),
('KP', 'GM'),
('LE', 'MB'),
('ST', 'RT'),
('WB', 'HT'),
('WB', 'ST'),
('MB', 'ST'),
('FW', 'ST'),
('HP', 'GM'),
('CP', 'YG'),
('GP', 'GM'),
('BP', 'HT'),
('BP', 'WF'),
('FC', 'HT'),
('RR', 'CD'),
('HB', 'MB'),
('HW', 'GM'),
('DP', 'YG');

INSERT INTO ENTRENAMIENTO (dni_entrenador, dni_alumno, tipo_programa, fecha_creacion) VALUES
('11111111A', '66666666F', 'GM', '2020-05-10'),
('22222222B', '77777777G', 'YG', '2020-05-11'),
('33333333C', '88888888H', 'WF', '2020-05-11'),
('44444444D', '99999999I', 'CD', '2020-05-11'),
('55555555E', '00000000J', 'RH', '2020-05-11'),
('11111111A', '11111111K', 'WF', '2020-05-11'),
('22222222B', '22222222L', 'HT', '2020-05-12'),
('33333333C', '33333333M', 'GM', '2020-05-12'),
('44444444D', '44444444N', 'ST', '2020-05-12'),
('55555555E', '55555555O', 'WF', '2020-05-13'),
('11111111A', '66666666P', 'GM', '2020-05-13'),
('22222222B', '77777777Q', 'MB', '2020-05-14'),
('33333333C', '88888888R', 'ST', '2020-05-14'),
('44444444D', '99999999S', 'RT', '2020-05-14'),
('55555555E', '00000000T', 'CD', '2020-05-14');

INSERT INTO LINEA_ENTRENAMIENTO (codigo_entreno, codigo_ejercicio, repeticiones, tiempo_min) VALUES
(1, 'PU', 10, 4),
(1, 'KP', 20, null),
(1, 'GP', 20, null),
(1, 'HW', 20, null),
(2, 'CP', null, 3),
(2, 'GP', null, 5),
(2, 'DP', null, 5),
(3, 'BS', 40, 10),
(3, 'DL', 20, 10),
(3, 'HP', 30, 10),
(4, 'RR', null, 20),
(4, 'DU', 400, null),
(4, 'MU', 20, null),
(5, 'KF', 20, 5),
(5, 'PK', null, 15),
(6, 'BS', 25, null),
(6, 'PU', 25, null),
(7, 'FS', 25, null),
(7, 'PU', 100, 20),
(7, 'WB', 50, 7),
(8, 'PU', 20, null),
(8, 'MU', 20, null),
(8, 'DU', 400, 10),
(9, 'WB', 20, null),
(9, 'FW', 100, null),
(10, 'HP', 20, null),
(10, 'SS', 20, null),
(10, 'CJ', 10, 20),
(10, 'KF', 20, null),
(11, 'DU', 400, 10),
(11, 'LE', 20, null),
(11, 'WB', 200, 4),
(12, 'CP', 20, null),
(12, 'GP', 20, null),
(12, 'LE', 20, null),
(12, 'HB', 20, null),
(13, 'HB', null, 10),
(13, 'FW', null, 10),
(13, 'FS', 20, null),
(13, 'WB', 50, 5),
(14, 'LE', 40, 10),
(14, 'ST', 20, null),
(15, 'RW', 20, null),
(15, 'KF', null, 5),
(15, 'DU', 20, null);