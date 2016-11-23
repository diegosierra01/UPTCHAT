-- phpMyAdmin SQL Dump
-- version 4.2.7.1
-- http://www.phpmyadmin.net
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 23-11-2016 a las 04:33:53
-- Versión del servidor: 5.6.20
-- Versión de PHP: 5.5.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de datos: `uptchat_bd`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `grupo`
--

CREATE TABLE IF NOT EXISTS `grupo` (
  `idgrupo` varchar(10) NOT NULL,
  `nombre` varchar(120) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `grupo`
--

INSERT INTO `grupo` (`idgrupo`, `nombre`) VALUES
('G5', 'el grupo'),
('G4', 'Grupo empr'),
('G1', 'grupo1'),
('G2', 'grupo2'),
('G3', 'grupo3');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `mensaje`
--

CREATE TABLE IF NOT EXISTS `mensaje` (
`idmensaje` int(11) NOT NULL,
  `cadena` varchar(255) NOT NULL,
  `destinatario` varchar(10) NOT NULL,
  `fecha` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `remitente` varchar(10) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=29 ;

--
-- Volcado de datos para la tabla `mensaje`
--

INSERT INTO `mensaje` (`idmensaje`, `cadena`, `destinatario`, `fecha`, `remitente`) VALUES
(1, 'msj quemado', 'U1', '2016-11-22 15:46:23', 'U2'),
(2, 'Hola', 'U1', '2016-11-22 15:46:23', 'U2'),
(3, 'sin embargo', 'U2', '2016-11-22 15:46:23', 'U1'),
(5, 'Perro', 'U1', '2016-11-22 15:46:23', 'U1'),
(6, 'Gato', 'U1', '2016-11-22 15:46:23', 'U2'),
(7, 'Hola XYZ', 'U1', '2016-11-22 15:46:23', 'U2'),
(11, 'nada', 'U1', '2016-11-22 15:46:23', 'U2'),
(13, 'Hi', 'U1', '2016-11-22 15:46:23', 'U2'),
(14, 'buenas', 'U1', '2016-11-22 15:46:23', 'U1'),
(15, 'Hola', 'U4', '2016-11-22 15:47:20', 'U1'),
(16, 'vaja', 'U3', '2016-11-22 15:47:41', 'U1'),
(21, 'Que hubo', 'U4', '2016-11-22 23:34:04', 'U1'),
(22, 'Que hubo loco', 'U4', '2016-11-22 23:34:16', 'U1'),
(23, 'gracias por', 'U4', '2016-11-22 23:40:05', 'U1'),
(24, 'GHz', 'U2', '2016-11-22 23:44:44', 'U1'),
(27, 'GHz', 'G2', '2016-11-23 02:37:29', 'U1'),
(28, 'Gogh jan', 'G4', '2016-11-23 03:00:15', 'U4');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE IF NOT EXISTS `usuario` (
  `idusuario` varchar(10) NOT NULL,
  `nick` varchar(45) NOT NULL,
  `password` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`idusuario`, `nick`, `password`) VALUES
('U1', 'loco', 'a123'),
('U2', 'Emili', 'a123'),
('U3', 'jose', 'qw13'),
('U4', 'diego', '1234'),
('U5', 'hugo', '1234567');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario_grupo`
--

CREATE TABLE IF NOT EXISTS `usuario_grupo` (
  `idgrupo` varchar(10) NOT NULL,
  `idusuario` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `usuario_grupo`
--

INSERT INTO `usuario_grupo` (`idgrupo`, `idusuario`) VALUES
('G1', 'U5'),
('G3', 'U5'),
('G2', 'U5'),
('G4', 'U5'),
('G4', 'U4'),
('G3', 'U1');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `grupo`
--
ALTER TABLE `grupo`
 ADD PRIMARY KEY (`idgrupo`), ADD UNIQUE KEY `nombre` (`nombre`);

--
-- Indices de la tabla `mensaje`
--
ALTER TABLE `mensaje`
 ADD PRIMARY KEY (`idmensaje`), ADD KEY `men_fk_idu` (`remitente`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
 ADD PRIMARY KEY (`idusuario`), ADD UNIQUE KEY `nick` (`nick`);

--
-- Indices de la tabla `usuario_grupo`
--
ALTER TABLE `usuario_grupo`
 ADD KEY `usug_fk_idg` (`idgrupo`), ADD KEY `usug_fk_idu` (`idusuario`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `mensaje`
--
ALTER TABLE `mensaje`
MODIFY `idmensaje` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=29;
--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `mensaje`
--
ALTER TABLE `mensaje`
ADD CONSTRAINT `men_fk_idu` FOREIGN KEY (`remitente`) REFERENCES `usuario` (`idusuario`);

--
-- Filtros para la tabla `usuario_grupo`
--
ALTER TABLE `usuario_grupo`
ADD CONSTRAINT `usug_fk_idg` FOREIGN KEY (`idgrupo`) REFERENCES `grupo` (`idgrupo`),
ADD CONSTRAINT `usug_fk_idu` FOREIGN KEY (`idusuario`) REFERENCES `usuario` (`idusuario`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
