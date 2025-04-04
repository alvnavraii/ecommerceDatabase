const { request, expect, baseUrl, getToken, beforeAll } = require('../utils/testSetup');

describe('Test de videos', () => {
    before(async () => {
        await beforeAll();
    });

    it('Debería traer todos los videos', async () => {
        const response = await request(baseUrl)
            .get('/videos/all')
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

    it('debería traer todos los vídeos activos', async () => {
        const response = await request(baseUrl)
        .get('/videos')
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

    it('No debería encontrar un video por id si este está inactivo', async () => {
        const response = await request(baseUrl)
        .get('/videos/2') //id de un tag inactivo
        .set('Authorization', `Bearer ${getToken()}`);

        expect(response.status).to.equal(404);
    })

     it('Debería encontrar un video por id si este está activo ', async () => {
        const response = await request(baseUrl)
        .get('/videos/1') //id de un tag activo
        .set('Authorization', `Bearer ${getToken()}`);

        expect(response.status).to.equal(200);
        expect(response.body).to.be.an('object');
        expect(response.body).to.have.property('id');
        expect(response.body.id).to.be.a('number');
    })

    it('Debería reactivar un vídeo', async () => {
        const response = await request(baseUrl)
        .put('/videos/1/reactivate')
        .set('Authorization', `Bearer ${getToken()}`);

        expect(response.status).to.equal(200);
        expect(response.body).to.be.an('object');
        expect(response.body).to.have.property('active');
        expect(response.body.active).to.equal(true);
    })

   it('Debería crear un vídeo o indicar si ya existe o indicar que ya existe', async () => {
        const response = await request(baseUrl)
        .post('/videos')
        .send({
            title: 'Técnicas modernas de cromoterapia',
            description: 'Expone las técnicas modernas de cromoterapia',
            price: 10.99,
            uploadDate: new Date().toISOString(),
            filePath: 'https://www.youtube.com/watch?v=dQw4w9WgXcQ'
        })
        .set('Authorization', `Bearer ${getToken()}`);

        // Validar que el status sea 201 o 409
        expect(response.status).to.be.oneOf([201, 409]);

        if (response.status === 201) {
            // Validar respuesta de creación exitosa
            expect(response.body).to.be.an('object');
            expect(response.body).to.have.property('title');
            expect(response.body.name).to.equal('Técnicas modernas de cromoterapia');
            expect(response.body).to.have.property('active');
            expect(response.body.active).to.equal(true);
        } else {
            // Validar respuesta de conflicto
            expect(response.body).to.be.an('object');
            expect(response.body).to.have.property('message');
            expect(response.body.message).to.be.a('string');
        }
    })

    it('Debería actualizar un vídeo', async () => {
        const response = await request(baseUrl)
        .put('/videos/1')
        .send({
            title: 'Depresión, angustia y ansiedad'})
        .set('Authorization', `Bearer ${getToken()}`);

        expect(response.status).to.equal(200);
        expect(response.body).to.be.an('object');
        expect(response.body).to.have.property('title');
        expect(response.body.title).to.equal('Depresión, angustia y ansiedad');
    })

    it('Debería eliminar un borrar un vídeo', async () => {
        const response = await request(baseUrl)
        .delete('/videos/2')
        .set('Authorization', `Bearer ${getToken()}`);

        expect(response.status).to.equal(200);
        expect(response.body).to.be.an('object');
        expect(response.body).to.have.property('active');
        expect(response.body.active).to.equal(false);
    })



   

});
