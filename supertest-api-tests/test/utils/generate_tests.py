import os

script = '''
const { request, expect, baseUrl, getToken, beforeAll } = require('../utils/testSetup');

describe('Test de #testName', () => {
    before(async () => {
        await beforeAll();
    });

    it('Debería traer #all', async () => {
       
        
        const response = await request(baseUrl)
            .get('/#testName/all')
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

    it('debería traer #active', async () => {
        const response = await request(baseUrl)
        .get('/#testName')
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

    it('No debería encontrar #idInactive', async () => {
        const response = await request(baseUrl)
        .get('/#testName/4') //id de un tag inactivo
        .set('Authorization', `Bearer ${getToken()}`);

        expect(response.status).to.equal(404);
    })

    it('Debería encontrar #idActive', async () => {
        const response = await request(baseUrl)
        .get('/#testName/1') //id de un tag activo
        .set('Authorization', `Bearer ${getToken()}`);

        expect(response.status).to.equal(200);
        expect(response.body).to.be.an('object');
        expect(response.body).to.have.property('id');
        expect(response.body.id).to.be.a('number');
    })

    it('Debería reactivar #reactivate', async () => {
        const response = await request(baseUrl)
        .put('/#testName/2/reactivate')
        .set('Authorization', `Bearer ${getToken()}`);

        expect(response.status).to.equal(200);
        expect(response.body).to.be.an('object');
        expect(response.body).to.have.property('active');
        expect(response.body.active).to.equal(true);
    })

    it('Debería crear #create o indicar que ya existe', async () => {
        const response = await request(baseUrl)
        .post('/#testName')
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

    it('Debería actualizar #update', async () => {
        const response = await request(baseUrl)
        .put('/#testName/8')
        .send({
            name: 'Cromoterapia China'})
        .set('Authorization', `Bearer ${getToken()}`);

        expect(response.status).to.equal(200);
        expect(response.body).to.be.an('object');
        expect(response.body).to.have.property('name');
        expect(response.body.name).to.equal('Cromoterapia China');
    })

    it('Debería eliminar un #delete', async () => {
        const response = await request(baseUrl)
        .delete('/#testName/8')
        .set('Authorization', `Bearer ${getToken()}`);

        expect(response.status).to.equal(200);
        expect(response.body).to.be.an('object');
        expect(response.body).to.have.property('active');
        expect(response.body.active).to.equal(false);
    })
});
'''

def generate_tests(test_name, all, active, idInactive, idActive, reactivate, create, update, delete):
    file_name = os.path.abspath(os.path.join(os.getcwd(), 'test/api/', f'{test_name}.test.js'))
    with open(file_name, 'w') as file:
        file.write(script.replace('#testName', test_name).replace('#all', all).replace('#active', active).replace('#idInactive', idInactive).replace('#idActive', idActive).replace('#reactivate', reactivate).replace('#create', create).replace('#update', update).replace('#delete', delete))
        file.close()
    print(f'Archivo creado: {file_name}')

if __name__ == '__main__':
    generate_tests('categorias',
                   'todos las categorías',
                   'todos los categorías activas',
                   'una categoría por id si este está inactiva',
                   'una categoría por id si este está activa ',
                   'una categoría',
                   'una categoría o indicar si ya existe',
                   'una categoría',
                   'una categoría')