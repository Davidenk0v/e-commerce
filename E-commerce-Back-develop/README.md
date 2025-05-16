# E-commerce-Back 

## Probar la aplicación (desarrollo)

En el punto actual, el proyecto no dispone de un entorno de pruebas o producción desde el que poder interactuar con las aplicaciones. Se depende de un entorno local en el que se puedan correr las aplicaciones. Los requisitos son:

- Docker para servicios y bases de datos

- Node.js para el front

- IDE que permita lanzar los proyectos de Spring que utilizan versiones de Java 17 y 21

### 1. Clonar los Repositorios

- Clonar Backend:

```console
    git clone https://github.gsissc.myatos.net/ES-TEF-CEDEI-FORMACIONES/E-commerce-Back.git
```

-  Clonar Frontend:

```console
    git clone https://github.gsissc.myatos.net/ES-TEF-CEDEI-FORMACIONES/E-commerce-Front.git
```

 ### 2. Levantar contenedores con servicios y bases de datos

 Lanzar el docker-compose que se encuentra en la raíz del repositorio E-commerce-Back:

 ```console
    docker-compose up -d
 ```

 ### 3. Arrancar la aplicación <i>config</i>

 Esta aplicación contiene los ficheros yml de configuración de los microservicios que forman parte del sistema. Debe estar disponible en el momento de arrancar cada uno de ellos para que puedan obtener de ella su correspondiente configuración.

 ### 4. Arrancar la aplicación <i>eureka</i>

 Eureka permite que los diferentes microservicios se comuniquen entre sí sin necesidad de apuntar a direcciones IP o puertos fijos.

 [Ver más sobre Eureka](E-commerce-Back)

 ### 5. Arrancar las aplicaciones de microservicios

 Una vez los servicios de configuración y Eureka están levantados, no importa el orden en que los microservicios se vayan levantando. Todos ellos están configurados para apoyarse en Eureka para ir descubriendo dónde están los demás microservicios y comunicarse con éstos.

 ### 6. Instalar las dependencias del front

Dentro de E-commerce-Front, posicionarse en la raíz del proyecto: E-commerce-Front/e-commerce. Una vez ahí, instalar las dependencias:

```console
    npm i
```

### 7. Lanzar la aplicación en modo desarrollo (dev) 

```console
    npm run dev
```

Una vez la aplicación esté lanzada, podemos acceder a ella desde nuestro browser: [http://localhost:5173](http://localhost:5173).

### 8. Creación de usuario en MinIO para la gestión de imagenes.

Primero debemos ir a la interfaz gráfica de Minio que está corriendo en el puerto:
```port
http://localhost:9003/login
```

Luego iniciamos sesión con las siguientes credenciales:

```password
username: root
password: 12345678
```

Luego en el menú de la izquierda hacemos click en ''Identify'' y luego sobre Users.
![minio1](https://github.gsissc.myatos.net/ES-TEF-CEDEI-FORMACIONES/E-commerce-Back/assets/13198/a7f8c257-775e-46bf-a5db-bb60ae354519)

Luego en la pantalla de Users, a la derecha, le damos sobre Create User + y nos saldrá esta pantalla:
![minio2](https://github.gsissc.myatos.net/ES-TEF-CEDEI-FORMACIONES/E-commerce-Back/assets/13198/e95ddcac-72fe-4092-84b2-0500b90fa8ab)

Elegimos un nombre y una contraseña y marcamos la casilla readWrite como en la imagen. Le damos a guardar y luego nos aparecerá el usuario creado.

Una vez creado el usuario hacemos click sobre el y nos saldrá la siguiente pantalla. Una vez ahí le damos click a Service Accounts:
![minio3](https://github.gsissc.myatos.net/ES-TEF-CEDEI-FORMACIONES/E-commerce-Back/assets/13198/ce658df9-e7a7-41c1-b9c5-566ddce528d7)

Nos saldrá una pantalla como la de la imagen y tendrémos que hacer click sobre Create Service Account +:
![minio4](https://github.gsissc.myatos.net/ES-TEF-CEDEI-FORMACIONES/E-commerce-Back/assets/13198/0f1181da-d97d-442a-96d1-cd7be32f6b1a)

Luego nos saldrá el siguiente modal:
![minio5](https://github.gsissc.myatos.net/ES-TEF-CEDEI-FORMACIONES/E-commerce-Back/assets/13198/427181cb-d887-4fdd-8322-b9ef0551726b)

Y ahora marcaremos los Customize Credentials a ON y rellenamos el Access Key y el Secret Key indicados aquí:
```yml
    Access Key: 3C96HQCAU3MG3T4ABWUD
    Secret Key: h6npXEt+4+eOuPRyq1uRMYKOP5r9SNxtOEJ3RILn
```

Una vez rellenano le damos a crear y ya estaría MinIO listo para el uso.
