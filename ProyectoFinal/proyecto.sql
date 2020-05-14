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
(001, 'usuario001', 'entrenador', 'Oswald', 'Cobblepot', 'Penguin', '11111111A', 'oswaldc@gmail.com', 600000001, 'Pingu Ave. 66 Gotham City  KT13 8XQ' ),
(002, 'usuario002', 'entrenador', 'Harley', 'Queen', 'Joker', '22222222B', 'harleyqk@gmail.com', 600000002, 'Madness St. 24 Kansas  KT13 8XL' ),
(003, 'usuario003', 'entrenador', 'Edward', 'Nygma', 'Enigma', '33333333C', 'edwardnygma@gmail.com', 600000003, 'The Riddler St. 48 Gotham City  KH13 8XQ' ),
(004, 'usuario004', 'entrenador', 'Alexander', 'Luthor', null, '44444444D', 'lexluthor@gmail.com', 600000004, 'Kryptonite Road 67 Metropolis  KT13 9XQ' ),
(005, 'usuario005', 'entrenador', 'Alice', 'Kane', 'Beth', '55555555E', 'alicekane@gmail.com', 600000005, 'Mansion Wayne Road s/n Gotham City  KT13 8PQ' ),
(006, 'usuario006', 'alumno', 'Selina', 'Kyle', 'Catwoman', '66666666F', 'catwoman@gmail.com', 600000006, 'The little Rabbit St. 44 Gotham City  KH13 8XQ'),
(007, 'usuario007', 'alumno', 'Bruce', 'Wayne', 'Batman', '77777777G', 'brucewayne@gmail.com', 600000007, 'Mansion Wayne Road s/n Gotham City  KT13 8XQ' ),
(008, 'usuario008', 'alumno', 'Clark', 'Kent', 'Superman', '88888888H', 'clarkkent@gmail.com', 600000008, 'Long Field St. 24 Kansas  KT13 8XL' ),
(009, 'usuario009', 'alumno', 'Kara', 'Danvers', 'Supergirl', '99999999I', 'karadanvers@gmail.com', 600000009, 'The CatCo s/n Central City  KT00 8XQ' ),
(010, 'usuario010', 'alumno', 'Barry', 'Allen', 'Flash', '11111112A', 'barryallen@gmail.com', 600000010, 'Running Home St. 99 Star City  KT13 5XQ' ),
(011, 'usuario011', 'alumno', 'Oliver', 'Queen', 'Arrow', '22222223B', 'oliverqueen@gmail.com', 600000011, 'Mansion Queen Road s/n Starling City  KT13 8IQ' ),
(012, 'usuario012', 'alumno', 'Sara', 'Lance', 'Canary', '33333334C', 'saralance@gmail.com', 600000012, 'Time Ship Road 33 s/n Time City  KA13 8XQ' ),
(013, 'usuario013', 'alumno', 'Kate', 'Kane', 'Batwoman', '44444445D', 'katekane@gmail.com', 600000013, 'Wayne Enterprises Ave. s/n Gotham City  KM13 80Q' ),
(014, 'usuario014', 'alumno', 'Helena', 'Bertinelli', 'Huntress', '55555556E', 'helenaberti@gmail.com', 600000014, 'Mansion Bertinelli Road s/n Starling City  KT33 8XQ' ),
(015, 'usuario015', 'alumno', 'Diana', 'Prince', 'Wonder', '66666667F', 'dianaprince@gmail.com', 600000015, 'The Sea s/n Water City  KT10 8UY' ),
(016, 'usuario016', 'alumno', 'Arthur', 'Curry', 'Aquaman', '77777778G', 'arthurcurry@gmail.com', 600000016, 'Mansion Wayne Road s/n Gotham City  KT13 7YY' ),
(017, 'usuario017', 'alumno', 'Alfred', 'Pennyworth', 'Butler', '88888889H', 'alfredpenny@gmail.com', 600000017, 'Mansion Wayne Road s/n Gotham City  KT13 8PPQ' ), 
(018, 'usuario018', 'alumno', 'Victor', 'Stone', 'Cyborg', '99999990I', 'victorstone@gmail.com', 600000018, 'Technology Ave. s/n Gotham City  KK13 8XQ' ),
(019, 'usuario019', 'alumno', 'John', 'Constantine', 'Witcher', '11111113A', 'johnconstantine@gmail.com', 600000019, 'Hellskitchen 23 Starling City  IT13 8XQ' ),
(020, 'usuario020', 'alumno', 'Rachel', 'Roth', 'Raven', '22222224B', 'rachelroth@gmail.com', 600000020, 'The darkest avenur 99 Chicago City  KT00 8XQ' );


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

INSERT INTO ENTRENAMIENTO (train_code, id_entrenador, id_alumno, fecha_creacion) VALUES
(1, 001, 006, '2020-05-10'),
(2, 002, 007, '2020-05-11'),
(3, 003, 008, '2020-05-11'),
(4, 004, 009, '2020-05-11'),
(5, 005, 010, '2020-05-11'),
(6, 001, 011, '2020-05-11'),
(7, 002, 012, '2020-05-12'),
(8, 003, 013, '2020-05-12'),
(9, 004, 014, '2020-05-12'),
(10, 005, 015, '2020-05-13'),
(11, 001, 016, '2020-05-13'),
(12, 002, 017, '2020-05-14'),
(13, 003, 018, '2020-05-14'),
(14, 004, 019, '2020-05-14'),
(15, 005, 020, '2020-05-14');

INSERT INTO LINEA_ENTRENAMIENTO (codigo_entreno, codigo_ejercicio, repeticiones, tiempo_min) VALUES
(1, 'PU', 10, 4),
(1, 'KP', 20, null),
(1, 'GP', 20, null),
(1, 'HW', 20, null),
(2, 'CP', null, 3),
(2, 'GP', null, 5),
(2, 'DP', null, 5),
(2, 'CP', null, 2),
(3, 'BS', 40, 10),
(3, 'DL', 20, 10),
(3, 'HP', 30, 10),
(4, 'RR', null, 20),
(4, 'DU', 400, null),
(4, 'MU', 20, null),
(5, 'KF', 20, 5),
(5, 'PK', null, 15),
(6, 'BS', 25, null),
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
(13, 'HB', null, 10),
(13, 'FS', 20, null),
(13, 'WB', 50, 5),
(14, 'GP', 20, null),
(14, 'LE', 40, 10),
(14, 'ST', 20, null),
(15, 'RW', 20, null),
(15, 'KF', null, null),
(15, 'FS', 20, null);
