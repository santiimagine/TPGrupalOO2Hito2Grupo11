-- Epicentro Gourmet - Hito 2 - Carga de datos iniciales
--
-- ORDEN DE USO:
--   1) Ejecutar el CREATE DATABASE (primeras lineas) para crear la base.
--   2) Levantar la aplicacion una vez: Hibernate crea las tablas solo.
--   3) Ejecutar el resto del script (desde USE) UNA SOLA VEZ.
--
-- Si se ejecuta todo junto antes del paso 2, fallan los INSERT porque
-- las tablas todavia no existen.
--
-- Usuario administrador de prueba:
--   mail: admin@epicentrogourmet.com  |  contrasenia: admin1234

CREATE DATABASE IF NOT EXISTS epicentro_gourmet CHARACTER SET utf8mb4;

USE epicentro_gourmet;

-- Administrador (precargado). Se puede correr mas de una vez sin duplicarse.
INSERT IGNORE INTO usuario (mail, contrasenia, created_at, updated_at)
VALUES ('admin@epicentrogourmet.com',
        '$2a$10$TRM1DmgExxq8q81y2w2DuODcLgdXRqiMNQdb2ri7upXAPJN0RErSW',
        NOW(), NOW());

INSERT IGNORE INTO user_rol (usuario_id, rol, created_at, updated_at)
SELECT id, 'ADMIN', NOW(), NOW()
FROM usuario
WHERE mail = 'admin@epicentrogourmet.com';


-- Festivales de prueba: pasado (9 dias), activo hoy (30 dias) y futuro (41 dias).
-- OJO: este bloque NO evita duplicados. Si se ejecuta dos veces hay que
-- rehacer la base.
INSERT INTO festival (nombre, temporada, fecha_inicio, fecha_fin) VALUES
('Festival de Primavera', 'Primavera', '2026-09-21', '2026-09-30'),
('Festival Parrilla y Sabores', 'Otono', DATE_SUB(CURDATE(), INTERVAL 5 DAY), DATE_ADD(CURDATE(), INTERVAL 25 DAY)),
('Festival de Verano', 'Verano', '2027-01-10', '2027-02-20');