# PetMatch Mini — Laboratorio 10 (ADSO SENA)

Ampliación del proyecto del Laboratorio 9. Se conserva el CRUD de mascotas y se agrega el registro e historial de solicitudes de apoyo relacionadas con cada mascota.

## Requisitos

- JDK 21.
- MariaDB Server activo.
- Maven Wrapper incluido (`mvnw` y `mvnw.cmd`).
- Base de datos `petmatch_lab`.
- Variables de entorno `DB_URL`, `DB_USERNAME` y `DB_PASSWORD`.

## Preparar MariaDB

En una sesión administrativa, crea la base y el usuario. 

```sql
CREATE DATABASE IF NOT EXISTS petmatch_lab
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'petmatch_user'@'127.0.0.1'
  IDENTIFIED BY 'CAMBIAR_POR_UNA_CLAVE_LOCAL_SEGURA';

GRANT ALL PRIVILEGES ON petmatch_lab.*
  TO 'petmatch_user'@'127.0.0.1';
```


## Configurar variables en PowerShell

Abre PowerShell desde la carpeta raíz:

```powershell
$env:DB_URL = 'jdbc:mariadb://127.0.0.1:3306/petmatch_lab'
$env:DB_USERNAME = 'petmatch_user'
$env:DB_PASSWORD = 'TU_CLAVE_LOCAL'
```


## Ejecutar la aplicación

```powershell
.\mvnw.cmd spring-boot:run
```

Abre `http://localhost:8080/pets`.

Desde el detalle de una mascota utiliza **Ver solicitudes de apoyo** para registrar, consultar, completar, cancelar o reemplazar solicitudes.

## Ejecutar las pruebas

```powershell
.\mvnw.cmd test
```

La aplicación normal utiliza MariaDB. Las pruebas utilizan H2 desde `src/test/resources/application.yaml`.

## Reglas implementadas

- Cada solicitud pertenece a una mascota existente.
- Una mascota solo puede tener una solicitud `OPEN` en el flujo normal.
- Los estados disponibles son `OPEN`, `COMPLETED` y `CANCELLED`.
- Una solicitud completada o cancelada no puede volver a modificarse.
- Reemplazar una solicitud cancela la anterior y crea otra dentro de una transacción.
- Una mascota con solicitudes históricas no puede eliminarse.

## Archivos principales

- `DESARROLLO_LABORATORIO_10.md`: conceptos, respuestas a ejercicios, diagnóstico y reto integrador.
- `src/main/java/com/example/petmatch_lab/model/SupportRequest.java`: entidad y transiciones de estado.
- `src/main/java/com/example/petmatch_lab/repository/SupportRequestRepository.java`: consultas derivadas.
- `src/main/java/com/example/petmatch_lab/service/SupportRequestService.java`: reglas y operaciones transaccionales.
- `src/main/java/com/example/petmatch_lab/controller/SupportRequestController.java`: rutas y formularios.
- `src/main/java/com/example/petmatch_lab/controller/PageErrorHandler.java`: respuestas HTML 404 y 409.
- `src/main/resources/application.yaml`: conexión a MariaDB mediante variables de entorno.
- `src/test/resources/application.yaml`: configuración H2 para pruebas.






## 10. Laboratorio 11 — Seguridad y cuentas

El proyecto amplía PetMatch Mini con registro, inicio y cierre de sesión, roles `USER` y `ADMIN`, protección de rutas y CSRF. Se conservan los servicios y las reglas del Laboratorio 10.

La documentación de actividades, ejercicios, conceptos y verificaciones se encuentra en [`Definiciones_11.md`].


