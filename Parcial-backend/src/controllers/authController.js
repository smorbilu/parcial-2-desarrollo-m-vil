const authService = require('../services/authService');
const apiResponse = require('../utils/apiResponse');

function responderError(res, err) {
    if (err instanceof authService.ValidationError) {
        return apiResponse.error(res, { message: err.message, statusCode: 400 });
    }
    if (err instanceof authService.AuthError) {
        return apiResponse.error(res, { message: err.message, statusCode: 401 });
    }
    if (err instanceof authService.ConflictError) {
        return apiResponse.error(res, { message: err.message, statusCode: 409 });
    }
    console.error(err);
    return apiResponse.error(res, { message: 'Error interno del servidor', statusCode: 500 });
}

async function login(req, res) {
    const { usuario, password } = req.body || {};

    try {
        const result = await authService.login(usuario, password);
        return apiResponse.success(res, {
            message: 'Login exitoso',
            body: result,
        });
    } catch (err) {
        return responderError(res, err);
    }
}

async function register(req, res) {
    const { usuario, password } = req.body || {};

    try {
        const result = await authService.register(usuario, password);
        return apiResponse.success(res, {
            message: 'Usuario registrado correctamente',
            body: result,
            statusCode: 201,
        });
    } catch (err) {
        return responderError(res, err);
    }
}

module.exports = { login, register };
