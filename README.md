# DOSW-Library

Sistema de gestión de biblioteca desarrollado con **Spring Boot** como parte del curso de Desarrollo de Software (DOSW). Permite gestionar libros, usuarios y préstamos de manera organizada, con validaciones, manejo de errores y documentación de API.

## Documentación API — Swagger

La API está completamente documentada con Swagger. Con la aplicación corriendo, se puede acceder a la documentación interactiva en:
http://localhost:8081/swagger-ui/index.html

Desde esta interfaz se pueden probar todos los endpoints directamente sin necesidad de herramientas externas como Postman.

![Swagger UI](evidencias/swagger1.png)
![Swagger UI](evidencias/swagger2.png)

## Pruebas unitarias

Se implementaron **47 pruebas** distribuidas en 6 clases de prueba, cubriendo tanto escenarios exitosos como escenarios de error para los tres módulos del sistema.

### Resultados de las pruebas

![Pruebas JUnit](evidencias/test.png)

## Cobertura de pruebas — JaCoCo

El análisis de cobertura se realizó con JaCoCo

![Cobertura JaCoCo](evidencias/jacoco.png)

## Análisis estático — SonarQube

Se realizó el análisis estático del código con SonarQube, obteniendo los siguientes resultados:

![SonarQube](evidencias/Sonar1.png)
![SonarQube](evidencias/sonar2.png)
