DROP TABLE IF EXISTS ubicaciones;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    usuario VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL
);

CREATE UNIQUE INDEX users_usuario_lower_key ON users (LOWER(usuario));

CREATE TABLE ubicaciones (
    id SERIAL PRIMARY KEY,
    usuario_id INTEGER NOT NULL REFERENCES users(id),
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(500) NOT NULL DEFAULT '',
    latitud DOUBLE PRECISION NOT NULL CHECK (latitud BETWEEN -90 AND 90),
    longitud DOUBLE PRECISION NOT NULL CHECK (longitud BETWEEN -180 AND 180),
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX ubicaciones_usuario_id_idx ON ubicaciones (usuario_id);

INSERT INTO users (usuario, password_hash)
VALUES ('parcial', '$2a$10$hjfWXwG9deBvYVCZaDB/EegNtAiCXIVadJAZUnUCMotaxGceZ2a2e');
