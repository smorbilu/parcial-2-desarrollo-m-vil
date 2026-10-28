const ubicacionRepository = require('../repositories/ubicacionRepository');

class UbicacionValidationError extends Error {
    constructor(message) {
        super(message);
        this.name = 'UbicacionValidationError';
    }
}

function textoValido(valor) {
    return typeof valor === 'string' && valor.trim().length > 0;
}

function coordenadaValida(valor, limite) {
    return typeof valor === 'number' && Number.isFinite(valor) && Math.abs(valor) <= limite;
}

async function crear(datos, usuarioId) {
    const { nombre, descripcion, latitud, longitud } = datos || {};

    if (!textoValido(nombre)) {
        throw new UbicacionValidationError('El nombre de la ubicación es obligatorio');
    }

    if (nombre.trim().length > 100) {
        throw new UbicacionValidationError('El nombre no puede pasar de 100 caracteres');
    }

    if (descripcion !== undefined && descripcion !== null && typeof descripcion !== 'string') {
        throw new UbicacionValidationError('La descripción no es válida');
    }

    const descripcionLimpia = (descripcion || '').trim();

    if (descripcionLimpia.length > 500) {
        throw new UbicacionValidationError('La descripción no puede pasar de 500 caracteres');
    }

    if (!coordenadaValida(latitud, 90) || !coordenadaValida(longitud, 180)) {
        throw new UbicacionValidationError('Las coordenadas de la ubicación no son válidas');
    }

    return ubicacionRepository.create({
        usuarioId,
        nombre: nombre.trim(),
        descripcion: descripcionLimpia,
        latitud,
        longitud,
    });
}

async function listar(usuarioId) {
    return ubicacionRepository.findByUsuario(usuarioId);
}

module.exports = { crear, listar, UbicacionValidationError };
