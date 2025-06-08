# 🎓 TFG – Aplicación de Gestión de Obras

**Autores:** Manuel & Tristán  
**Tecnologías:** Java · JavaFX · CSS · Firebase Realtime Database

---

## 📱 Descripción General

Este proyecto es una **aplicación de escritorio multiplataforma** desarrollada en **Java** con interfaz gráfica **JavaFX**, conectada en tiempo real a una base de datos **Firebase**.  
Forma parte de un sistema completo que incluye también una **aplicación móvil Android**, ambos conectados a la misma base de datos para garantizar la sincronización de datos de fichajes, empleados, obras y horarios.

---

## 🧩 Funcionalidades

###Aplicación de Escritorio (JavaFX)

-  **Gestión de empleados**:
  - Alta y visualización de empleados.
  - Asignación de empleados a obras.
  - Consulta de la obra asignada a cada uno.
-  **Gestión de obras**:
  - Alta y visualización de obras.
  - Visualización de empleados asignados.
-  **Gestión de horarios**:
  - Consulta y edición de los horarios semanales de los empleados.
- **Informes** (en desarrollo):
  - Consulta de puntualidad y fichajes.
-  **Perfil del administrador**:
  - Cambio de contraseña.
  - Cambio del nombre de usuario (próximamente).
- **Pantalla de bienvenida** al iniciar sesión.

### Aplicación Móvil (Android)

- Fichajes de entrada y salida por parte del trabajador.
- Consulta de horarios asignados.
- Visualización de obra asignada.
- Sincronización total con escritorio vía Firebase.

---

## Firebase: Conexión en tiempo real

Ambas aplicaciones (escritorio y móvil) comparten la misma base de datos en Firebase:

- Todos los cambios se reflejan al instante.
- Fichajes realizados desde el móvil pueden consultarse desde escritorio.
- Obras asignadas desde escritorio son visibles en el móvil.

---

## Requisitos

- JDK **17 o superior**
- JavaFX SDK (versión incluida)
- IDE como **IntelliJ IDEA**, **Eclipse** o **NetBeans**
- Acceso a internet para conectarse a Firebase

---

## 📁 Librerías necesarias

Descarga todas las librerías necesarias desde este enlace:

 [Google Drive – Librerías JavaFX](https://drive.google.com/drive/folders/1e--PHlhqufd6_XC6x3cR8U19LHLzOfBA?usp=drive_link)

Incluye:
- JavaFX SDK completo
- Librerías adicionales si se usaron (p. ej., envío de emails)

---

## Cómo ejecutar el proyecto

### 1. Añadir JavaFX al proyecto

1. Descarga el contenido del enlace de arriba.
2. Abre tu proyecto en tu IDE.
3. Ve a **Project Structure** o **Build Path**.
4. Añade todos los `.jar` dentro de `/lib` de JavaFX como librerías externas.

### 2. Configurar los argumentos de la VM

Agrega lo siguiente en la configuración de ejecución (Run Configuration):

```bash
--module-path /ruta/a/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml




📁 src/
 ├── controller/       ← Controladores JavaFX
 ├── model/            ← Clases Empleado, Obra, Fichaje, etc.
 ├── util/             ← FirebaseService, Sesión, helpers
 ├── view/             ← Archivos .fxml de la interfaz
 └── Main.java         ← Punto de entrada de la app
 
 
 
 
 
 
 
 
+------------------+          escritura / lectura           +--------------------+
| App de Escritorio|  <---------------------------------->  |    Firebase DB     |
|    (JavaFX)      |                                        | (Realtime Database)|
+------------------+                                        +--------------------+
        ▲                                                         ▲
        |                                                         |
        |               sincronización automática                 |
        ▼                                                         ▼
+------------------+                                     +--------------------+
| App de Android   |                                     |  Aplicación móvil  |
|  (empleado)      |                                     |  para fichajes     |
+------------------+                                     +--------------------+
 
 
 
 
 Para más información:

 Manuel – [manudarzee@gmail.com]

Tristán – [tcoutial@gmail.com]

 
 
 
 Este proyecto es parte del Trabajo Final de Ciclo (TFG) del CFGS de Desarrollo de Aplicaciones Multiplataforma.
Puede ser utilizado únicamente con fines educativos.



 
 

