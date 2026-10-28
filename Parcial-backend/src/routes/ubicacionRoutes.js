const express = require('express');
const ubicacionController = require('../controllers/ubicacionController');
const { autenticar } = require('../middleware/authMiddleware');

const router = express.Router();

router.post('/ubicaciones', autenticar, ubicacionController.crear);
router.get('/ubicaciones', autenticar, ubicacionController.listar);

module.exports = router;
