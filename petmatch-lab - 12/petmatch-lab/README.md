# PetMatch Mini — Laboratorio 12

Aplicación web Spring Boot que conserva la interfaz HTML con Thymeleaf e incorpora una API REST JSON protegida con HTTP Basic.

## Requisitos

- Java 21.
- MariaDB en ejecución para el uso normal de la aplicación.
- Maven Wrapper incluido en el proyecto.

## Configuración de MariaDB (Windows PowerShell)

Ajusta los valores de conexión de acuerdo con tu instalación:

```powershell
$env:DB_URL="jdbc:mariadb://localhost:3306/petmatch_lab"
$env:DB_USERNAME="tu_usuario"
$env:DB_PASSWORD="tu_contraseña"
```

## Ejecutar pruebas

```powershell
.\mvnw.cmd test
```

Las pruebas utilizan H2 en memoria mediante `src/test/resources/application.yaml`. No necesitan conectarse a la base MariaDB de desarrollo.

## Iniciar la aplicación

```powershell
.\mvnw.cmd spring-boot:run
```

Interfaz web: `http://localhost:8080/pets`

API de mascotas: `http://localhost:8080/api/v1/pets`

Las rutas de la API requieren HTTP Basic. Las consultas y creación de solicitudes están permitidas a USER y ADMIN; las operaciones administrativas requieren ADMIN.

## Endpoints REST

| Método | Ruta | Permiso |
|---|---|---|
| GET | `/api/v1/pets` | USER / ADMIN |
| GET | `/api/v1/pets/{petId}` | USER / ADMIN |
| POST | `/api/v1/pets` | ADMIN |
| PUT | `/api/v1/pets/{petId}` | ADMIN |
| DELETE | `/api/v1/pets/{petId}` | ADMIN |
| GET | `/api/v1/pets/{petId}/requests` | USER / ADMIN |
| GET | `/api/v1/pets/{petId}/requests/{requestId}` | USER / ADMIN |
| POST | `/api/v1/pets/{petId}/requests` | USER / ADMIN |
| POST | `/api/v1/pets/{petId}/requests/{requestId}/complete` | ADMIN |
| POST | `/api/v1/pets/{petId}/requests/{requestId}/cancel` | ADMIN |
| PUT | `/api/v1/pets/{petId}/requests/{requestId}/replace` | ADMIN |

## Documentación del laboratorio

Consulta `Definicion-12.md` para las definiciones, actividades resueltas, tabla de rutas, pruebas manuales con curl, reto integrador, SQL de comprobación y respuestas del diagnóstico final.


