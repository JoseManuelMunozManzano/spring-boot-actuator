# SPRING BOOT 3 COOKBOOK

## Añadir Actuator a mi aplicación

Uso Spring Initializr: `https://start.spring.io/`

![01-Spring-Initializr.png](images/01-Spring-Initializr.png)

En `applications.yml` he añadido los siguientes endpoints: `health,env,metrics,beans,loggers`.

- /actuator/health: provee información de salud de la aplicación. Muy útil en entornos contenerizados como Kubernetes para asegurar que la aplicación está arriba y ejecutándose
- /actuator/env: devuelve las variables de entorno de la aplicación
- /actuator/metrics: devuelve una lista con las métricas que han sido expuestas por la aplicación. Se puede obtener el valor de una de las métricas añadiendo el nombre de la métrica al endpoint
  - /actuator/metrics/process.cpu.usage
- /actuator/beans: devuelve una lista con los beans registrados en el contenedor IoC, es decir, los beans que pueden inyectarse en otros beans
- /actuator/loggers: devuelve una lista de niveles de log y loggers de la aplicación. Permite modificar el nivel de log en tiempo de ejecución
- /actuator/info: devuelve información de la app, como la versión, nombre de la app...

Para controlar quien accede a los endpoint de Actuator, podríamos proteger los endpoints.

## Ejecución del proyecto

- Clonar/descargar el proyecto
- Sustituir el nombre `application_template.yml` por `application.yml` e indicar los valores correctos en usuario y password de BBDD
- Ejecutar el proyecto con el comando: `./mvnw spring-boot:run`
    - O ejecutar directamente desde IntelliJ Idea
- Endpoints de Actuator:
  - http://localhost:8080/actuator/health
  - http://localhost:8080/actuator/env
  - http://localhost:8080/actuator/metrics
    - - http://localhost:8080/actuator/metrics/process.cpu.usage  (esta métrica, por ejemplo)
  - http://localhost:8080/actuator/beans
  - http://localhost:8080/actuator/loggers
  - http://localhost:8080/actuator/info
  - http://localhost:8080/actuator/football y veremos la versión actual `teams/1.0.1.json`
  - Cambiar el nombre del fichero a `teams/1.0.2.json`, añadiendo al array un nuevo equipo de fútbol y refrescar
    - `curl --request POST http://localhost:8080/actuator/football`
    - Ejecutar de nuevo `http://localhost:8080/actuator/football` y veremos la nueva versión `teams/1.0.2.json`
- Endpoints
  - http://localhost:8080/football para mostrar los valores cargados en memoria, provenientes del fichero de `teams/1.0.1.json`

## Añadir endpoint de Actuator personalizado a mi aplicación

Voy a cargar un fichero de una carpeta y devolver ciertos valores. Una vez hecho esto, necesitaré crear un endpoint de Actuator personalizado que devuelva el fichero cargado. También voy a configurar el endpoint para recargar el fichero.

Creamos:

- `loader/FileLoader` para cargar un fichero y mantener el contenido el memoria
- `actuator/FootballCustomEndpoint` define el endpoint de Actuator `football`
- `config/FootballConfiguration` es una clase de configuración donde creamos beans para FileLoader y FootballCustomEndpoint
- `loader/DataInitializer` implementa una interface ApplicationRunner para cargar el fichero de `/teams/1.0.1.json`
- `teams/1.0.1.json` fichero con un array de valores
- `controllers/FootballController` devuelve el contenido cargado en memoria por la clase FileLoader.

Modificamos `application.yml` para indicar la carpeta que contiene el fichero a cargar y añadimos el nuevo endpoint de Actuator.

### Testing

Ver arriba Ejecución del proyecto.

## Usando sondas y crear un control health personalizado

Vamos a configurar la aplicación para gestionar sondas (probes) y crear un control health personalizado para verificar que la aplicación puede conectarse a una BBDD.

Modificamos:

- `pom.xml` para añadir las dependencias para poder usar la BBDD `PostgreSQL`
- `application.yml` para habilitar los probes readiness y liveness y configurar la BBDD
- `loader/FileLoader` modificado para simular que la carga del fichero nos lleva 10 segundos

Creamos:

- `services/TradingService` emula el servicio de football trading
- `actuator/FootballHealthIndicator` donde hacemos override el método `health()` para controlar la conectividad de la BBDD

### Testing

#### Test 1
Ejecutar la app, abrir el navegador y ejecutar el siguiente endpoint

- http://localhost:8080/actuator/health/readiness

Durante los primeros 10sg, que es lo que tarda en cargarse el fichero, veremos:

```json
{
 "status": "OUT_OF_SERVICE"
}
```

Pasados 10sg, el fichero estará cargado y veremos:

```json
{
 "status": "UP"
}
```

#### Test 2

Ejecutar la app 

- http://localhost:8080/actuator/health/liveness

Siempre veremos el resultado:

```json
{
 "status": "UP"
}
```

#### Test 3

- Usar el endpoint de trading en Postman
  - Hay que iniciar la app cada vez antes de hacer el POST
  - En la carpeta Postman se encuentra el fichero con el endpoint a probar, `trading`
  - Recordar que si hay más de 90 peticiones pendientes, se marcará como fallo, es decir, el endpoint liveness devolverá `DOWN` de vez en cuando
    - http://localhost:8080/actuator/health/liveness

#### Test 4

- Testear el endpoint de chequeo de health en Postman
  - En Docker poner en pausa `Postgres`
  - Ejecutar en Postman el endpoint `test health endpoint`
  - Tras un rato aparecerá el resultado siguiente:
   
```json  
{
  "status": "DOWN",
  "groups": [
    "liveness",
    "readiness"
  ]
}
```
  - Si en Docker pulsamos resume y ejecutamos el endpoint de nuevo veremos

```json  
{
  "status": "UP",
  "groups": [
    "liveness",
    "readiness"
  ]
}
```