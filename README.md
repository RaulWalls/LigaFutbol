# LigaFutbol

Sistema de gestión de una liga de fútbol con interfaz gráfica Swing, desarrollado en Java siguiendo el patrón MVC (Modelo-Vista-Controlador). Permite administrar equipos, jugadores y generar calendarios de partidos con algoritmo de Berger.

---

## Tabla de Contenidos

- [Estructura del Proyecto](#estructura-del-proyecto)
- [Arquitectura](#arquitectura)
- [Modelos](#modelos)
- [Controladores](#controladores)
- [Vistas](#vistas)
- [Excepciones](#excepciones)
- [Utilidades y Persistencia](#utilidades-y-persistencia)
- [Flujo de Funcionamiento](#flujo-de-funcionamiento)
- [Tecnologías](#tecnologías)

---

## Estructura del Proyecto

```
LigaFutbol/
├── Main.java                                   ← Punto de entrada de la aplicación
├── files/
│   ├── calendario.dat                          ← Calendario serializado (autogenerado)
│   ├── equipos.dat                             ← Lista de equipos serializada (autogenerado)
│   └── jugadores.dat                           ← Lista de jugadores serializada (autogenerado)
├── src/
│   ├── controller/
│   │   ├── CalendarioController.java           ← Lógica de generación y filtrado del calendario
│   │   ├── EquipoController.java               ← CRUD de equipos, manejo de eventos de vista
│   │   └── JugadorController.java              ← CRUD de jugadores, filtrado por equipo
│   ├── exception/
│   │   ├── LigaException.java                  ← Excepción base del sistema
│   │   ├── EquipoDuplicadoException.java        ← Nombre de equipo ya registrado
│   │   ├── EquipoNotFoundException.java         ← Equipo no encontrado por ID o nombre
│   │   ├── JugadorNotFoundException.java        ← Jugador no encontrado por ID
│   │   ├── PartidoInvalidoException.java        ← Menos de 2 equipos al generar calendario
│   │   └── PersistenciaException.java          ← Error al leer/escribir archivos .dat
│   ├── model/
│   │   ├── Calendario.java                     ← Algoritmo de Berger, gestión de partidos
│   │   ├── Equipo.java                         ← Entidad equipo (UUID, nombre, sede, estadio)
│   │   ├── Jugador.java                        ← Entidad jugador (UUID, nombre, equipo, fecha nac.)
│   │   ├── LigaManager.java                    ← Fachada central de lógica de negocio
│   │   └── Partido.java                        ← Entidad partido (local, visitante, jornada)
│   ├── resources/
│   │   ├── equipo.png                          ← Ícono del diálogo de equipo
│   │   ├── jugador.png                         ← Ícono del diálogo de jugador
│   │   └── logo.png                            ← Ícono de la ventana principal
│   ├── util/
│   │   └── ArchivoUtil.java                    ← Serialización/deserialización de objetos
│   └── view/
│       ├── CalendarioPanel.java                ← Panel de visualización del calendario
│       ├── DialogoEquipo.java                  ← Modal para agregar/editar equipos
│       ├── DialogoJugador.java                 ← Modal para agregar/editar jugadores
│       ├── EquipoPanel.java                    ← Panel CRUD de equipos con tabla y búsqueda
│       ├── JugadorPanel.java                   ← Panel CRUD de jugadores con filtro por equipo
│       └── MainFrame.java                      ← Ventana principal con pestañas (JTabbedPane)
└── README.md
```

---

## Arquitectura

El proyecto sigue el patrón **MVC** estricto:

```
┌─────────────┐     eventos      ┌──────────────────┐     lógica      ┌────────────────┐
│    Vista    │ ─────────────►   │   Controlador    │ ─────────────►  │    Modelo      │
│  (view/*)   │ ◄─────────────   │  (controller/*)  │ ◄─────────────  │   (model/*)    │
└─────────────┘    refresca      └──────────────────┘    resultado    └────────────────┘
                                                                              │
                                                                    ┌─────────▼────────┐
                                                                    │  Persistencia    │
                                                                    │  (files/*.dat)   │
                                                                    └──────────────────┘
```

**Relaciones entre entidades:**

```
Equipo (1) ──────────────────── (N) Jugador
   │                                    │
   └──── (N) ────── Partido ───── (N) ──┘
              (local / visitante)
```

---

## Modelos

### `Main.java`

Punto de entrada. Instancia el modelo (`LigaManager`), crea la ventana (`MainFrame`) y conecta los tres controladores con sus paneles respectivos. Toda la inicialización se lanza en el hilo de despacho de eventos de Swing (EDT).

---

### `src/model/LigaManager.java`

Fachada central que encapsula toda la lógica de negocio. Carga los datos serializados al arrancar y expone métodos para las tres áreas funcionales.

**Constantes de ruta:**

| Constante        | Valor                    |
|------------------|--------------------------|
| `RUTA_EQUIPOS`   | `files/equipos.dat`      |
| `RUTA_JUGADORES` | `files/jugadores.dat`    |
| `RUTA_CALENDARIO`| `files/calendario.dat`   |

**Métodos — Equipos:**

| Firma                                      | Descripción                                                            |
|--------------------------------------------|------------------------------------------------------------------------|
| `agregarEquipo(Equipo)`                    | Agrega equipo; lanza `EquipoDuplicadoException` si el nombre ya existe |
| `eliminarEquipo(String id)`                | Elimina equipo; lanza `EquipoNotFoundException` si no existe           |
| `modificarEquipo(String id, Equipo datos)` | Actualiza campos; puede lanzar ambas excepciones anteriores            |
| `buscarEquipoPorId(String id)`             | Retorna equipo o lanza `EquipoNotFoundException`                       |
| `buscarEquipoPorNombre(String nombre)`     | Búsqueda por nombre exacto                                             |
| `getEquipos()`                             | Retorna lista completa de equipos                                      |
| `guardarEquipos()`                         | Serializa lista a `files/equipos.dat`                                  |

**Métodos — Jugadores:**

| Firma                                         | Descripción                                                |
|-----------------------------------------------|------------------------------------------------------------|
| `agregarJugador(Jugador)`                     | Agrega jugador; verifica que el equipo referenciado exista |
| `eliminarJugador(String id)`                  | Lanza `JugadorNotFoundException` si no existe              |
| `modificarJugador(String id, Jugador datos)`  | Actualiza campos                                           |
| `buscarJugadorPorId(String id)`               | Retorna jugador o lanza `JugadorNotFoundException`         |
| `getJugadores()`                              | Lista completa de jugadores                                |
| `getJugadoresPorEquipo(String equipoId)`      | Filtra jugadores de un equipo específico                   |
| `guardarJugadores()`                          | Serializa lista a `files/jugadores.dat`                    |

**Métodos — Calendario:**

| Firma                 | Descripción                                                                                           |
|-----------------------|-------------------------------------------------------------------------------------------------------|
| `generarCalendario()` | Delega a `Calendario.generarCalendario()`; lanza `PartidoInvalidoException` si hay menos de 2 equipos |
| `getCalendario()`     | Retorna objeto `Calendario`                                                                           |
| `limpiarCalendario()` | Reinicia el calendario                                                                                |
| `guardarCalendario()` | Serializa objeto `Calendario` a `files/calendario.dat`                                                |

---

### `src/model/Equipo.java`

Representa un equipo de fútbol. Implementa `Serializable` para persistencia.

| Campo              | Tipo     | Descripción                          |
|--------------------|----------|--------------------------------------|
| `id`               | `String` | UUID generado automáticamente        |
| `nombre`           | `String` | Nombre del equipo (único en la liga) |
| `sede`             | `String` | Ciudad o localidad                   |
| `nombreEstadio`    | `String` | Nombre del estadio                   |
| `directorTecnico`  | `String` | Nombre del director técnico          |
| `dueno`            | `String` | Nombre del dueño                     |

---

### `src/model/Jugador.java`

Representa un jugador de fútbol. Implementa `Serializable`.

| Campo              | Tipo        | Descripción                              |
|--------------------|-------------|------------------------------------------|
| `id`               | `String`    | UUID generado automáticamente            |
| `nombre`           | `String`    | Nombre completo                          |
| `direccion`        | `String`    | Dirección de residencia                  |
| `fechaNacimiento`  | `LocalDate` | Fecha de nacimiento (formato dd/MM/yyyy) |
| `lugarNacimiento`  | `String`    | Lugar de nacimiento                      |
| `equipoId`         | `String`    | ID del equipo al que pertenece           |

El método `getEdad()` calcula la edad actual usando `java.time.Period`.

---

### `src/model/Partido.java`

Representa un partido entre dos equipos. Implementa `Serializable`.

| Campo              | Tipo     | Descripción                                       |
|--------------------|----------|---------------------------------------------------|
| `id`               | `String` | UUID generado automáticamente                     |
| `equipoLocalId`    | `String` | ID del equipo que juega en casa                   |
| `equipoVisitanteId`| `String` | ID del equipo visitante                           |
| `jornada`          | `int`    | Número de jornada                                 |
| `resultado`        | `String` | `null` hasta registrarse (formato `GolesL-GolesV`)|

El método `isTieneResultado()` verifica si el partido tiene resultado registrado.

---

### `src/model/Calendario.java`

Gestiona la lista de partidos generados mediante el **algoritmo de Berger** (sistema de round-robin).

**Constante:**
- `BYE_ID = "equipo-bye-descanso"` — ID fijo para el equipo virtual de descanso cuando hay número impar de equipos. El nombre que se muestra en pantalla para este equipo es `"LOBOS DE LA FCC"` con sede en Puebla.

**Algoritmo de Berger:**
- Si hay número impar de equipos, se añade un equipo BYE para equilibrar.
- **Fase de ida:** N−1 jornadas (donde N = número de equipos, ajustado a par).
- **Fase de vuelta:** Se invierten local y visitante de la fase de ida.
- **Total de jornadas:** `2 × (N − 1)`.

| Campo          | Tipo           | Descripción                          |
|----------------|----------------|--------------------------------------|
| `partidos`     | `List<Partido>`| Lista de todos los partidos          |
| `totalJornadas`| `int`          | Número total de jornadas             |
| `generado`     | `boolean`      | Indica si el calendario fue generado |

Métodos clave: `generarCalendario(List<Equipo>)`, `getPartidosPorJornada(int)`, `limpiar()`.

---

## Controladores

### `src/controller/EquipoController.java`

Maneja los eventos del `EquipoPanel` y coordina con `LigaManager`.

| Método                 | Acción                                                                 |
|------------------------|------------------------------------------------------------------------|
| `onAgregarEquipo()`    | Abre `DialogoEquipo`, valida, agrega, guarda y refresca la tabla       |
| `onEliminarEquipo()`   | Solicita confirmación, elimina equipo y sus jugadores en cascada       |
| `onModificarEquipo()`  | Abre `DialogoEquipo` precargado, actualiza datos y guarda              |
| `onBuscarEquipo()`     | Filtra la tabla por nombre (case-insensitive)                          |
| `onRefrescarLista()`   | Recarga la tabla desde el modelo                                       |

Excepciones manejadas: `EquipoDuplicadoException`, `EquipoNotFoundException`, `PersistenciaException`.

---

### `src/controller/JugadorController.java`

Maneja los eventos del `JugadorPanel`.

| Método                        | Acción                                                           |
|-------------------------------|------------------------------------------------------------------|
| `onAgregarJugador()`          | Verifica que exista al menos 1 equipo, abre `DialogoJugador`     |
| `onEliminarJugador()`         | Solicita confirmación y elimina el jugador seleccionado          |
| `onModificarJugador()`        | Abre `DialogoJugador` precargado y actualiza datos               |
| `onFiltrarPorEquipo(String)`  | Filtra la tabla por el equipo seleccionado en el combo           |
| `onRefrescarLista()`          | Recarga tabla y combo de equipos desde el modelo                 |

Excepciones manejadas: `JugadorNotFoundException`, `EquipoNotFoundException`, `PersistenciaException`.

---

### `src/controller/CalendarioController.java`

Maneja los eventos del `CalendarioPanel`.

| Método                       | Acción                                                                                                 |
|------------------------------|--------------------------------------------------------------------------------------------------------|
| `onGenerarCalendario()`      | Si ya fue generado, pide confirmación; ejecuta algoritmo de Berger y guarda                            |
| `onFiltrarJornada(int)`      | Muestra solo los partidos de la jornada indicada                                                       |
| `onRegistrarResultado()`     | Obtiene el partido seleccionado, valida el formato `GolesL-GolesVisitante`, guarda y refresca la celda |
| `onLimpiarCalendario()`      | Solicita confirmación, limpia objeto `Calendario` y guarda                                             |

Excepciones manejadas: `PartidoInvalidoException` (menos de 2 equipos), `PersistenciaException` (al guardar resultado).

---

## Vistas

### `src/view/MainFrame.java`

Ventana principal de la aplicación.

- **Título:** `"Liga Mexicana de Futbol"`
- **Tamaño:** 900 × 600 px
- **Ícono:** `src/resources/logo.png`
- **Layout:** `JTabbedPane` con tres pestañas: Equipos, Jugadores, Calendario

Expone `mostrarMensajeError(String)` y `mostrarMensajeExito(String)` para que los controladores muestren retroalimentación al usuario.

---

### `src/view/EquipoPanel.java`

Panel de gestión de equipos.

- **Norte:** Campo de texto de búsqueda + botón "Buscar"
- **Centro:** `JTable` con columnas: Nombre, Sede, Estadio, Director Técnico, Dueño
- **Sur:** Botones "Agregar", "Editar", "Eliminar"

---

### `src/view/JugadorPanel.java`

Panel de gestión de jugadores.

- **Norte:** `JComboBox` para filtrar por equipo + botón "Todos"
- **Centro:** `JTable` con columnas: Nombre, Dirección, Fecha Nac., Lugar Nac., Equipo, Edad
- **Sur:** Botones "Agregar", "Editar", "Eliminar"

---

### `src/view/CalendarioPanel.java`

Panel de visualización y edición del calendario.

- **Norte:** `JSpinner` de jornada + botón "Filtrar jornada" + botón "Generar Calendario"
- **Centro:** `JTable` con columnas: Jornada, Local, Visitante, Resultado (solo lectura; la edición se hace mediante el botón)
- **Sur:** Botón "Registrar Resultado" + botón "Limpiar Calendario"

**Lista paralela `partidosActuales`:** Se mantiene sincronizada con las filas de la tabla en cada llamada a `mostrarCalendario()` o `mostrarPartidos()`. Permite recuperar el objeto `Partido` real a partir del índice de fila seleccionado, sin necesitar columnas ocultas.

| Método público                          | Descripción                                                                 |
|-----------------------------------------|-----------------------------------------------------------------------------|
| `getPartidoSeleccionado()`              | Retorna el `Partido` de la fila seleccionada, o `null` si no hay selección  |
| `actualizarResultadoEnTabla(Partido p)` | Refresca solo la celda de resultado de la fila correspondiente al partido   |

El método privado `resolverNombre(String equipoId, List<Equipo>)` traduce el `BYE_ID` al nombre `"LOBOS DE LA FCC"` en la tabla.

---

### `src/view/DialogoEquipo.java`

Diálogo modal (`JDialog`) para crear o editar un equipo.

- **Campos obligatorios:** Nombre, Sede, Estadio, Director Técnico, Dueño
- **Layout:** `GridBagLayout` con ícono ilustrativo (`equipo.png`)
- `mostrarYObtenerResultado()` retorna el `Equipo` configurado o `null` si el usuario canceló

---

### `src/view/DialogoJugador.java`

Diálogo modal (`JDialog`) para crear o editar un jugador.

- **Campos obligatorios:** Nombre, Dirección, Lugar de nacimiento, Fecha de nacimiento, Equipo
- **Validaciones:** campos no vacíos, formato de fecha `dd/MM/yyyy`, equipo seleccionado
- **Layout:** `GridBagLayout` con ícono ilustrativo (`jugador.png`)
- `mostrarYObtenerResultado()` retorna el `Jugador` configurado o `null` si el usuario canceló

---

## Excepciones

Todas heredan de `LigaException extends Exception`.

| Clase                        | Cuándo se lanza                                             |
|------------------------------|-------------------------------------------------------------|
| `LigaException`              | Clase base; no se lanza directamente                        |
| `EquipoDuplicadoException`   | Nombre de equipo ya registrado en la liga                   |
| `EquipoNotFoundException`    | Equipo no encontrado por ID o por nombre                    |
| `JugadorNotFoundException`   | Jugador no encontrado por ID                                |
| `PartidoInvalidoException`   | Intento de generar calendario con menos de 2 equipos        |
| `PersistenciaException`      | Error de I/O al leer o escribir archivos `.dat`             |

---

## Utilidades y Persistencia

### `src/util/ArchivoUtil.java`

Clase de utilidad estática para serialización de objetos Java.

| Método                               | Descripción                                                             |
|--------------------------------------|-------------------------------------------------------------------------|
| `guardarObjeto(Object, String ruta)` | Serializa el objeto al archivo indicado                                 |
| `cargarObjeto(String ruta)`          | Deserializa y retorna el objeto; retorna `null` si no existe el archivo |
| `archivoExiste(String ruta)`         | Verifica existencia del archivo                                         |

Los archivos `.dat` se generan automáticamente en la carpeta `files/` la primera vez que se guarda información.

---

## Flujo de Funcionamiento

### Arranque

```
main()
  └─► new LigaManager()          ← carga files/*.dat si existen
  └─► new MainFrame()            ← crea ventana con 3 paneles
  └─► new EquipoController()     ← se conecta a EquipoPanel
  └─► new JugadorController()    ← se conecta a JugadorPanel
  └─► new CalendarioController() ← se conecta a CalendarioPanel
  └─► refrescar vistas           ← poblar tablas con datos cargados
  └─► frame.inicializar()        ← setVisible(true) en hilo EDT
```

### Ciclo CRUD de Equipos

```
Usuario → [Agregar] en EquipoPanel
  └─► EquipoController.onAgregarEquipo()
        └─► DialogoEquipo.mostrarYObtenerResultado()
              └─► (usuario llena campos y acepta)
        └─► LigaManager.agregarEquipo(equipo)
        └─► LigaManager.guardarEquipos()   ← serializa a files/equipos.dat
        └─► EquipoPanel.refrescarTabla()
```

### Generación de Calendario

```
Usuario → [Generar Calendario] en CalendarioPanel
  └─► CalendarioController.onGenerarCalendario()
        └─► (si ya existe) → JOptionPane confirmación
        └─► LigaManager.generarCalendario()
              └─► Calendario.generarCalendario(equipos)  ← algoritmo de Berger
        └─► LigaManager.guardarCalendario()  ← serializa a files/calendario.dat
        └─► CalendarioPanel.mostrarCalendario()
```

---

## Tecnologías

| Categoría            | Detalle                                                |
|----------------------|--------------------------------------------------------|
| Lenguaje             | Java (SE)                                              |
| GUI                  | Java Swing (`javax.swing.*`, `java.awt.*`)             |
| Fechas               | `java.time.LocalDate`, `java.time.Period`              |
| Persistencia         | Serialización Java (`ObjectInputStream/OutputStream`)  |
| IDs únicos           | `java.util.UUID`                                       |
| Patrón               | MVC (Modelo-Vista-Controlador)                         |
| Control de versiones | Git                                                    |
