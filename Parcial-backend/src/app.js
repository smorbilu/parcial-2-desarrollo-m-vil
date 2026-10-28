const express = require('express');
const cors = require('cors');
const authRoutes = require('./routes/authRoutes');
const ubicacionRoutes = require('./routes/ubicacionRoutes');
const apiResponse = require('./utils/apiResponse');

const app = express();

app.use(cors());
app.use(express.json());

app.get('/api/health', (req, res) => {
    apiResponse.success(res, { message: 'API de Parcial funcionando correctamente' });
});

app.use('/api', authRoutes);
app.use('/api', ubicacionRoutes);

app.use((req, res) => {
    apiResponse.error(res, { message: 'Ruta no encontrada', statusCode: 404 });
});

app.use((err, req, res, next) => {
    if (err.type === 'entity.parse.failed') {
        return apiResponse.error(res, { message: 'El JSON enviado no es válido', statusCode: 400 });
    }
    console.error(err);
    return apiResponse.error(res, { message: 'Error interno del servidor', statusCode: 500 });
});

module.exports = app;
