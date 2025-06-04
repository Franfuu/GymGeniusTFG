-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         11.3.1-MariaDB - mariadb.org binary distribution
-- SO del servidor:              Win64
-- HeidiSQL Versión:             12.3.0.6589
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Volcando estructura de base de datos para gymgenius
CREATE DATABASE IF NOT EXISTS `gymgenius` /*!40100 DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci */;
USE `gymgenius`;

-- Volcando estructura para tabla gymgenius.clases
CREATE TABLE IF NOT EXISTS `clases` (
  `id_clase` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) DEFAULT NULL,
  `descripcion` text DEFAULT NULL,
  `id_empleado` int(11) DEFAULT NULL,
  `id_sala` int(11) DEFAULT NULL,
  `hora_inicio` time DEFAULT NULL,
  `hora_fin` time DEFAULT NULL,
  `dias_semana` set('Lunes','Martes','Miércoles','Jueves','Viernes','Sábado','Domingo') DEFAULT NULL,
  PRIMARY KEY (`id_clase`),
  KEY `id_empleado` (`id_empleado`),
  KEY `id_sala` (`id_sala`),
  CONSTRAINT `clases_ibfk_1` FOREIGN KEY (`id_empleado`) REFERENCES `empleados` (`id_empleado`),
  CONSTRAINT `clases_ibfk_2` FOREIGN KEY (`id_sala`) REFERENCES `salas` (`id_sala`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- La exportación de datos fue deseleccionada.

-- Volcando estructura para tabla gymgenius.clientes
CREATE TABLE IF NOT EXISTS `clientes` (
  `id_cliente` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) DEFAULT NULL,
  `apellido` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  `fecha_registro` date DEFAULT curdate(),
  `estado_membresia` enum('activo','suspendido') DEFAULT 'activo',
  `contraseña` varchar(30) DEFAULT NULL,
  `foto` longblob DEFAULT NULL,
  PRIMARY KEY (`id_cliente`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- La exportación de datos fue deseleccionada.

-- Volcando estructura para tabla gymgenius.empleados
CREATE TABLE IF NOT EXISTS `empleados` (
  `id_empleado` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) DEFAULT NULL,
  `apellido` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `fecha_contratacion` date DEFAULT NULL,
  `contraseña` varchar(30) DEFAULT NULL,
  `foto` longblob DEFAULT NULL,
  PRIMARY KEY (`id_empleado`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- La exportación de datos fue deseleccionada.

-- Volcando estructura para tabla gymgenius.inscripciones_clases
CREATE TABLE IF NOT EXISTS `inscripciones_clases` (
  `id_inscripcion` int(11) NOT NULL AUTO_INCREMENT,
  `id_cliente` int(11) DEFAULT NULL,
  `id_clase` int(11) DEFAULT NULL,
  `fecha_inscripcion` date DEFAULT curdate(),
  PRIMARY KEY (`id_inscripcion`),
  KEY `id_cliente` (`id_cliente`),
  KEY `id_clase` (`id_clase`),
  CONSTRAINT `inscripciones_clases_ibfk_1` FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id_cliente`),
  CONSTRAINT `inscripciones_clases_ibfk_2` FOREIGN KEY (`id_clase`) REFERENCES `clases` (`id_clase`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- La exportación de datos fue deseleccionada.

-- Volcando estructura para tabla gymgenius.maquinas
CREATE TABLE IF NOT EXISTS `maquinas` (
  `id_maquina` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) DEFAULT NULL,
  `marca` varchar(100) DEFAULT NULL,
  `modelo` varchar(100) DEFAULT NULL,
  `id_sala` int(11) DEFAULT NULL,
  `foto` longblob DEFAULT NULL,
  PRIMARY KEY (`id_maquina`),
  KEY `id_sala` (`id_sala`),
  CONSTRAINT `maquinas_ibfk_1` FOREIGN KEY (`id_sala`) REFERENCES `salas` (`id_sala`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- La exportación de datos fue deseleccionada.

-- Volcando estructura para tabla gymgenius.salas
CREATE TABLE IF NOT EXISTS `salas` (
  `id_sala` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) DEFAULT NULL,
  `capacidad_maxima` int(11) DEFAULT NULL,
  `ubicacion` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id_sala`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- La exportación de datos fue deseleccionada.

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
