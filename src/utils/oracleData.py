import oracledb
import inflect

# Configuración de la conexión
username = "ecommerce"
password = "PwdLota5971!"
dsn = "localhost:1521/XEPDB1"  # Formato: host:port/service_name

def return_structure(table_name):

    dict_columns = []
    p = inflect.engine()
    pluaral = p.plural(table_name)
    if pluaral == table_name:
        pluaral = table_name+"s"

    try:
        # Establecer la conexión
        connection = oracledb.connect(user=username, password=password, dsn=dsn)

        print("Conexión exitosa a la base de datos!")

        # Crear un cursor
        cursor = connection.cursor()

        # Ejemplo: Ejecutar una consulta simple
        cursor.execute(f"SELECT COLUMN_NAME, DATA_TYPE, DATA_LENGTH, NULLABLE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = '{pluaral.upper()}' ")  # 'dual' es una tabla especial en Oracle
        for row in cursor:
            if row[0] not in ["CREATED_AT", "UPDATED_AT", "CREATED_BY", "UPDATED_BY"]:
                dict_columns.append({"column_name": row[0], "data_type": row[1], "data_length": row[2], "nullable": row[3]})

        

    except oracledb.DatabaseError as e:
        print("Error al conectar a la base de datos:", e)
    finally:
        # Cerrar el cursor y la conexión
        if 'cursor' in locals():
            cursor.close()
        if 'connection' in locals():
            connection.close()

    return dict_columns

if __name__ == "__main__":
    return_structure("measurement_unit_translation")