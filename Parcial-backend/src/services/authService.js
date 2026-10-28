const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const userRepository = require('../repositories/userRepository');

class AuthError extends Error {
    constructor(message) {
        super(message);
        this.name = 'AuthError';
    }
}

class ValidationError extends Error {
    constructor(message) {
        super(message);
        this.name = 'ValidationError';
    }
}

class ConflictError extends Error {
    constructor(message) {
        super(message);
        this.name = 'ConflictError';
    }
}

const UNIQUE_VIOLATION = '23505';
const USUARIO_MAXIMO = 50;

function buildSession(user) {
    const token = jwt.sign(
        { userId: user.id, usuario: user.usuario },
        process.env.JWT_SECRET,
        { expiresIn: process.env.JWT_EXPIRES_IN || '12h' }
    );

    return {
        token,
        user: {
            id: user.id,
            usuario: user.usuario,
        },
    };
}

async function login(usuario, password) {
    if (typeof usuario !== 'string' || typeof password !== 'string' || !usuario.trim() || !password) {
        throw new ValidationError('El usuario y la contraseña son obligatorios');
    }

    const user = await userRepository.findByUsuario(usuario.trim());
    if (!user) {
        throw new AuthError('Usuario o contraseña incorrectos');
    }

    const passwordMatches = await bcrypt.compare(password, user.password_hash);
    if (!passwordMatches) {
        throw new AuthError('Usuario o contraseña incorrectos');
    }

    return buildSession(user);
}

async function register(usuario, password) {
    if (typeof usuario !== 'string' || typeof password !== 'string' || !usuario.trim() || !password) {
        throw new ValidationError('El usuario y la contraseña son obligatorios');
    }

    const nombreUsuario = usuario.trim();

    if (nombreUsuario.length > USUARIO_MAXIMO) {
        throw new ValidationError('El usuario no puede pasar de 50 caracteres');
    }

    const existente = await userRepository.findByUsuario(nombreUsuario);
    if (existente) {
        throw new ConflictError('El usuario ya existe');
    }

    const passwordHash = await bcrypt.hash(password, 10);

    try {
        const creado = await userRepository.create({ usuario: nombreUsuario, passwordHash });
        return buildSession(creado);
    } catch (err) {
        if (err.code === UNIQUE_VIOLATION) {
            throw new ConflictError('El usuario ya existe');
        }
        throw err;
    }
}

module.exports = { login, register, AuthError, ValidationError, ConflictError };
