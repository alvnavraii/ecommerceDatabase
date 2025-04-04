const request = require('supertest');
const { expect } = require('chai');
const baseUrl = process.env.BASE_URL || 'http://localhost:8080/api/v1';

let token;

const initializeToken = async () => {
    try {
        const response = await request(baseUrl)
            .post('/auth/login')
            .send({
                email: 'alvnavra@gmail.com',
                password: 'Temporal1!'
            });
        
        if (!response.body.token) {
            throw new Error(`No se recibió el token en la respuesta. Status: ${response.status}`);
        }
        
        token = response.body.token;
        return token;
    } catch (error) {
        throw error;
    }
};

const beforeAll = async () => {
    if (!token) {
        token = await initializeToken();
    }
    return token;
};

module.exports = {
    request,
    expect,
    baseUrl,
    getToken: () => {
        if (!token) {
            throw new Error('Token no inicializado. Asegúrate de llamar a beforeAll primero.');
        }
        return token;
    },
    beforeAll
}; 