
const { request, expect, baseUrl, getToken, beforeAll } = require('../utils/testSetup');

describe('Test de categories', () => {
    before(async () => {
        await beforeAll();
    });

    it('Debería traer todas las categorías', async () => {
       
        
        const response = await request(baseUrl)
            .get('/categories/all')
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

    it('debería traer todas los categorías activas', async () => {
        const response = await request(baseUrl)
        .get('/categories')
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

     it('No debería encontrar una categoría por id si este está inactiva', async () => {
        const response = await request(baseUrl)
        .get('/categories/2') //id de un tag inactivo
        .set('Authorization', `Bearer ${getToken()}`);

        expect(response.status).to.equal(404);
    })

    it('Debería encontrar una categoría por id si este está activa ', async () => {
        const response = await request(baseUrl)
        .get('/categories/1') //id de un tag activo
        .set('Authorization', `Bearer ${getToken()}`);

        expect(response.status).to.equal(200);
        expect(response.body).to.be.an('object');
        expect(response.body).to.have.property('id');
        expect(response.body.id).to.be.a('number');
    })

   it('Debería reactivar una categoría', async () => {
        const response = await request(baseUrl)
        .put('/categories/4/reactivate')
        .set('Authorization', `Bearer ${getToken()}`);

        expect(response.status).to.equal(200);
        expect(response.body).to.be.an('object');
        expect(response.body).to.have.property('active');
        expect(response.body.active).to.equal(true);
    })

    it('Debería crear una categoría o indicar si ya existe o indicar que ya existe', async () => {
        const response = await request(baseUrl)
        .post('/categories')
        .send({
            name: 'Medicina Tradicional China',
        })
        .set('Authorization', `Bearer ${getToken()}`);

        // Validar que el status sea 201 o 409
        expect(response.status).to.be.oneOf([201, 409]);

        if (response.status === 201) {
            // Validar respuesta de creación exitosa
            expect(response.body).to.be.an('object');
            expect(response.body).to.have.property('name');
            expect(response.body.name).to.equal('Medicina Tradicional China');
            expect(response.body).to.have.property('active');
            expect(response.body.active).to.equal(true);
        } else {
            // Validar respuesta de conflicto
            expect(response.body).to.be.an('object');
            expect(response.body).to.have.property('message');
            expect(response.body.message).to.be.a('string');
        }
    })

    it('Debería actualizar una categoría', async () => {
        const response = await request(baseUrl)
        .put('/categories/4')
        .send({
            name: 'Somnolencia Dormilona'})
        .set('Authorization', `Bearer ${getToken()}`);

        expect(response.status).to.equal(200);
        expect(response.body).to.be.an('object');
        expect(response.body).to.have.property('name');
        expect(response.body.name).to.equal('Somnolencia Dormilona');
    })

    it('Debería eliminar un una categoría', async () => {
        const response = await request(baseUrl)
        .delete('/categories/4')
        .set('Authorization', `Bearer ${getToken()}`);

        expect(response.status).to.equal(200);
        expect(response.body).to.be.an('object');
        expect(response.body).to.have.property('active');
        expect(response.body.active).to.equal(false);
    }) 
});
