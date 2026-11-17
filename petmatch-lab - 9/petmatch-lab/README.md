# PetMatch Mini — Laboratorio 9 (ADSO SENA)

Proyecto actualizado para persistir mascotas en MariaDB mediante Spring Data JPA, Hibernate y Jakarta Persistence.

## Requisitos

- JDK 21.
- MariaDB Server activo.
- Maven Wrapper incluido (`mvnw` y `mvnw.cmd`).
- Base de datos `petmatch_lab` y usuario `petmatch_user` con permisos sobre esa base.

## Preparar MariaDB

En una sesión administrativa, crea la base y el usuario. 
CREATE DATABASE IF NOT EXISTS petmatch_lab
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'petmatch_user'@'127.0.0.1'
  IDENTIFIED BY 'CAMBIAR_POR_UNA_CLAVE_LOCAL_SEGURA';

GRANT ALL PRIVILEGES ON petmatch_lab.*
  TO 'petmatch_user'@'127.0.0.1';
```

## Configurar variables en PowerShell

Abre PowerShell desde la carpeta raíz del proyecto:

```powershell
$env:DB_URL = 'jdbc:mariadb://127.0.0.1:3306/petmatch_lab'
$env:DB_USERNAME = 'petmatch_user'
$env:DB_PASSWORD = 'TU_CLAVE_LOCAL'
```

Ajusta el puerto en `DB_URL` si tu servidor MariaDB utiliza uno diferente. No se guarda contraseñas en el repositorio.

## Ejecutar

```powershell
.\mvnw.cmd spring-boot:run
```

Abre `http://localhost:8080/pets` en el navegador.

Para ejecutar las pruebas:

```powershell
.\mvnw.cmd test
```

La aplicación normal utiliza MariaDB. Las pruebas unitarias utilizan Mockito y las pruebas JPA/MVC utilizan H2 desde `src/test/resources/application.yaml`.

## Archivos importantes

- `SOLUCION_LABORATORIO_9.md`: respuestas, conceptos, ejercicios, retos y explicación de los cambios.
- `src/main/java/com/example/petmatch_lab/model/Pet.java`: entidad JPA.
- `src/main/java/com/example/petmatch_lab/repository/PetRepository.java`: repositorio Spring Data JPA.
- `src/main/java/com/example/petmatch_lab/service/PetService.java`: reglas y operaciones del catálogo.
- `src/main/resources/application.yaml`: conexión mediante variables de entorno.
- `src/test/resources/application.yaml`: configuración H2 exclusiva para pruebas.

