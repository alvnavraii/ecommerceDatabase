const { request, expect, baseUrl, getToken, beforeAll } = require('../utils/testSetup');

describe('Test de videoTag', () => {
    before(async () => {
        await beforeAll();
    });

    it('Debería traer todos los videoTags', async () => {
       
        
        const response = await request(baseUrl)
            .get('/videoTags/all')
            .set('Authorization', `Bearer ${getToken()}`);
        
        expect(response.status).to.equal(200);
        expect(response.body).to.be.an('array');
        expect(response.body).to.not.be.empty;
        response.body.forEach(videoTag => {
            expect(videoTag).to.be.an('object');
            expect(videoTag).to.have.property('id');
            expect(videoTag).to.have.property('video');
            expect(videoTag.video).to.be.an('object');
            expect(videoTag.video).to.have.property('id');
            expect(videoTag.video).to.have.property('title');
            expect(videoTag).to.have.property('tag');
            expect(videoTag.tag).to.be.an('object');
            expect(videoTag.tag).to.have.property('id');
            expect(videoTag.tag).to.have.property('name');
        });
    })

    it('debería traer todos los videoTags activos', async () => {
        const response = await request(baseUrl)
        .get('/videoTags')
        .set('Authorization', `Bearer ${getToken()}`);

        expect(response.status).to.equal(200);
        expect(response.body).to.be.an('array');
        expect(response.body).to.not.be.empty;
        response.body.forEach(videoTag => {
            expect(videoTag).to.be.an('object');
            expect(videoTag).to.have.property('active');
            expect(videoTag.active).to.equal(true);
            expect(videoTag).to.have.property('video');
            expect(videoTag.video).to.be.an('object');
            expect(videoTag.video).to.have.property('id');
            expect(videoTag.video).to.have.property('title');
            expect(videoTag).to.have.property('tag');
            expect(videoTag.tag).to.be.an('object');
            expect(videoTag.tag).to.have.property('id');
            expect(videoTag.tag).to.have.property('name');
        })
    })

  it('No debería encontrar un videoTag por id si este está inactivo', async () => {
        const response = await request(baseUrl)
        .get('/videoTags/2') //id de un tag inactivo
        .set('Authorization', `Bearer ${getToken()}`);

        expect(response.status).to.equal(404);
    })

    it('Debería encontrar un videoTag por id si este está activo ', async () => {
        const response = await request(baseUrl)
        .get('/videoTags/1') //id de un tag activo
        .set('Authorization', `Bearer ${getToken()}`);

        expect(response.status).to.equal(200);
       
    })

    it('Debería reactivar un videoTag', async () => {
        const response = await request(baseUrl)
        .put('/videoTags/2/reactivate')
        .set('Authorization', `Bearer ${getToken()}`);

        expect(response.status).to.equal(200);
        expect(response.body).to.be.an('object');
        expect(response.body).to.have.property('active');
        expect(response.body.active).to.equal(true);
    })

    it('Debería crear un videoTag o indicar si ya existe o indicar que ya existe', async () => {
        const response = await request(baseUrl)
        .post('/videoTags')
        .send({
            videoId: 1,
            tagId: 8,
            isActive: true
        })
        .set('Authorization', `Bearer ${getToken()}`);

        expect(response.status).to.be.oneOf([201, 409]);

        if (response.status === 201) {
            expect(response.body).to.be.an('object');
            expect(response.body).to.have.property('video');
            expect(response.body).to.have.property('tag');
            expect(response.body).to.have.property('active');
            expect(response.body.active).to.equal(true);
        } else {
            expect(response.body).to.be.an('object');
            expect(response.body).to.have.property('message');
            expect(response.body.message).to.be.a('string');
        }
    })

    it('Debería actualizar un videoTag', async () => {
        const response = await request(baseUrl)
        .put('/videoTags/3')
        .send({
            tagId: 7,
        })
        .set('Authorization', `Bearer ${getToken()}`);

        expect(response.status).to.equal(200);
        expect(response.body).to.be.an('object');
        expect(response.body).to.have.property('tag');
        expect(response.body.tag).to.be.an('object');
    })

    it('Debería eliminar un un videoTag', async () => {
        const response = await request(baseUrl)
        .delete('/videoTags/2')
        .set('Authorization', `Bearer ${getToken()}`);

        expect(response.status).to.equal(200);
        expect(response.body).to.be.an('object');
        expect(response.body).to.have.property('active');
        expect(response.body.active).to.equal(false);
    })
});
