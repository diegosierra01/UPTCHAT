-- phpMyAdmin SQL Dump
-- version 4.2.7.1
-- http://www.phpmyadmin.net
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 12-11-2016 a las 20:13:09
-- Versión del servidor: 5.6.20
-- Versión de PHP: 5.5.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de datos: `uptchat`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `grupo`
--

CREATE TABLE IF NOT EXISTS `grupo` (
`id` int(11) NOT NULL,
  `nombre` varchar(250) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

--
-- Volcado de datos para la tabla `grupo`
--

INSERT INTO `grupo` (`id`, `nombre`) VALUES
(1, 'grupo1'),
(2, 'grupo 2'),
(3, 'grupo3');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `mensaje`
--

CREATE TABLE IF NOT EXISTS `mensaje` (
`id` int(11) NOT NULL,
  `cadena` varchar(250) NOT NULL,
  `remitente` varchar(250) NOT NULL,
  `destinatario` varchar(250) NOT NULL,
  `fecha` varchar(120) NOT NULL,
  `hora` varchar(120) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=35 ;

--
-- Volcado de datos para la tabla `mensaje`
--

INSERT INTO `mensaje` (`id`, `cadena`, `remitente`, `destinatario`, `fecha`, `hora`) VALUES
(1, 'msj quemado', 'U1', 'U2', '9/11/2016', '6:46 P.M'),
(2, 'Hola', 'U1', 'U2', '9/11/2016', '6:29 P.M'),
(3, 'sin embargo', 'U2', 'U1', '9/11/2016', '6:51 A.M'),
(5, 'Perro', 'U1', 'U1', '9/11/2016', '7:17 A.M'),
(6, 'Gato', 'U1', 'U2', '9/11/2016', '7:22 AM'),
(7, 'Hola XYZ', 'U1', 'U2', '9/11/2016', '7:23 AM'),
(11, 'nada', 'U1', 'U2', '9/11/2016', '7:32 A.M'),
(13, 'Hi', 'U1', 'U2', '10/11/2016', '9:21 A.M'),
(14, 'buenas', 'U1', 'U1', '10/11/2016', '9:21 A.M'),
(31, 'Hi', 'U1', 'U5', '11/11/2016', '8:48 P.M'),
(32, 'Hi gusta', 'U1', 'U5', '11/11/2016', '8:49 P.M'),
(33, 'Hola', 'U1', 'U3', '11/11/2016', '8:50 P.M'),
(34, 'Ho grupo1', 'U1', '2', '12/11/2016', '0:13 P.M');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE IF NOT EXISTS `usuario` (
  `id` varchar(250) NOT NULL,
  `nick` varchar(250) NOT NULL,
  `password` varchar(250) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`id`, `nick`, `password`) VALUES
('U1', 'loco', 'a123'),
('U2', 'Emili', 'a123'),
('U3', 'jose', 'qw13'),
('U4', 'dieho', '1234'),
('U5', 'hugo', '1234567');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario_grupo`
--

CREATE TABLE IF NOT EXISTS `usuario_grupo` (
  `id_usuario` varchar(250) NOT NULL,
  `id_grupo` varchar(250) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `usuario_grupo`
--

INSERT INTO `usuario_grupo` (`id_usuario`, `id_grupo`) VALUES
('U5', '3'),
('U4', '1'),
('U5', '2'),
('U2', '3');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `grupo`
--
ALTER TABLE `grupo`
 ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `mensaje`
--
ALTER TABLE `mensaje`
 ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
 ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `grupo`
--
ALTER TABLE `grupo`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT de la tabla `mensaje`
--
ALTER TABLE `mensaje`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=35;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
