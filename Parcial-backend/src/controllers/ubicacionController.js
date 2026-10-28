const ubicacionService = require('../services/ubicacionService');
const apiResponse = require('../utils/apiResponse');

async function crear(req, res) {
    try {
        const ubicacion = await ubicacionService.crear(req.body, req.user.userId);
        return apiResponse.success(res, {
            message: 'Ubicación guardada correctamente',
            body: ubicacion,
            statusCode: 201,
        });
    } catch (err) {
        if (err instanceof ubicacionService.UbicacionValidationError) {
            return apiResponse.error(res, {
                message: err.message,
                statusCode: 400,
            });
        }
        console.error(err);
        return apiResponse.error(res, {
            message: 'Error interno del servidor',
            statusCode: 500,
        });
    }
}

async function listar(req, res) {
    try {
        const ubicaciones = await ubicacionService.listar(req.user.userId);
        return apiResponse.success(res, {
            message: 'Ubicaciones obtenidas correctamente',
            body: ubicaciones,
        });
    } catch (err) {
        console.error(err);
        return apiResponse.error(res, {
            message: 'Error interno del servidor',
            statusCode: 500,
        });
    }
}

module.exports = { crear, listar };
