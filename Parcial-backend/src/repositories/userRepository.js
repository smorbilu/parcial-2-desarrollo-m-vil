const pool = require('../config/db');

async function findByUsuario(usuario) {
    const result = await pool.query(
        `SELECT id, usuario, password_hash
         FROM users
         WHERE LOWER(usuario) = LOWER($1)
         LIMIT 1`,
        [usuario]
    );
    return result.rows[0] || null;
}

async function create({ usuario, passwordHash }) {
    const result = await pool.query(
        `INSERT INTO users (usuario, password_hash)
         VALUES ($1, $2)
         RETURNING id, usuario`,
        [usuario, passwordHash]
    );
    return result.rows[0];
}

module.exports = { findByUsuario, create };
