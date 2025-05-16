# 🛒 E-Commerce Platform

Plataforma de comercio electrónico basada en microservicios con arquitectura moderna, tecnologías robustas y funcionalidades avanzadas tanto para usuarios como para administradores y vendedores.

---

## 🚀 Tecnologías Utilizadas

### 🖥️ Frontend
- React
- TypeScript

### 🔧 Backend
- Spring Boot
- Kafka (event streaming)
- WebSockets (notificaciones en tiempo real)

### 🗃️ Base de Datos
- PostgreSQL

---

## 📋 Requisitos Funcionales

### ✅ Requisito 1: Estructura y Gestión
**Descripción:**  
Definir la estructura de la base de datos y microservicios. Organizar y gestionar tareas del equipo.  
**Criterios de aceptación:**  
- PDF con estructura completa (microservicios, topics, etc.)
- Registro de tareas y tiempos dedicados

### 🛍️ Requisito 2: Vista de Productos
**Descripción:**  
Visualización e interacción con todos los productos disponibles.  
**Criterios de aceptación:**
- Mostrar: nombre, precio, descripción, valoraciones, imagen
- Filtros desde el backend: rango de precios, categoría, calificación, stock
- Agregar productos al carrito o lista de deseos
- Paginación o lazy loading para grandes catálogos

### 🔍 Requisito 3: Detalle del Producto
**Descripción:**  
Vista detallada de un producto seleccionado.  
**Criterios de aceptación:**
- Mostrar información completa (precio, variantes, uso, garantía)
- Valoraciones ordenables
- Botones para añadir al carrito/deseos y compartir en redes

### ⭐ Requisito 4: Valoraciones
**Descripción:**  
Sistema de valoración de productos por usuarios.  
**Criterios de aceptación:**
- Una valoración por usuario y producto
- De 0 a 5 estrellas, con texto e imágenes opcionales

### ❓ Requisito 5: Preguntas y Respuestas
**Descripción:**  
Los usuarios pueden hacer preguntas sobre productos.  
**Criterios de aceptación:**
- Notificación al preguntar y al responder
- Respuestas múltiples por producto

### 👤 Requisito 6: Perfil de Usuario
**Descripción:**  
Gestión del perfil, pedidos y valoraciones del usuario.  
**Criterios de aceptación:**
- Edición de datos personales
- Historial de pedidos con filtros
- Historial de valoraciones

### 🛒 Requisito 7: Carrito de Compras
**Descripción:**  
Carrito individual con gestión de stock y confirmaciones.  
**Criterios de aceptación:**
- Refleja stock actual
- Notificación si algún producto está agotado
- Confirmación al realizar pedido
- Procesos de pago/envío en microservicios separados

### 📦 Requisito 8: Pedidos y Facturas
**Descripción:**  
Gestión y seguimiento de pedidos.  
**Criterios de aceptación:**
- Filtros por fecha
- Descarga de facturas (PDF/Excel)
- Estado actualizado en tiempo real
- Panel del vendedor para actualizar estado

### 🔔 Requisito 9: Notificaciones en Tiempo Real
**Descripción:**  
Sistema de notificaciones personalizado vía WebSocket.  
**Criterios de aceptación:**
- Microservicio dedicado
- Notificaciones específicas por tipo y usuario
- Historial ordenado
- Marcar como leído o eliminar

### 🔧 Requisito 10: Panel de Administrador
**Descripción:**  
Zona de gestión para administradores.  
**Criterios de aceptación:**
- Aprobación de solicitudes de vendedores
- Gestión de productos (estado, filtros)
- Supervisión de chats
- Estadísticas generales
- Reportes descargables

### 🧾 Requisito 11: Gestión de Vendedores
**Descripción:**  
Vendedores crean ofertas, supervisadas por admins.  
**Criterios de aceptación:**
- Proceso de revisión
- Notificación de decisiones
- Historial completo del vendedor

### 💬 Requisito 12: Chat en Vivo
**Descripción:**  
Comunicación entre clientes, vendedores y admins.  
**Criterios de aceptación:**
- Envío y recepción en tiempo real
- Identificación de roles (cliente, vendedor, admin)
- Soporte para texto, imágenes y documentos

---

## 📦 Integraciones

### 🔍 SonarQube (Community Dockerized)

**Objetivo:** Análisis estático de código para frontend y backend.

**Backend:**  
- Jacoco + Maven  
- Informe desde `pom.xml`  
- [Ejemplo de configuración](https://github.gsissc.myatos.net/ES-TEF-CEDEI-FORMACIONES/E-commerce-Back/blob/main/Product/pom.xml)

**Frontend:**  
- `@sonar/scan`  
- Generar informe de cobertura con Jest  
- Configuración mediante `sonar-project.properties`

**Ejemplo de ejecución:**
```bash
sonar-scanner -D"sonar.projectKey=E-commerce-front"               -D"sonar.sources=."               -D"sonar.host.url=http://localhost:9001"               -D"sonar.token=<TOKEN>"
```

### 🖼️ MinIO (Gestión de Imágenes)

**Objetivo:** Almacenamiento de imágenes de productos.

**Dependencia:**
```xml
<dependency>
  <groupId>io.minio</groupId>
  <artifactId>minio</artifactId>
  <version>RELEASE</version>
</dependency>
```

**Configuración (`application.properties`):**
```properties
minio.url=http://localhost:9002
minio.access.name=LLRE3TAOW7H2BX0WBZXC
minio.access.secret=LYIXToohJ0XZwr9szLCdrPQ3Xao58U53NZwl95Ls
minio.bucket.name=products
```

**Docker Compose:**
```yaml
minio:
  image: quay.io/minio/minio:RELEASE.2022-02-18T01-50-10Z
  volumes:
    - ./data:/data
  ports:
    - 9002:9002
    - 9003:9003
  environment:
    MINIO_ROOT_USER: 'root'
    MINIO_ROOT_PASSWORD: '12345678'
    MINIO_ADDRESS: ':9002'
    MINIO_CONSOLE_ADDRESS: ':9003'
  command: minio server /data
```

---

## 🛠️ Estructura de Microservicios

> La plataforma está basada en arquitectura de microservicios, con separación clara entre dominios: usuarios, productos, carrito, pedidos, notificaciones, administración y chat.

- Todos los eventos entre servicios son gestionados por Kafka
- La comunicación en tiempo real se basa en WebSockets
- Cada microservicio incluye su propia lógica de negocio y acceso a la base de datos
- Notificaciones y autenticación se gestionan de forma centralizada

---

## 📄 Licencia

Este proyecto es propiedad del equipo de desarrollo interno y no está disponible públicamente bajo una licencia de código abierto, salvo que se indique lo contrario.
