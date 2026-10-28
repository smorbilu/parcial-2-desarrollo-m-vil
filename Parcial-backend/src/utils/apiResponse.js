function success(res, { message = 'OK', body = null, statusCode = 200 } = {}) {
    return res.status(statusCode).json({
        standardResponse: {
            httpCode: statusCode,
            message,
        },
        body,
    });
}

function error(res, { message = 'Ocurrió un error', statusCode = 400, body = null } = {}) {
    return res.status(statusCode).json({
        standardResponse: {
            httpCode: statusCode,
            message,
        },
        body,
    });
}

module.exports = { success, error };
