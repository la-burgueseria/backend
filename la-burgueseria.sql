

CREATE TABLE `usuario` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `documento` varchar(12),
  `nombre` varchar(40),
  `apellido` varchar(40),
  `contrasena` varchar(150),
  `rol_id` int,
  `estado` boolean
);

CREATE TABLE `rol` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `nombre` varchar(20),
  `estado` boolean
);

CREATE TABLE `permiso` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `nombre` varchar(50),
  `url` varchar(250),
  `estado` boolean
);

CREATE TABLE `permiso_por_rol` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `rol_id` int,
  `permiso_id` int
);

CREATE TABLE `insumo` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `nombre` varchar(80),
  `cantidad` int
);

CREATE TABLE `insumos_por_producto` (
  `int` int PRIMARY KEY AUTO_INCREMENT,
  `insumo_id` int,
  `producto_id` int
);

CREATE TABLE `producto` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `nombre` varchar(60),
  `precio` double,
  `categoria_producto_id` int,
  `imagen` varchar(250),
  `descripcion` varchar(100)
);

CREATE TABLE `mesero` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `documento` varchar(12),
  `nombre` varchar(40),
  `apellido` varchar(40)
);

CREATE TABLE `mesa` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `qr_id` int,
  `estado_mesa_id` int
);

CREATE TABLE `estado_mesa` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `nombre` varchar(30)
);

CREATE TABLE `categoria_producto` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `nombre` varchar(20)
);

CREATE TABLE `qr` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `ruta` varchar(250),
  `url` varchar(250)
);

CREATE TABLE `productos_por_mesa` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `producto_id` int,
  `mesa_id` int
);

CREATE TABLE `cuenta` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `productos_por_mesa_id` int,
  `mesero_id` int,
  `total` int,
  `estado_cuenta` int,
  `fecha` timestamp
);

CREATE TABLE `estado_cuenta` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `nombre` varchar(50)
);

CREATE TABLE `ingreso` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `fecha` timestamp,
  `metodo_pago` varchar(20),
  `cuenta_id` int,
  `total` int
);

CREATE TABLE `egreso` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `fecha` timestamp,
  `total` int,
  `tipo_egreso` int
);

CREATE TABLE `tipo_egreso` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `nombre` varchar(20)
);

CREATE TABLE `nomina_semanal` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `dias_laborados` int,
  `fecha_inicio` date,
  `fecha_fin` date,
  `mesero_id` int,
  `salario_diario` int,
  `adelanto` int,
  `total` int
);

CREATE TABLE `reporte_mensual` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `egreso_id` int,
  `ingreso_id` int,
  `fecha_inicio` date,
  `fecha_fin` date,
  `ruta` varchar(250)
);

ALTER TABLE `usuario` ADD FOREIGN KEY (`rol_id`) REFERENCES `rol` (`id`);

ALTER TABLE `permiso_por_rol` ADD FOREIGN KEY (`rol_id`) REFERENCES `rol` (`id`);

ALTER TABLE `permiso_por_rol` ADD FOREIGN KEY (`permiso_id`) REFERENCES `permiso` (`id`);

ALTER TABLE `insumos_por_producto` ADD FOREIGN KEY (`insumo_id`) REFERENCES `insumo` (`id`);

ALTER TABLE `insumos_por_producto` ADD FOREIGN KEY (`producto_id`) REFERENCES `producto` (`id`);

ALTER TABLE `producto` ADD FOREIGN KEY (`categoria_producto_id`) REFERENCES `categoria_producto` (`id`);

ALTER TABLE `mesa` ADD FOREIGN KEY (`estado_mesa_id`) REFERENCES `estado_mesa` (`id`);

ALTER TABLE `mesa` ADD FOREIGN KEY (`qr_id`) REFERENCES `qr` (`id`);

ALTER TABLE `productos_por_mesa` ADD FOREIGN KEY (`producto_id`) REFERENCES `producto` (`id`);

ALTER TABLE `productos_por_mesa` ADD FOREIGN KEY (`mesa_id`) REFERENCES `mesa` (`id`);

ALTER TABLE `cuenta` ADD FOREIGN KEY (`productos_por_mesa_id`) REFERENCES `productos_por_mesa` (`id`);

ALTER TABLE `cuenta` ADD FOREIGN KEY (`estado_cuenta`) REFERENCES `estado_cuenta` (`id`);

ALTER TABLE `cuenta` ADD FOREIGN KEY (`mesero_id`) REFERENCES `mesero` (`id`);

ALTER TABLE `reporte_mensual` ADD FOREIGN KEY (`egreso_id`) REFERENCES `egreso` (`id`);

ALTER TABLE `reporte_mensual` ADD FOREIGN KEY (`ingreso_id`) REFERENCES `ingreso` (`id`);

ALTER TABLE `egreso` ADD FOREIGN KEY (`tipo_egreso`) REFERENCES `tipo_egreso` (`id`);

ALTER TABLE `ingreso` ADD FOREIGN KEY (`cuenta_id`) REFERENCES `cuenta` (`id`);

ALTER TABLE `nomina_semanal` ADD FOREIGN KEY (`adelanto`) REFERENCES `egreso` (`id`);

ALTER TABLE `nomina_semanal` ADD FOREIGN KEY (`mesero_id`) REFERENCES `mesero` (`id`);
