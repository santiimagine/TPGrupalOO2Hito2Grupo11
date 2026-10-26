# Epicentro Gourmet - TP Grupal Hito 2 (Spring Boot)

Orientación a Objetos II - UNLa - 2026

## Grupo 11

| Integrante | Usuario de GitHub |
|---|---|
| Santiago Saitz | [@santiimagine](https://github.com/santiimagine) |
| Francisco Robles Acuña | [@Francisco-Robles](https://github.com/Francisco-Robles) |
| Nicolás Campusano | [@NicoPro03](https://github.com/NicoPro03) |
| Emanuel Sotelo | [@EmaNicoSotelo](https://github.com/EmaNicoSotelo) |

## Diagrama de clases

(completar: enlace al diagrama de clases)

## Cómo correr la aplicación

Requisitos: Java 17, MySQL 8 y un IDE (IntelliJ IDEA). El proyecto usa Maven.

1. **Crear la base de datos.** En MySQL ejecutar:

   ```sql
   CREATE DATABASE IF NOT EXISTS epicentro_gourmet CHARACTER SET utf8mb4;
   ```

2. **Configurar las variables de entorno** en la configuración de ejecución del IDE
   (Run > Edit Configurations > Environment variables), separadas por punto y coma:

   ```
   DB_URL=jdbc:mysql://localhost:3306/epicentro_gourmet;DB_USERNAME=su_usuario;DB_PASSWORD=su_contraseña;MAIL_USERNAME=usuario_mail;MAIL_PASSWORD=contraseña_mail
   ```

3. **Levantar la aplicación** ejecutando `EpicentroGourmetApplication`
   (http://localhost:8080). La primera vez, Hibernate crea las tablas automáticamente.

4. **Cargar los datos iniciales.** Con la aplicación ya levantada una vez, ejecutar en MySQL
   el script [`sql/epicentro_gourmet_hito2.sql`](sql/epicentro_gourmet_hito2.sql)
   **una sola vez** (desde la línea `USE epicentro_gourmet;`). Si se ejecuta dos veces, los
   festivales quedan duplicados y hay que rehacer la base.

5. **Probar el login** con el administrador precargado:
   - Mail: `admin@epicentrogourmet.com`
   - Contraseña: `admin1234`

Notas:
- Si alguien cambia una entidad, debe avisar al grupo: `ddl-auto: update` no borra columnas
  viejas, así que el resto tiene que borrar y recrear su esquema.
- Cada integrante usa su propia base local. Las contraseñas y variables de entorno no se suben al repositorio.

## Estructura del proyecto

```
src/main/java/com/unla/epicentrogourmet
  configuration/    Configuración de Spring: seguridad (login y rutas por rol) y ModelMapper
  controllers/      Reciben las URLs, piden datos a los services y devuelven la vista
  dtos/             DTOs que viajan a las vistas
  entities/         Entidades JPA
  helpers/          ViewRouteHelper (rutas y vistas), Constantes (valores de negocio)
  repositories/     Acceso a la base de datos (IxxxRepository, Spring Data JPA)
  services/         Lógica de negocio (IxxxService); las clases están en implementation/
src/main/resources/templates   Vistas Thymeleaf
sql/                           Scripts de base de datos
```

## Registro de actualizaciones

Cada actualización del repositorio debe registrarse en la siguiente tabla, indicando el caso de uso realizado y el responsable.

| Fecha | Caso de uso                                                                                                 | PR                                                                        | Responsable |
|---|-------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------|---|
| 13/10/2026 | Base del proyecto (entidades, seguridad, página de inicio y script de datos)                                | [#1](https://github.com/santiimagine/TPGrupalOO2Hito2Grupo11/pull/1)      | Nicolás Campusano |
| 16/10/2026 | Empleado: Ver información & actualidad                                                                      | [#2 y #3](https://github.com/santiimagine/TPGrupalOO2Hito2Grupo11/pull/2) | Santiago Saitz |
| 16/10/2026 | Registro de unidad de venta con responsable                                                                 | [#4](https://github.com/santiimagine/TPGrupalOO2Hito2Grupo11/pull/4)      | Nicolás Campusano |
| 17/10/2026 | Administrador 1 - 2: Ver festivales y unidades de venta de un festival                                      | [#5](https://github.com/santiimagine/TPGrupalOO2Hito2Grupo11/pull/5)      | Nicolás Campusano |
| 19/10/2026 | Administrador 3: Ranking UnidadesVenta - Responsable 5: Reporte Recaudacion                                 | [#6](https://github.com/santiimagine/TPGrupalOO2Hito2Grupo11/pull/6)      | Nicolás Campusano |
| 20/10/2026 | Administrador 4: Unidades con Mayor Canon - Responsable 3: Cálculo de Canon                                 | [#7](https://github.com/santiimagine/TPGrupalOO2Hito2Grupo11/pull/7)      | Nicolás Campusano |
| 20/10/2026 | Empleado: Liquidación de Haberes y Descargar Recibo de Sueldo                                               | [#9](https://github.com/santiimagine/TPGrupalOO2Hito2Grupo11/pull/9)      | Santiago Saitz |
| 20/10/2026 | Responsable (Flujo 4): Alta de platos                                                                       | [#10](https://github.com/santiimagine/TPGrupalOO2Hito2Grupo11/pull/10)    | Emanuel Sotelo |
| 20/10/2026 | Responsable 2 (filtros): Listado y filtrado de platos                                                       | [#11](https://github.com/santiimagine/TPGrupalOO2Hito2Grupo11/pull/11)    | Emanuel Sotelo |
| 20/10/2026 | Responsable 8: Plato Estrella                                                                               | [#12](https://github.com/santiimagine/TPGrupalOO2Hito2Grupo11/pull/12)    | Emanuel Sotelo |
| 20/10/2026 | Responsable 6: Cálculo de Rentabilidad Neta                                                                 | [#13](https://github.com/santiimagine/TPGrupalOO2Hito2Grupo11/pull/13)    | Emanuel Sotelo |
| 20/10/2026 | Responsable 7: Cálculo de Rentabilidad Neta entre dos fechas                                                | [#14](https://github.com/santiimagine/TPGrupalOO2Hito2Grupo11/pull/14)    | Emanuel Sotelo |
| 20/10/2026 | Responsable (CU individual): Rentabilidad por plato (ranking de platos por ganancia)                        | [#15](https://github.com/santiimagine/TPGrupalOO2Hito2Grupo11/pull/15)    | Emanuel Sotelo |
| 21/10/2026 | Responsable (CU individual): Modificar plato y baja lógica con reactivación                                 | [#16](https://github.com/santiimagine/TPGrupalOO2Hito2Grupo11/pull/16)    | Emanuel Sotelo |
| 21/10/2026 | Responsable (CU individual): Ventas por día                                                                 | [#17](https://github.com/santiimagine/TPGrupalOO2Hito2Grupo11/pull/17)    | Emanuel Sotelo |
| 21/10/2026 | Empleado: Cerrar Caja                                                                                       | [#18](https://github.com/santiimagine/TPGrupalOO2Hito2Grupo11/pull/18)    | Santiago Saitz |
| 22/10/2026 | Administrador (CU individual): Resumen economico de un festival                                             | [#20](https://github.com/santiimagine/TPGrupalOO2Hito2Grupo11/pull/20)    | Nicolás Campusano |
| 22/10/2026 | Empleado (CU individual): Recibo de sueldo en PDF (con vista previa)                                        | [#22](https://github.com/santiimagine/TPGrupalOO2Hito2Grupo11/pull/22)    | Santiago Saitz |
| 23/10/2026 | Administrador (CU individual): Personal de una unidad de venta                                              | [#23](https://github.com/santiimagine/TPGrupalOO2Hito2Grupo11/pull/23)    | Nicolás Campusano |
| 24/10/2026 | Empleado (CU individual): Cerrar Caja (con vista previa)                                                    | [#24](https://github.com/santiimagine/TPGrupalOO2Hito2Grupo11/pull/24)    | Santiago Saitz |
| 25/10/2026 | Responsable 1 (ABM): Listado, alta con envío de mail, modificación, baja lógica y reactivación de empleados | [#25](https://github.com/santiimagine/TPGrupalOO2Hito2Grupo11/pull/25)    | Francisco Robles Acuña |
| 26/10/2026 | Responsable 2 (filtros): Listado y filtrado de empleados por texto, fecha, tipo o unidad                    | [#26](https://github.com/santiimagine/TPGrupalOO2Hito2Grupo11/pull/26)    | Francisco Robles Acuña |
