# Mantenimiento Vehiculos Pro - Frontend

![Kotlin](https://img.shields.io/badge/Kotlin-1.9.23-7F52FF.svg?style=for-the-badge&logo=kotlin)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-2024.09.00-4285F4.svg?style=for-the-badge&logo=jetpackcompose)
![Retrofit](https://img.shields.io/badge/Retrofit-2.9.0-E91E63.svg?style=for-the-badge&logo=square)

Aplicación Android nativa para la gestión y seguimiento del mantenimiento de vehículos personales. Permite a los usuarios registrar sus vehículos, añadir historiales de mantenimiento, y generar códigos QR para un acceso rápido a la información.

## ✨ Características Principales

- **Gestión de Múltiples Vehículos**: Añade y administra una flota de vehículos personales.
- **Historial de Mantenimiento**: Registra cada servicio realizado, desde cambios de aceite hasta revisiones complejas.
- **Generación de QR**: Asocia un código QR único a cada vehículo para consultar su información rápidamente.
- **Escáner QR Integrado**: Utiliza la cámara del dispositivo para escanear y acceder a los detalles de un vehículo.
- **Interfaz Moderna**: Desarrollada completamente con Jetpack Compose para una experiencia de usuario fluida y reactiva.

## 🛠️ Tecnologías y Arquitectura

Este proyecto está construido siguiendo las mejores prácticas de desarrollo Android moderno.

- **Lenguaje**: **Kotlin** como único lenguaje de programación.
- **Interfaz de Usuario**: **Jetpack Compose** para la construcción de toda la UI de forma declarativa.
- **Arquitectura**: **MVVM** (Model-View-ViewModel) para separar la lógica de negocio de la UI.
- **Navegación**: **Navigation Compose** para gestionar el flujo entre las diferentes pantallas de la aplicación.
- **Comunicación con API**: **Retrofit** y **OkHttp** para realizar las peticiones a la API REST, con un interceptor para logging.
- **Manejo de JSON**: **Gson** para la serialización y deserialización de datos entre la app y el servidor.
- **Programación Asíncrona**: **Kotlin Coroutines** para manejar operaciones en segundo plano de forma eficiente.
- **Inyección de Dependencias**: **Hilt** para gestionar y proveer las dependencias a lo largo de la app.
- **Persistencia Local**: **DataStore Preferences** para guardar datos de sesión del usuario.
- **Cámara y QR**: **CameraX** y **ZXing** para la funcionalidad de escaneo de códigos QR.

## 🚀 Puesta en Marcha

Para poder ejecutar la aplicación y conectarla con el servidor de backend local, es crucial configurar correctamente la dirección IP del servidor.

### Configuración de la Conexión al Servidor

La configuración se encuentra en el archivo: `app/src/main/java/com/mantenimientovehiculospro/data/network/RetrofitProvider.kt`

Dependiendo de dónde se ejecute la aplicación (emulador o dispositivo físico), se debe utilizar una IP diferente. **Solo una de las siguientes opciones debe estar descomentada en el código.**

---

### Opción 1: Teléfono por USB (Recomendado)

Esta es la opción más estable y funciona en cualquier red.

1.  Asegúrate de tener el teléfono conectado por USB con la depuración activada.
2.  Antes de lanzar la aplicación desde Android Studio, ejecuta el siguiente comando en una terminal. Esto redirige el tráfico del puerto `8080` del teléfono al `8080` de tu ordenador:

    ```sh
    adb reverse tcp:8080 tcp:8080
    ```

    *Si tienes un dispositivo específico y el comando anterior falla, puedes apuntar al dispositivo con su ID (que puedes obtener con `adb devices`):*

    ```sh
    adb -s TU_ID_DE_DISPOSITIVO reverse tcp:8080 tcp:8080
    ```

3.  En el archivo `RetrofitProvider.kt`, asegúrate de que la IP sea `127.0.0.1`:

    ```kotlin
    val ip = "127.0.0.1"
    ```

---

### Opción 2: Emulador de Android Studio

El emulador utiliza una dirección IP especial para comunicarse con el `localhost` del ordenador anfitrión.

1.  En el archivo `RetrofitProvider.kt`, descomenta y utiliza la siguiente IP:

    ```kotlin
    val ip = "10.0.2.2"
    ```

---

### Opción 3: Teléfono por Wi-Fi

Para esta opción, tanto tu ordenador como tu teléfono deben estar conectados a la **misma red Wi-Fi**.

1.  Busca la dirección IP de tu ordenador en la red local (ej. `192.168.1.XX`).
2.  En el archivo `RetrofitProvider.kt`, descomenta y utiliza esa IP:

    ```kotlin
    // ¡Acuérdate de cambiarla por la tuya!
    val ip = "192.168.1.10"
    ```
