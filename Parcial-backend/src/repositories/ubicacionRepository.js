const pool = require('../config/db');

const COLUMNAS = `
    id,
    nombre,
    descripcion,
    latitud,
    longitud,
    created_at AS "creadoEn"`;

async function create({ usuarioId, nombre, descripcion, latitud, longitud }) {
    const result = await pool.query(
        `INSERT INTO ubicaciones (usuario_id, nombre, descripcion, latitud, longitud)
         VALUES ($1, $2, $3, $4, $5)
         RETURNING ${COLUMNAS}`,
        [usuarioId, nombre, descripcion, latitud, longitud]
    );
    return result.rows[0];
}

async function findByUsuario(usuarioId) {
    const result = await pool.query(
        `SELECT ${COLUMNAS}
         FROM ubicaciones
         WHERE usuario_id = $1
         ORDER BY id DESC`,
        [usuarioId]
    );
    return result.rows;
}

module.exports = { create, findByUsuario };
