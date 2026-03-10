-- Script de datos iniciales para la base de datos

CREATE DATABASE IF NOT EXISTS foro_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE foro_db;

-- Insertar un usuario de prueba
-- Contraseña: 123456 (encriptada con BCrypt)
INSERT INTO usuarios (login, clave) VALUES
    ('admin@foro.com', '$2a$10$Y50UaMFOxteibQEYLrwuHeehHYfcoafCopUazP/s/ltelpcwHjeAm')
ON DUPLICATE KEY UPDATE login = login;

-- Nota: para generar tu propio hash BCrypt en Java:
-- new BCryptPasswordEncoder().encode("tu_password")
