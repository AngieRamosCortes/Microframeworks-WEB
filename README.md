# Servidor Web 

Un framework web ligero desarrollado en Java que permite crear aplicaciones web con servicios REST y manejo de archivos estáticos de manera sencilla y eficiente.

---

## Empezando

Estas instrucciones te permitirán obtener una copia del proyecto y ejecutarlo en tu máquina local para desarrollo y pruebas.

### Prerrequisitos

Necesitas tener instalados:

- [Java JDK 17+](https://www.oracle.com/java/technologies/downloads/)
- [Apache Maven 3.8+](https://maven.apache.org/)

Verifica las versiones ejecutando:

```bash
java -version
mvn -version
```

### Instalación

Clona el repositorio y compílalo con Maven:

```bash
git clone https://github.com/AngieRamosCortes/Microframeworks-WEB.git
cd Microframeworks-WEB
mvn clean install
```

Ejecuta el servidor con Maven:

```bash
mvn clean compile
mvn clean package
java -jar target/WebServer-1.0.0.jar
```

### Acceso en el navegador

Abre en tu navegador:

```
http://localhost:8080
```

El servidor estará corriendo y podrá ver la interfaz de demostración del framework.

### Endpoints disponibles:

- `GET /hello?name=Pedro` - Servicio de saludo personalizado
- `GET /pi` - Constante matemática PI en formato JSON
- `GET /random` - Generador de números aleatorios
- `GET /calc?a=10&b=5&op=add` - Calculadora con operaciones básicas
- `GET /health` - Estado del servidor
- `GET /about` - Información del framework

## Ejecución de las pruebas

Para ejecutar las pruebas automatizadas del sistema:

```bash
mvn test
```

### Pruebas de extremo a extremo

Las pruebas evalúan la funcionalidad completa del framework, incluyendo:

- Manejo correcto de rutas GET
- Procesamiento de parámetros de consulta
- Servicio de archivos estáticos
- Respuestas JSON válidas

### Pruebas de estilo de codificación

Se verifica que las clases Java compilen correctamente (Java 17) y se mantengan convenciones básicas (paquetes, nombres de clase, etc.).

---

## Arquitectura

El framework implementa varios patrones de diseño:

- **Facade Pattern**: WebFramework como interfaz principal
- **Strategy Pattern**: Router para manejo de rutas
- **Builder Pattern**: Response para construcción de respuestas
- **Observer Pattern**: Gestión de rutas y eventos

### Componentes principales:

- `WebFramework`: Clase principal
- `Router`: Gestión de rutas
- `HttpServer`: Servidor HTTP con pool de threads
- `StaticFileHandler`: Manejo de archivos estáticos
- `Request/Response`: Objetos de solicitud y respuesta

## Construido con

* **Java 11** - Lenguaje de programación
* **Maven** - Gestión de dependencias y construcción
* **Socket API** - Comunicación de red de bajo nivel
* **Lambda Expressions** - Programación funcional para handlers

## Características principales

- Definición de rutas con expresiones lambda
- Extracción sencilla de parámetros de consulta con `req.getValues()`
- Configuración flexible de directorios de archivos estáticos
- Soporte para múltiples tipos de contenido (JSON, HTML, texto)
- Manejo automático de errores HTTP
- Pool de threads para concurrencia
- Arquitectura modular y extensible

## Control de versiones

Este proyecto utiliza Git para el control de versiones. Para ver las versiones disponibles, consulte las etiquetas en este repositorio.

## Autora

* **Angie Julieth Ramos Cortes** - *Desarrollo completo*
---

## License

Este proyecto está bajo la licencia **MIT** – consulta el archivo `LICENSE.md` para más detalles.

---

## Expresiones de gratitud

- Inspiración en conceptos básicos de servidores web en Java
- Agradecimientos a la **Escuela Colombiana de Ingeniería Julio Garavito** por el contexto académico

