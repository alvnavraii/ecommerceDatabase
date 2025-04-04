const { request, expect, baseUrl, getToken, beforeAll } = require('../utils/testSetup');

describe('Test de tags', () => {
    before(async () => {
        await beforeAll();
    });

    it('Debería traer todas los tags', async () => {
       
        
        const response = await request(baseUrl)
            .get('/tags/all')
            .set('Authorization', `Bearer ${getToken()}`);
        
        expect(response.status).to.equal(200);
        expect(response.body).to.be.an('array');
        expect(response.body).to.not.be.empty;
        response.body.forEach(item => {
            expect(item).to.be.an('object');
            expect(item).to.have.property('id');
            expect(item.id).to.be.a('number');
        });
    })

    it('debería traer todos los tags activos', async () => {
        const response = await request(baseUrl)
        .get('/tags')
        .set('Authorization', `Bearer ${getToken()}`);

        expect(response.status).to.equal(200);
        expect(response.body).to.be.an('array');
        expect(response.body).to.not.be.empty;
        response.body.forEach(item => {
            expect(item).to.be.an('object');
            expect(item).to.have.property('active');
            expect(item.active).to.equal(true);
        })
    })

    it('No debería encontrar un tag por id si este está inactivo', async () => {
        const response = await request(baseUrl)
        .get('/tags/4') //id de un tag inactivo
        .set('Authorization', `Bearer ${getToken()}`);

        expect(response.status).to.equal(404);
    })

    it('Debería encontrar un tag por id si este está activo', async () => {
        const response = await request(baseUrl)
        .get('/tags/1') //id de un tag activo
        .set('Authorization', `Bearer ${getToken()}`);

        expect(response.status).to.equal(200);
        expect(response.body).to.be.an('object');
        expect(response.body).to.have.property('id');
        expect(response.body.id).to.be.a('number');
    })

    it('Debería reactivar un tag', async () => {
        const response = await request(baseUrl)
        .put('/tags/2/reactivate')
        .set('Authorization', `Bearer ${getToken()}`);

        expect(response.status).to.equal(200);
        expect(response.body).to.be.an('object');
        expect(response.body).to.have.property('active');
        expect(response.body.active).to.equal(true);
    })

    it('Debería crear un tag o indicar que ya existe', async () => {
        const response = await request(baseUrl)
        .post('/tags')
        .send({
            name: 'Cromoterapia III',
        })
        .set('Authorization', `Bearer ${getToken()}`);

        // Validar que el status sea 201 o 409
        expect(response.status).to.be.oneOf([201, 409]);

        if (response.status === 201) {
            // Validar respuesta de creación exitosa
            expect(response.body).to.be.an('object');
            expect(response.body).to.have.property('name');
            expect(response.body.name).to.equal('Cromoterapia III');
            expect(response.body).to.have.property('active');
            expect(response.body.active).to.equal(true);
        } else {
            // Validar respuesta de conflicto
            expect(response.body).to.be.an('object');
            expect(response.body).to.have.property('message');
            expect(response.body.message).to.be.a('string');
        }
    })

    it('Debería actualizar un tag', async () => {
        const response = await request(baseUrl)
        .put('/tags/8')
        .send({
            name: 'Cromoterapia China'})
        .set('Authorization', `Bearer ${getToken()}`);

        expect(response.status).to.equal(200);
        expect(response.body).to.be.an('object');
        expect(response.body).to.have.property('name');
        expect(response.body.name).to.equal('Cromoterapia China');
    })

    it('Debería eliminar un tag', async () => {
        const response = await request(baseUrl)
        .delete('/tags/8')
        .set('Authorization', `Bearer ${getToken()}`);

        expect(response.status).to.equal(200);
        expect(response.body).to.be.an('object');
        expect(response.body).to.have.property('active');
        expect(response.body.active).to.equal(false);
    })
  // tus tests aquí
});