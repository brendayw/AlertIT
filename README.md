# AlertIT

AlertIT es un servicio de notificaciones meteorológicas que detecta condiciones de lluvia o viento fuertes y envía alertas automáticamente por WhatsApp. El proyecto demuestra integración con APIs externas, procesamiento en tiempo real y automatización de tareas utilizando un stack moderno y ampliamente demandado en la industria.

### ✔️ Funcionalidades clave

* Consumo de APIs externas (WeatherAPI) usando WebClient.
* Procesamiento de datos y reglas de negocio para evaluar condiciones climáticas.
* Automatización de tareas mediante Spring Scheduler.
* Envío de notificaciones por WhatsApp usando Twilio API.
* Arquitectura pensada para testing automatizado y contenedorización (Docker) — pendiente de implementación.

### 🧩 Tecnologías utilizadas
- Java 17+ 
- Spring Boot 
- WebClient 
- Spring Scheduler 
- Twilio WhatsApp API 
- Springdoc OpenAPI (Swagger)
- Docker (pendiente)
- Testing automatizado (pendiente)

### ⭐ Requisitos
- Java 17+
- Maven 3.8+
- API Key de WeatherAPI
- Cuenta en Twilio (SID, Auth Token, número sandbox de WhatsApp)
- Conexión a Internet
- (Opcional) Docker para contenerización

### 🚀 Instalación

1. Clonar el repositorio

        git clone https://github.com/tu-usuario/AlertIT.git
        cd AlertIT

2. Configurar variables de entorno

        WEATHER_API_KEY=tu_api_key_de_weatherapi
        TWILIO_ACCOUNT_SID=tu_sid
        TWILIO_AUTH_TOKEN=tu_auth_token
        TWILIO_WHATSAPP_FROM=whatsapp:+14155238886
        WHATSAPP_TO=whatsapp:+54911XXXXXXXX


   **Importante:**
    
    El número TWILIO_WHATSAPP_FROM corresponde al sandbox de Twilio. 
    El número WHATSAPP_TO debe estar registrado en tu sandbox.

3. Instalar dependencias

        mvn clean install

4. Ejecutar la aplicación

         mvn spring-boot:run

La aplicación iniciará en: http://localhost:8080

### ⚙️ Funcionalidades principales

#### Consumo de API meteorológica (WeatherAPI)

* Obtiene información actualizada del clima (viento, lluvia, tormenta) usando WebClient.
* Trabaja con datos reales en tiempo real.

#### Evaluación de condiciones climáticas

* Procesa los datos recibidos y determina si existen condiciones severas según criterios definidos para Bahía Blanca (adaptable a otras ciudades).
* Demuestra aplicación de lógica de negocio sobre datos externos.

#### Generación automática de alertas

* Cuando se cumplen las condiciones configuradas, se genera una alerta interna lista para ser enviada al usuario.

#### Envío de notificaciones por WhatsApp

* Integra Twilio para enviar mensajes automáticamente a un número configurado.

- **Ejecución periódica (Scheduler)**

* Un proceso automático se ejecuta cada 1 minuto (configurable; recomendado cada 6 horas) para:
  * Consultar el clima. 
  * Analizar los datos. 
  * Enviar notificaciones si es necesario.

No requiere interacción del usuario, pero la ubicación de la consulta se puede cambiar mediante el endpoint correspondiente.

### 📁 Estructura

Arquitectura **hexagonal (Ports & Adapters):**

    AlertIT
    │
    ├───application
    │   ├───scheduler
    │   └───services
    ├───domain
    │   ├───models
    │   ├───ports
    │   └───services
    ├───infrastructure
    │   ├───adapters
    │   ├───client
    │   │   └───dto
    │   ├───config
    │   └───weather
    └───presentation
        ├───dto
        └───rest

### 🌐 Endpoints

Todos los endpoints de AlertIT están bajo:
    
    /api/v1/weather

1. **Obtener clima actual**

* GET:

          /current?location=Bahia+Blanca
* Parámetro: location (nombre de la ciudad o ubicación)
* Respuesta: WeatherData con información actual del clima

      {
          "temperature": 21.3,
          "condition": "Soleado",
          "location": "Bahia Blanca",
          "region": "Buenos Aires",
          "humidity": 56,
          "windSpeed": 8.3,
          "extremeTemperature": false
      }

2. **Obtener coordenadas de una dirección (geocoding)**

* **GET** 

      /geocoding?location=Bahía Blanca

* Parámetro: address (dirección a geolocalizar)
* Respuesta: GeocodingData con latitud y longitud 

        {
          "latitude": -38.72,
          "longitude": -62.28,
          "formattedAddress": "Bahia Blanca, Argentina",
          "city": "Bahia Blanca",
          "region": "Buenos Aires",
          "country": "Argentina",
          "validLocation": true
        }

3. **Procesar alerta para una ubicación**

* **GET** 

       /alert?location=Bahía Blanca

* Parámetro: address (dirección a evaluar)
* Respuesta: Map<String, Object> con:

        {
            "alertasActivas": [],
            "recomendaciones": [],
            "clima": {
            "temperature": 21.3,
            "condition": "Soleado",
            "location": "Bahia Blanca",
            "region": "Buenos Aires",
            "humidity": 56,
            "windSpeed": 8.3,
            "extremeTemperature": false
        },
        "nivelAlerta": "VERDE",
        "geocoding": {
            "latitude": -38.72,
            "longitude": -62.28,
            "formattedAddress": "Bahia Blanca, Argentina",
            "city": "Bahia Blanca",
            "region": "Buenos Aires",
            "country": "Argentina",
            "validLocation": true
        },
        "fuente": "Sistema de Alertas - Criterios Bahía Blanca",
        "resumen": "Sin alertas meteorológicas activas.",
        "timestamp": "2025-11-29T16:53:33.417009700"
        }

### ⚡ Estado del proyecto

- En desarrollo.
- Próximamente: testing automatizado y contenedorización con Docker.