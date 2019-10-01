-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Oct 01, 2019 at 03:49 PM
-- Server version: 5.7.26
-- PHP Version: 7.2.18

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `checador`
--

-- --------------------------------------------------------

--
-- Table structure for table `actualizaciones`
--

DROP TABLE IF EXISTS `actualizaciones`;
CREATE TABLE IF NOT EXISTS `actualizaciones` (
  `usuario` int(8) UNSIGNED NOT NULL,
  `actualizado` datetime DEFAULT NULL,
  `huella` int(10) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `administradores`
--

DROP TABLE IF EXISTS `administradores`;
CREATE TABLE IF NOT EXISTS `administradores` (
  `codigo` varchar(10) NOT NULL,
  PRIMARY KEY (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `bloques`
--

DROP TABLE IF EXISTS `bloques`;
CREATE TABLE IF NOT EXISTS `bloques` (
  `bloque` tinyint(4) NOT NULL,
  `inicio` date NOT NULL,
  `fin` date NOT NULL,
  `anio` year(4) NOT NULL,
  `ciclo` enum('A','B','V') NOT NULL,
  PRIMARY KEY (`bloque`,`anio`,`ciclo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `configuraciones`
--

DROP TABLE IF EXISTS `configuraciones`;
CREATE TABLE IF NOT EXISTS `configuraciones` (
  `configuracion` varchar(20) NOT NULL,
  `valor` varbinary(50) NOT NULL,
  PRIMARY KEY (`configuracion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `configuraciones`
--

INSERT INTO `configuraciones` (`configuracion`, `valor`) VALUES
('datatime', 0x36),
('decorated', 0x74727565),
('fullscreen', 0x74727565),
('hostserver', 0x687474703a2f2f3132372e302e302e312f73696361),
('huellatime', 0x36),
('preventaltf4', 0x74727565),
('preventesc', 0x74727565),
('servertimeout', 0x3130),
('sincronizar', 0x74727565),
('tecladotime', 0x35);

-- --------------------------------------------------------

--
-- Table structure for table `correosusuarios`
--

DROP TABLE IF EXISTS `correosusuarios`;
CREATE TABLE IF NOT EXISTS `correosusuarios` (
  `usuario` varchar(10) NOT NULL,
  `correo` varchar(100) NOT NULL,
  `principal` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`usuario`,`correo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `crn`
--

DROP TABLE IF EXISTS `crn`;
CREATE TABLE IF NOT EXISTS `crn` (
  `usuario` int(8) UNSIGNED NOT NULL,
  `materia` varchar(10) NOT NULL DEFAULT '',
  `crn` int(8) UNSIGNED NOT NULL,
  `anio` year(4) NOT NULL,
  `ciclo` enum('A','B') NOT NULL,
  PRIMARY KEY (`usuario`,`materia`,`crn`,`anio`,`ciclo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `directivos`
--

DROP TABLE IF EXISTS `directivos`;
CREATE TABLE IF NOT EXISTS `directivos` (
  `codigo` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `eventos`
--

DROP TABLE IF EXISTS `eventos`;
CREATE TABLE IF NOT EXISTS `eventos` (
  `tipo` tinyint(3) UNSIGNED NOT NULL,
  `inicio` date NOT NULL,
  `fin` date NOT NULL,
  `asignaturas` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`tipo`,`inicio`,`fin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `eventos_tipos`
--

DROP TABLE IF EXISTS `eventos_tipos`;
CREATE TABLE IF NOT EXISTS `eventos_tipos` (
  `tipo` tinyint(3) UNSIGNED NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) DEFAULT NULL,
  `color` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`tipo`),
  KEY `id` (`tipo`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Stand-in structure for view `get_crns`
-- (See below for the actual view)
--
DROP VIEW IF EXISTS `get_crns`;
CREATE TABLE IF NOT EXISTS `get_crns` (
`crn` int(8) unsigned
,`anio` year(4)
,`ciclo` enum('A','B')
,`codmat` varchar(10)
,`materia` varchar(150)
,`codProf` int(8) unsigned
,`profesor` varchar(200)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `get_eventos`
-- (See below for the actual view)
--
DROP VIEW IF EXISTS `get_eventos`;
CREATE TABLE IF NOT EXISTS `get_eventos` (
`tipo` tinyint(3) unsigned
,`inicio` date
,`fin` date
,`nombre` varchar(50)
,`asignaturas` tinyint(1)
,`color` varchar(15)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `get_horario`
-- (See below for the actual view)
--
DROP VIEW IF EXISTS `get_horario`;
CREATE TABLE IF NOT EXISTS `get_horario` (
`usuario` int(8) unsigned
,`crn` int(8) unsigned
,`anio` year(4)
,`ciclo` enum('A','B')
,`bloque` tinyint(4)
,`horario` time
,`dia` enum('LUNES','MARTES','MIERCOLES','JUEVES','VIERNES','SABADO','DOMINGO')
,`aula` varchar(15)
,`materia` varchar(150)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `get_horario_crn`
-- (See below for the actual view)
--
DROP VIEW IF EXISTS `get_horario_crn`;
CREATE TABLE IF NOT EXISTS `get_horario_crn` (
`usuario` int(8) unsigned
,`nombre` varchar(200)
,`crn` int(8) unsigned
,`anio` year(4)
,`ciclo` enum('A','B')
,`bloque` tinyint(4)
,`inicio` date
,`fin` date
,`horario` time
,`dia` enum('LUNES','MARTES','MIERCOLES','JUEVES','VIERNES','SABADO','DOMINGO')
,`materia` varchar(150)
,`departamento` varchar(5)
,`aula` varchar(15)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `get_horario_para_asistencia`
-- (See below for the actual view)
--
DROP VIEW IF EXISTS `get_horario_para_asistencia`;
CREATE TABLE IF NOT EXISTS `get_horario_para_asistencia` (
`usuario` int(8) unsigned
,`nombre` varchar(200)
,`departamento` char(3)
,`tipo` varchar(30)
,`crn` int(8) unsigned
,`anio` year(4)
,`horario` time
,`dia` enum('LUNES','MARTES','MIERCOLES','JUEVES','VIERNES','SABADO','DOMINGO')
,`materia` varchar(150)
,`inicio` date
,`fin` date
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `get_log`
-- (See below for the actual view)
--
DROP VIEW IF EXISTS `get_log`;
CREATE TABLE IF NOT EXISTS `get_log` (
`usuario` varchar(10)
,`nombre` varchar(200)
,`fecha` datetime
,`descripcion` varbinary(200)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `get_some_usuarios`
-- (See below for the actual view)
--
DROP VIEW IF EXISTS `get_some_usuarios`;
CREATE TABLE IF NOT EXISTS `get_some_usuarios` (
`usuario` int(8) unsigned
,`nombre` varchar(200)
,`codtipo` tinyint(1) unsigned
,`tipo` varchar(30)
,`coddepto` char(3)
,`departamento` varchar(100)
,`status` tinyint(1) unsigned
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `get_usuario`
-- (See below for the actual view)
--
DROP VIEW IF EXISTS `get_usuario`;
CREATE TABLE IF NOT EXISTS `get_usuario` (
`usuario` int(8) unsigned
,`nombre` varchar(200)
,`tipo` varchar(30)
,`status` varchar(15)
,`departamento` varchar(100)
,`telefono` varchar(20)
,`correo` varchar(100)
);

-- --------------------------------------------------------

--
-- Table structure for table `horarioscrn`
--

DROP TABLE IF EXISTS `horarioscrn`;
CREATE TABLE IF NOT EXISTS `horarioscrn` (
  `crn` int(8) UNSIGNED NOT NULL,
  `bloque` tinyint(1) UNSIGNED NOT NULL,
  `dia` enum('LUNES','MARTES','MIERCOLES','JUEVES','VIERNES','SABADO','DOMINGO') NOT NULL,
  `hora` time NOT NULL,
  `duracion` time NOT NULL DEFAULT '02:00:00',
  `aula` varchar(15) DEFAULT NULL,
  `anio` year(4) NOT NULL,
  `ciclo` enum('A','B') NOT NULL,
  PRIMARY KEY (`crn`,`bloque`,`dia`,`hora`,`anio`,`ciclo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `horarioscrn_hrs`
--

DROP TABLE IF EXISTS `horarioscrn_hrs`;
CREATE TABLE IF NOT EXISTS `horarioscrn_hrs` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `duracion` time NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `horarioscrn_lapsos`
--

DROP TABLE IF EXISTS `horarioscrn_lapsos`;
CREATE TABLE IF NOT EXISTS `horarioscrn_lapsos` (
  `fecha_inicial` date NOT NULL,
  `checar` enum('entrada','entysal','otro') NOT NULL DEFAULT 'entrada',
  PRIMARY KEY (`fecha_inicial`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `horariousuarios`
--

DROP TABLE IF EXISTS `horariousuarios`;
CREATE TABLE IF NOT EXISTS `horariousuarios` (
  `usuario` int(8) UNSIGNED NOT NULL,
  `dias` varchar(7) NOT NULL,
  `entrada` time DEFAULT NULL,
  `salida` time DEFAULT NULL,
  `diasig` tinyint(1) DEFAULT '0',
  `vigencia` date NOT NULL,
  PRIMARY KEY (`usuario`,`dias`,`vigencia`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `huellas`
--

DROP TABLE IF EXISTS `huellas`;
CREATE TABLE IF NOT EXISTS `huellas` (
  `usuario` int(8) UNSIGNED NOT NULL,
  `huella` blob,
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `FK_huellas` (`usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Triggers `huellas`
--
DROP TRIGGER IF EXISTS `huella_deleted`;
DELIMITER $$
CREATE TRIGGER `huella_deleted` AFTER DELETE ON `huellas` FOR EACH ROW BEGIN 	INSERT INTO actualizaciones (usuario, actualizado, huella) VALUES (Old.usuario , NOW(), Old.id);     END
$$
DELIMITER ;
DROP TRIGGER IF EXISTS `huella_insert`;
DELIMITER $$
CREATE TRIGGER `huella_insert` AFTER INSERT ON `huellas` FOR EACH ROW BEGIN 	INSERT INTO actualizaciones (usuario, actualizado, huella) VALUES (New.usuario , NOW(), New.id);     END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `instancias`
--

DROP TABLE IF EXISTS `instancias`;
CREATE TABLE IF NOT EXISTS `instancias` (
  `codigo` varchar(5) NOT NULL,
  `nombre` varchar(100) DEFAULT NULL,
  `jefe` int(8) UNSIGNED DEFAULT NULL,
  PRIMARY KEY (`codigo`),
  KEY `jefe` (`jefe`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `justificantes_asignaturas`
--

DROP TABLE IF EXISTS `justificantes_asignaturas`;
CREATE TABLE IF NOT EXISTS `justificantes_asignaturas` (
  `folio` int(8) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT,
  `fecha` date NOT NULL,
  `crn` int(8) UNSIGNED NOT NULL,
  PRIMARY KEY (`folio`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `justificantes_comentarios`
--

DROP TABLE IF EXISTS `justificantes_comentarios`;
CREATE TABLE IF NOT EXISTS `justificantes_comentarios` (
  `folio` int(8) UNSIGNED ZEROFILL NOT NULL,
  `usuario` int(8) UNSIGNED NOT NULL,
  `comentario` varchar(512) NOT NULL,
  `horayfecha` datetime NOT NULL,
  PRIMARY KEY (`folio`,`usuario`,`horayfecha`),
  KEY `justificantes_comentarios_ibfk2` (`folio`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `justificantes_folios`
--

DROP TABLE IF EXISTS `justificantes_folios`;
CREATE TABLE IF NOT EXISTS `justificantes_folios` (
  `folio` int(8) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT,
  `fechayhora` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario` int(8) UNSIGNED NOT NULL,
  `justificante` smallint(5) UNSIGNED NOT NULL,
  `fraccion` varchar(5) DEFAULT NULL,
  `aceptado` tinyint(1) DEFAULT NULL,
  `aceptadopor` int(8) UNSIGNED DEFAULT NULL,
  `aprobado` tinyint(1) DEFAULT NULL,
  `aprobadopor` int(8) UNSIGNED DEFAULT NULL,
  PRIMARY KEY (`folio`),
  KEY `usuario` (`usuario`),
  KEY `justificante` (`justificante`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `justificantes_fracciones`
--

DROP TABLE IF EXISTS `justificantes_fracciones`;
CREATE TABLE IF NOT EXISTS `justificantes_fracciones` (
  `justificante_id` smallint(5) UNSIGNED NOT NULL,
  `fraccion` varchar(5) NOT NULL,
  `categoria` varchar(150) DEFAULT NULL,
  `documentos` tinyint(1) DEFAULT NULL,
  `descripcion` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`justificante_id`,`fraccion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `justificantes_lista`
--

DROP TABLE IF EXISTS `justificantes_lista`;
CREATE TABLE IF NOT EXISTS `justificantes_lista` (
  `id` smallint(5) UNSIGNED NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) DEFAULT NULL,
  `descripcion_gral` varchar(150) DEFAULT NULL,
  `descripcion` varbinary(250) DEFAULT NULL,
  `documentos` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `justificantes_periodo`
--

DROP TABLE IF EXISTS `justificantes_periodo`;
CREATE TABLE IF NOT EXISTS `justificantes_periodo` (
  `folio` int(8) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT,
  `fecha_inicial` date NOT NULL,
  `fecha_final` date NOT NULL,
  PRIMARY KEY (`folio`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `justificantes_tipousuarios`
--

DROP TABLE IF EXISTS `justificantes_tipousuarios`;
CREATE TABLE IF NOT EXISTS `justificantes_tipousuarios` (
  `justificante_id` smallint(5) UNSIGNED NOT NULL,
  `tipousuario_id` tinyint(1) UNSIGNED NOT NULL,
  `tipoUs` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`justificante_id`,`tipousuario_id`),
  KEY `tipousuario_id` (`tipousuario_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `log`
--

DROP TABLE IF EXISTS `log`;
CREATE TABLE IF NOT EXISTS `log` (
  `usuario` varchar(10) DEFAULT NULL,
  `fecha` datetime DEFAULT NULL,
  `descripcion` varbinary(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `materias`
--

DROP TABLE IF EXISTS `materias`;
CREATE TABLE IF NOT EXISTS `materias` (
  `codigo` varchar(10) NOT NULL,
  `nombre` varchar(150) NOT NULL,
  `departamento` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`codigo`,`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `mensajes`
--

DROP TABLE IF EXISTS `mensajes`;
CREATE TABLE IF NOT EXISTS `mensajes` (
  `usuario` int(8) UNSIGNED NOT NULL,
  `mensaje` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `registrosfull`
--

DROP TABLE IF EXISTS `registrosfull`;
CREATE TABLE IF NOT EXISTS `registrosfull` (
  `usuario` int(8) UNSIGNED NOT NULL,
  `fechahora` datetime NOT NULL,
  `tipo` enum('huella','teclado','justificado') DEFAULT NULL,
  `equipo` varchar(7) DEFAULT NULL,
  `modificado` int(8) UNSIGNED DEFAULT NULL,
  PRIMARY KEY (`usuario`,`fechahora`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `statususuarios`
--

DROP TABLE IF EXISTS `statususuarios`;
CREATE TABLE IF NOT EXISTS `statususuarios` (
  `status` tinyint(3) UNSIGNED NOT NULL,
  `descripcion` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tipousuarios`
--

DROP TABLE IF EXISTS `tipousuarios`;
CREATE TABLE IF NOT EXISTS `tipousuarios` (
  `tipo` tinyint(1) UNSIGNED NOT NULL,
  `descripcion` varchar(30) NOT NULL,
  `orden` tinyint(1) UNSIGNED NOT NULL,
  `jornada` enum('sinjornada','obligatoria','libre') NOT NULL,
  PRIMARY KEY (`tipo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
CREATE TABLE IF NOT EXISTS `usuarios` (
  `usuario` int(8) UNSIGNED NOT NULL,
  `nombre` varchar(200) NOT NULL,
  `departamento` char(3) NOT NULL,
  `tipo` tinyint(1) UNSIGNED NOT NULL,
  `status` tinyint(1) UNSIGNED NOT NULL,
  `pass` varchar(25) NOT NULL DEFAULT 'hola123',
  `telefono` varchar(20) DEFAULT NULL,
  `comentario` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`usuario`),
  KEY `tipo` (`tipo`),
  KEY `departamento` (`departamento`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Triggers `usuarios`
--
DROP TRIGGER IF EXISTS `user_insert`;
DELIMITER $$
CREATE TRIGGER `user_insert` AFTER INSERT ON `usuarios` FOR EACH ROW BEGIN 	INSERT INTO actualizaciones (usuario, actualizado) VALUES (New.usuario , NOW());     END
$$
DELIMITER ;
DROP TRIGGER IF EXISTS `user_update`;
DELIMITER $$
CREATE TRIGGER `user_update` AFTER UPDATE ON `usuarios` FOR EACH ROW BEGIN 	insert into actualizaciones (usuario, actualizado) values (Old.usuario , now());     END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `usuarios_huellas`
--

DROP TABLE IF EXISTS `usuarios_huellas`;
CREATE TABLE IF NOT EXISTS `usuarios_huellas` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `usuario` int(8) UNSIGNED NOT NULL,
  `huella` blob NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

--
-- Triggers `usuarios_huellas`
--
DROP TRIGGER IF EXISTS `usuarios_huellas_deleted`;
DELIMITER $$
CREATE TRIGGER `usuarios_huellas_deleted` AFTER DELETE ON `usuarios_huellas` FOR EACH ROW BEGIN 	INSERT INTO actualizaciones (usuario, actualizado, huella) VALUES (Old.usuario , NOW(), Old.id);     
    END
$$
DELIMITER ;
DROP TRIGGER IF EXISTS `usuarios_huellas_insert`;
DELIMITER $$
CREATE TRIGGER `usuarios_huellas_insert` AFTER INSERT ON `usuarios_huellas` FOR EACH ROW BEGIN 	INSERT INTO actualizaciones (usuario, actualizado, huella) VALUES (New.usuario , NOW(), New.id);     
    END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Structure for view `get_crns`
--
DROP TABLE IF EXISTS `get_crns`;

CREATE ALGORITHM=UNDEFINED DEFINER=`frank`@`%` SQL SECURITY DEFINER VIEW `get_crns`  AS  (select `crn`.`crn` AS `crn`,`crn`.`anio` AS `anio`,`crn`.`ciclo` AS `ciclo`,`crn`.`materia` AS `codmat`,`materias`.`nombre` AS `materia`,`crn`.`usuario` AS `codProf`,`usuarios`.`nombre` AS `profesor` from ((`crn` join `materias` on((`crn`.`materia` = `materias`.`codigo`))) join `usuarios` on((`crn`.`usuario` = `usuarios`.`usuario`))) order by `materias`.`nombre`) ;

-- --------------------------------------------------------

--
-- Structure for view `get_eventos`
--
DROP TABLE IF EXISTS `get_eventos`;

CREATE ALGORITHM=UNDEFINED DEFINER=`frank`@`%` SQL SECURITY DEFINER VIEW `get_eventos`  AS  (select `eventos`.`tipo` AS `tipo`,`eventos`.`inicio` AS `inicio`,`eventos`.`fin` AS `fin`,`eventos_tipos`.`nombre` AS `nombre`,`eventos`.`asignaturas` AS `asignaturas`,`eventos_tipos`.`color` AS `color` from (`eventos` join `eventos_tipos` on((`eventos`.`tipo` = `eventos_tipos`.`tipo`)))) ;

-- --------------------------------------------------------

--
-- Structure for view `get_horario`
--
DROP TABLE IF EXISTS `get_horario`;

CREATE ALGORITHM=UNDEFINED DEFINER=`frank`@`%` SQL SECURITY DEFINER VIEW `get_horario`  AS  (select `usuarios`.`usuario` AS `usuario`,`crn`.`crn` AS `crn`,`crn`.`anio` AS `anio`,`crn`.`ciclo` AS `ciclo`,`bloques`.`bloque` AS `bloque`,`horarioscrn`.`hora` AS `horario`,`horarioscrn`.`dia` AS `dia`,`horarioscrn`.`aula` AS `aula`,`materias`.`nombre` AS `materia` from (((((`usuarios` join `tipousuarios` on((`usuarios`.`tipo` = `tipousuarios`.`tipo`))) join `crn` on((`crn`.`usuario` = `usuarios`.`usuario`))) join `horarioscrn` on(((`horarioscrn`.`crn` = `crn`.`crn`) and (`horarioscrn`.`anio` = `crn`.`anio`) and (`horarioscrn`.`ciclo` = `crn`.`ciclo`)))) join `materias` on((`materias`.`codigo` = `crn`.`materia`))) join `bloques` on(((`horarioscrn`.`bloque` = `bloques`.`bloque`) and (`horarioscrn`.`anio` = `bloques`.`anio`) and (`horarioscrn`.`ciclo` = `bloques`.`ciclo`)))) where ((`horarioscrn`.`hora` > (now() - interval 20 minute)) and (`horarioscrn`.`hora` < (now() + interval 20 minute)) and (`crn`.`anio` = year(now())) and (`crn`.`ciclo` = `CURRENT_CICLO`()) and (`horarioscrn`.`dia` = `CURRENT_DIA`()) and ((`horarioscrn`.`bloque` = `CURRENT_BLOQUE`()) or (`bloques`.`bloque` = 0)) and (cast(now() as date) between `bloques`.`inicio` and `bloques`.`fin`))) ;

-- --------------------------------------------------------

--
-- Structure for view `get_horario_crn`
--
DROP TABLE IF EXISTS `get_horario_crn`;

CREATE ALGORITHM=UNDEFINED DEFINER=`frank`@`%` SQL SECURITY DEFINER VIEW `get_horario_crn`  AS  (select `usuarios`.`usuario` AS `usuario`,`usuarios`.`nombre` AS `nombre`,`crn`.`crn` AS `crn`,`crn`.`anio` AS `anio`,`crn`.`ciclo` AS `ciclo`,`bloques`.`bloque` AS `bloque`,`bloques`.`inicio` AS `inicio`,`bloques`.`fin` AS `fin`,`horarioscrn`.`hora` AS `horario`,`horarioscrn`.`dia` AS `dia`,`materias`.`nombre` AS `materia`,`materias`.`departamento` AS `departamento`,`horarioscrn`.`aula` AS `aula` from (((((`usuarios` join `tipousuarios` on((`usuarios`.`tipo` = `tipousuarios`.`tipo`))) join `crn` on((`crn`.`usuario` = `usuarios`.`usuario`))) join `horarioscrn` on(((`horarioscrn`.`crn` = `crn`.`crn`) and (`horarioscrn`.`anio` = `crn`.`anio`) and (`horarioscrn`.`ciclo` = `crn`.`ciclo`)))) join `materias` on((`materias`.`codigo` = `crn`.`materia`))) join `bloques` on(((`horarioscrn`.`bloque` = `bloques`.`bloque`) and (`horarioscrn`.`anio` = `bloques`.`anio`) and (`horarioscrn`.`ciclo` = `bloques`.`ciclo`))))) ;

-- --------------------------------------------------------

--
-- Structure for view `get_horario_para_asistencia`
--
DROP TABLE IF EXISTS `get_horario_para_asistencia`;

CREATE ALGORITHM=UNDEFINED DEFINER=`frank`@`%` SQL SECURITY DEFINER VIEW `get_horario_para_asistencia`  AS  (select `usuarios`.`usuario` AS `usuario`,`usuarios`.`nombre` AS `nombre`,`usuarios`.`departamento` AS `departamento`,`tipousuarios`.`descripcion` AS `tipo`,`crn`.`crn` AS `crn`,`crn`.`anio` AS `anio`,`horarioscrn`.`hora` AS `horario`,`horarioscrn`.`dia` AS `dia`,`materias`.`nombre` AS `materia`,`bloques`.`inicio` AS `inicio`,`bloques`.`fin` AS `fin` from (((((`crn` join `usuarios` on((`crn`.`usuario` = `usuarios`.`usuario`))) join `horarioscrn` on(((`horarioscrn`.`crn` = `crn`.`crn`) and (`horarioscrn`.`anio` = `crn`.`anio`) and (`horarioscrn`.`ciclo` = `crn`.`ciclo`)))) join `materias` on((`materias`.`codigo` = `crn`.`materia`))) join `bloques` on(((`horarioscrn`.`bloque` = `bloques`.`bloque`) and (`horarioscrn`.`anio` = `bloques`.`anio`) and (`horarioscrn`.`ciclo` = `bloques`.`ciclo`)))) left join `tipousuarios` on((`usuarios`.`tipo` = `tipousuarios`.`tipo`)))) ;

-- --------------------------------------------------------

--
-- Structure for view `get_log`
--
DROP TABLE IF EXISTS `get_log`;

CREATE ALGORITHM=UNDEFINED DEFINER=`frank`@`%` SQL SECURITY DEFINER VIEW `get_log`  AS  (select `log`.`usuario` AS `usuario`,`usuarios`.`nombre` AS `nombre`,`log`.`fecha` AS `fecha`,`log`.`descripcion` AS `descripcion` from (`log` join `usuarios` on((`log`.`usuario` = `usuarios`.`usuario`))) order by `log`.`fecha` desc limit 500) ;

-- --------------------------------------------------------

--
-- Structure for view `get_some_usuarios`
--
DROP TABLE IF EXISTS `get_some_usuarios`;

CREATE ALGORITHM=UNDEFINED DEFINER=`frank`@`%` SQL SECURITY DEFINER VIEW `get_some_usuarios`  AS  (select `usuarios`.`usuario` AS `usuario`,`usuarios`.`nombre` AS `nombre`,`usuarios`.`tipo` AS `codtipo`,`tipousuarios`.`descripcion` AS `tipo`,`usuarios`.`departamento` AS `coddepto`,`instancias`.`nombre` AS `departamento`,`usuarios`.`status` AS `status` from ((`usuarios` join `tipousuarios` on((`usuarios`.`tipo` = `tipousuarios`.`tipo`))) join `instancias` on((`usuarios`.`departamento` = `instancias`.`codigo`))) where (`usuarios`.`status` = 1)) ;

-- --------------------------------------------------------

--
-- Structure for view `get_usuario`
--
DROP TABLE IF EXISTS `get_usuario`;

CREATE ALGORITHM=UNDEFINED DEFINER=`frank`@`%` SQL SECURITY DEFINER VIEW `get_usuario`  AS  (select `usuarios`.`usuario` AS `usuario`,`usuarios`.`nombre` AS `nombre`,`tipousuarios`.`descripcion` AS `tipo`,`statususuarios`.`descripcion` AS `status`,`instancias`.`nombre` AS `departamento`,`usuarios`.`telefono` AS `telefono`,`correosusuarios`.`correo` AS `correo` from ((((`usuarios` join `tipousuarios` on((`usuarios`.`tipo` = `tipousuarios`.`tipo`))) join `instancias` on((`usuarios`.`departamento` = `instancias`.`codigo`))) join `statususuarios` on((`statususuarios`.`status` = `usuarios`.`status`))) left join `correosusuarios` on(((`correosusuarios`.`usuario` = `usuarios`.`usuario`) and (`correosusuarios`.`principal` = 1))))) ;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `instancias`
--
ALTER TABLE `instancias`
  ADD CONSTRAINT `instancias_ibfk_1` FOREIGN KEY (`jefe`) REFERENCES `usuarios` (`usuario`) ON DELETE SET NULL ON UPDATE SET NULL;

--
-- Constraints for table `justificantes_asignaturas`
--
ALTER TABLE `justificantes_asignaturas`
  ADD CONSTRAINT `justificantes_asignaturas_ibfk_2` FOREIGN KEY (`folio`) REFERENCES `justificantes_folios` (`folio`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `justificantes_comentarios`
--
ALTER TABLE `justificantes_comentarios`
  ADD CONSTRAINT `justificantes_comentarios_ibfk2` FOREIGN KEY (`folio`) REFERENCES `justificantes_folios` (`folio`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `justificantes_folios`
--
ALTER TABLE `justificantes_folios`
  ADD CONSTRAINT `justificantes_folios_ibfk_1` FOREIGN KEY (`usuario`) REFERENCES `usuarios` (`usuario`),
  ADD CONSTRAINT `justificantes_folios_ibfk_2` FOREIGN KEY (`justificante`) REFERENCES `justificantes_lista` (`id`);

--
-- Constraints for table `justificantes_fracciones`
--
ALTER TABLE `justificantes_fracciones`
  ADD CONSTRAINT `justificantes_fracciones_ibfk_1` FOREIGN KEY (`justificante_id`) REFERENCES `justificantes_lista` (`id`) ON UPDATE CASCADE;

--
-- Constraints for table `justificantes_periodo`
--
ALTER TABLE `justificantes_periodo`
  ADD CONSTRAINT `justificantes_periodo_ibfk_1` FOREIGN KEY (`folio`) REFERENCES `justificantes_folios` (`folio`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `justificantes_tipousuarios`
--
ALTER TABLE `justificantes_tipousuarios`
  ADD CONSTRAINT `justificantes_tipousuarios_ibfk_1` FOREIGN KEY (`justificante_id`) REFERENCES `justificantes_lista` (`id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `justificantes_tipousuarios_ibfk_2` FOREIGN KEY (`tipousuario_id`) REFERENCES `tipousuarios` (`tipo`);

--
-- Constraints for table `usuarios`
--
ALTER TABLE `usuarios`
  ADD CONSTRAINT `usuarios_ibfk_1` FOREIGN KEY (`tipo`) REFERENCES `tipousuarios` (`tipo`),
  ADD CONSTRAINT `usuarios_ibfk_2` FOREIGN KEY (`departamento`) REFERENCES `instancias` (`codigo`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
