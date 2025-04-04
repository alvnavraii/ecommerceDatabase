const { request, expect, baseUrl, getToken, beforeAll } = require('../utils/testSetup');

describe('Test de perfil', () => {

    before(async () => {
        await beforeAll();
    });

    it('Debería traer todos los perfiles', async () => {
        const response = await request(baseUrl)
        .get('/profiles/all')
        .set('Authorization', `Bearer ${getToken()}`);

        expect(response.status).to.equal(200);
        expect(response.body).to.be.an('array');
        expect(response.body).to.not.be.empty;
        response.body.forEach(item => {
            expect(item).to.be.an('object');
            expect(item).to.have.property('id');
            expect(item.id).to.be.a('number');
            expect(item).to.have.property('firstName');
            expect(item.firstName).to.be.a('string');
        })
    })

    it('Debería traer todos los perfiles activos', async () => {
        const response = await request(baseUrl)
        .get('/profiles')
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

    it('No debeería encontrar un id si está inactivo', async () => {
        const response = await request(baseUrl)
        .get('/profiles/3') //id de un perfil inactivo
        .set('Authorization', `Bearer ${getToken()}`);

        expect(response.status).to.equal(404);
    })

    it('Debería encontrar un id si está activo', async () => {
        const response = await request(baseUrl)
        .get('/profiles/1') //id de un perfil activo
        .set('Authorization', `Bearer ${getToken()}`);

        expect(response.status).to.equal(200);
        expect(response.body).to.be.an('object');
        expect(response.body).to.have.property('id');
        expect(response.body.id).to.be.a('number');
        expect(response.body).to.have.property('firstName');
        expect(response.body.firstName).to.be.a('string');

    })

    it('Debería reactivar un perfil', async () => {
        const response = await request(baseUrl)
        .put('/profiles/4/reactivate')
        .set('Authorization', `Bearer ${getToken()}`);

        expect(response.status).to.equal(200);
        expect(response.body).to.be.an('object');
        expect(response.body).to.have.property('active');
        expect(response.body.active).to.equal(true);
    })

    it('Debería crear un perfil o indicar que ya existe', async () => {
        const response = await request(baseUrl)
        .post('/profiles')
        .send({
            userId:2,
            firstName: 'Slender',
            lastName: 'Man',
        })
        .set('Authorization', `Bearer ${getToken()}`);

        expect(response.status).to.be.oneOf([201, 409]);

        if (response.status === 201) {
            expect(response.body).to.be.an('object');
            expect(response.body).to.have.property('firstName');
            expect(response.body.firstName).to.equal('Slender');
            expect(response.body).to.have.property('lastName');
            expect(response.body.lastName).to.equal('Man');
        } else {
            expect(response.body).to.be.an('object');
            expect(response.body).to.have.property('message');
            expect(response.body.message).to.be.a('string');
        }
    })

    it('Debería actualizar un profile', async () => {
        const response = await request(baseUrl)
        .put('/profiles/4')
        .send({
            bio: 'El mas aterraddor monstruo de los creepypastas.'
        })
        .set('Authorization', `Bearer ${getToken()}`);

        expect(response.status).to.equal(200);
        expect(response.body).to.be.an('object');
        expect(response.body).to.have.property('bio');
        expect(response.body.bio).to.equal('El mas aterraddor monstruo de los creepypastas.');
    })

    it('Debería eliminar un profile', async () => {
        const response = await request(baseUrl)
        .delete('/profiles/4')
        .set('Authorization', `Bearer ${getToken()}`);

        expect(response.status).to.equal(200);
        expect(response.body).to.be.an('object');
        expect(response.body).to.have.property('active');
        expect(response.body.active).to.equal(false);
    });



})