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

Para controlar quien accede a los endpoint de Actuator, podríamos proteger los endpoints.

## Ejecución del proyecto

- Clonar/descargar el proyecto
- Ejecutar el proyecto con el comando: `./mvnw spring-boot:run`
    - O ejecutar directamente desde IntelliJ Idea
- Endpoints de Actuator:
  - http://localhost:8080/actuator/health
  - http://localhost:8080/actuator/env
  - http://localhost:8080/actuator/metrics
    - - http://localhost:8080/actuator/metrics/process.cpu.usage  (esta métrica, por ejemplo)
  - http://localhost:8080/actuator/beans
  - http://localhost:8080/actuator/loggers
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