# AlertIT — Automated Weather Alert System

AlertIT es un servicio backend que monitorea condiciones climáticas en tiempo real y envía **alertas automáticas por WhatsApp** cuando se detectan eventos meteorológicos severos, como lluvias intensas o vientos fuertes.

El proyecto fue diseñado para simular un **caso real de backend productivo**, priorizando integración con servicios externos, automatización de procesos y una arquitectura mantenible.

---

## 🧠 ¿Qué problema resuelve?

Muchas alertas climáticas requieren:
- Consultar datos externos en tiempo real
- Procesar reglas de negocio propias
- Ejecutarse de forma automática, sin intervención del usuario
- Notificar por canales confiables

AlertIT automatiza todo ese flujo:
- Consulta datos meteorológicos reales
- Evalúa condiciones severas según criterios configurables
- Genera alertas internas
- Envía notificaciones automáticas por WhatsApp

---

## 🏗️ Decisiones técnicas destacadas

- **Arquitectura hexagonal (DDD)**  
  Separación clara entre dominio, aplicación e infraestructura para facilitar testing y evolución del sistema.

- **Integración con APIs externas**  
  Uso de `WebClient` para consumir WeatherAPI y Twilio de forma no bloqueante.

- **Automatización con Spring Scheduler**  
  El sistema funciona de manera autónoma mediante tareas programadas (polling configurable).

- **Backend orientado a eventos**  
  No requiere interacción directa del usuario para cumplir su función principal.

---

## ⭐ ¿Por qué este proyecto es relevante?

AlertIT no es una API CRUD tradicional. Está pensado como un backend autónomo que:
- Consume y coordina múltiples servicios externos
- Ejecuta lógica de negocio sin intervención del usuario
- Funciona mediante tareas programadas y eventos
- Refleja escenarios reales de sistemas productivos

---

## ⚙️ Stack tecnológico

- Java 17+
- Spring Boot
- Spring WebClient
- Spring Scheduler
- Twilio WhatsApp API
- WeatherAPI
- Springdoc OpenAPI (Swagger)
- Maven

📌 **Próximamente:**
- Testing automatizado (JUnit + Mockito)
- Contenerización con Docker

---

## 🚀 Cómo ejecutar el proyecto

#### ⭐ Requisitos

- Java 17+
- Maven 3.8+
- API Key de WeatherAPI
- Cuenta en Twilio (SID, Auth Token, número sandbox de WhatsApp)
- Conexión a Internet
- (Opcional) Docker para contenerización

---

### 🚀 Instalación

Clonar el repositorio:

    ```bash
    git clone https://github.com/tu-usuario/AlertIT.git
    cd AlertIT

Configurar variables de entorno:

    WEATHER_API_KEY=tu_api_key_de_weatherapi
    TWILIO_ACCOUNT_SID=tu_sid
    TWILIO_AUTH_TOKEN=tu_auth_token
    TWILIO_WHATSAPP_FROM=whatsapp:+14155238886
    WHATSAPP_TO=whatsapp:+54911XXXXXXXX

⚠️ El número TWILIO_WHATSAPP_FROM corresponde al sandbox de Twilio.
El número WHATSAPP_TO debe estar previamente registrado en el sandbox.

Instalar dependencias:

    mvn clean install

Ejecutar la aplicación:

    mvn spring-boot:run

La aplicación iniciará en: http://localhost:8080

---

## ⚙️ Funcionalidades principales

### 🌦️ Consumo de API meteorológica (WeatherAPI)
- Obtiene información actualizada del clima (viento, lluvia, tormenta) usando WebClient
- Trabaja con datos reales en tiempo real

### 🧠 Evaluación de condiciones climáticas
- Procesa los datos recibidos y determina si existen condiciones severas
- Aplica reglas de negocio configuradas para Bahía Blanca (adaptable a otras ciudades)

### 🔔 Generación automática de alertas
- Genera alertas internas cuando se cumplen las condiciones configuradas

### 💬 Envío de notificaciones por WhatsApp
- Integra Twilio para enviar mensajes automáticamente a un número configurado

### ⏱️ Ejecución periódica (Scheduler)
-Proceso automático configurable (actualmente cada 1 minuto; recomendado cada 6 horas)

**Flujo completo:**
- Consulta el clima
- Analiza los datos
- Envía notificaciones si es necesario

---

## 📁 Estructura del proyecto

Arquitectura hexagonal (DDD):

    AlertIT
    │
    ├── application
    │   ├── scheduler
    │   └── services
    ├── domain
    │   ├── enums
    │   ├── models
    │   ├── ports
    │   │   └── outbound
    │   └── services
    ├── infrastructure
    │   ├── adapters
    │   │   └── outbound
    │   │       ├── notification
    │   │       └── weather
    │   │           └── client
    │   │               └── dto
    │   ├── config
    └── presentation
        ├── dto
        └── rest

---


## 🌐 Endpoints


Base path: 

    /api/v1/weather

#### Obtener clima actual

    GET /current?location=Bahia+Blanca

Respuesta:


    {
        "temperature": 21.3,
        "condition": "Soleado",
        "location": "Bahia Blanca",
        "region": "Buenos Aires",
        "humidity": 56,
        "windSpeed": 8.3,
        "extremeTemperature": false
    }

#### Obtener coordenadas (Geocoding)

    GET /geocoding?location=Bahía Blanca

Respuesta:

    {
        "latitude": -38.72,
        "longitude": -62.28,
        "formattedAddress": "Bahia Blanca, Argentina",
        "city": "Bahia Blanca",
        "region": "Buenos Aires",
        "country": "Argentina",
        "validLocation": true
    }

#### Procesar alerta para una ubicación

    GET /alert?location=Bahía Blanca

Respuesta:

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

---

### 🧪 Testing (en progreso)
La arquitectura fue diseñada desde el inicio para permitir:
* Tests unitarios del dominio
* Mock de APIs externas
* Pruebas aisladas de reglas de negocio

***📌 Implementación planificada con JUnit y Mockito.***

---

### ⚡ Estado del proyecto

#### 🚧 En desarrollo activo

Próximos pasos:
- Tests automatizados
- Dockerización
- Configuración por perfiles (dev / prod)
