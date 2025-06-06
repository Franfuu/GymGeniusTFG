# GymGenius - Sistema de Gestión de Gimnasios

## Descripción
GymGenius es una aplicación completa para la gestión de gimnasios que permite administrar clientes, empleados, clases, salas, equipamiento y rutinas de entrenamiento. El sistema está desarrollado en Java con JavaFX para ofrecer una interfaz gráfica intuitiva y utiliza Hibernate para la persistencia de datos.

## Características principales

### Gestión de usuarios
- **Clientes**: Registro, modificación y eliminación de perfiles
- **Empleados/Instructores**: Administración del personal del gimnasio
- **Seguridad**: Sistema de autenticación con contraseñas hasheadas mediante BCrypt

### Gestión de instalaciones
- **Salas**: Administración de espacios del gimnasio
- **Máquinas**: Registro y asignación de equipamiento a salas

### Gestión de actividades
- **Clases**: Programación de actividades con horarios y asignación de instructores
- **Inscripciones**: Sistema para que los clientes se inscriban a clases

### Rutinas de entrenamiento
- **Creación de rutinas**: Asignación de rutinas personalizadas a clientes
- **Ejercicios**: Catálogo de ejercicios clasificados por grupo muscular
- **Exportación**: Funcionalidad para exportar rutinas a Excel

## Tecnologías utilizadas
- **Lenguaje**: Java
- **Interfaz gráfica**: JavaFX
- **Persistencia**: Hibernate + MariaDB
- **Seguridad**: BCrypt para hashing de contraseñas
- **Exportación de datos**: Apache POI

## Requisitos del sistema
- Java 17 o superior
- MariaDB 10.x o MySQL 8.x
- Maven 3.8.x

## Instalación

1. **Clonar el repositorio**
   ```
   git clone https://github.com/Franfuu/GymGeniusTFG.git
   cd GymGeniusTFG
   ```

2. **Configurar la base de datos**
    - Ejecutar el script SQL ubicado en `src/main/resources/gymgenius.sql`
    - Configurar las credenciales de conexión en el archivo de configuración

3. **Compilar el proyecto**
   ```
   mvn clean install
   ```

4. **Ejecutar la aplicación**
   ```
   mvn javafx:run
   ```

## Estructura del proyecto
- `src/main/java/com/github/Franfuu/controllers`: Controladores JavaFX
- `src/main/java/com/github/Franfuu/model`: Entidades y lógica de negocio
- `src/main/java/com/github/Franfuu/model/dao`: Acceso a datos
- `src/main/java/com/github/Franfuu/model/entities`: Entidades JPA/Hibernate
- `src/main/resources/com/github/Franfuu/view`: Vistas FXML
- `src/main/resources`: Recursos de la aplicación

## Relaciones en la base de datos
La aplicación implementa eliminación en cascada para mantener la integridad de los datos:
- Al eliminar un cliente, se eliminan sus rutinas e inscripciones
- Al eliminar una sala, se eliminan las clases programadas y las máquinas asignadas
- Al eliminar una clase, se eliminan todas sus inscripciones

## Características de seguridad
- Almacenamiento seguro de contraseñas mediante algoritmo BCrypt
- Validación de credenciales sin almacenamiento de contraseñas en texto plano

## Funcionalidades principales
- **Gestión de clientes**: Alta, baja y modificación de datos
- **Gestión de empleados**: Control del personal del gimnasio
- **Programación de clases**: Creación y asignación de horarios y salas
- **Inscripción a clases**: Proceso para que clientes se apunten a actividades
- **Gestión de rutinas**: Creación de rutinas personalizadas
- **Exportación de rutinas**: Generación de documentos Excel con detalles de ejercicios

## Autor
Francisco (Franfuu)

## Licencia
Este proyecto está disponible como software libre bajo los términos establecidos por el autor.