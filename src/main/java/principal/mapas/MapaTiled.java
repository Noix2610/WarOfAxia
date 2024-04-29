/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.mapas;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import principal.Constantes;
import principal.ElementosPrincipales;
import principal.GestorPrincipal;
import principal.control.GestorControles;
import principal.dijkstra.Dijkstra;
import principal.dijkstra.Nodo;
import principal.entes.Enemigo;
import principal.entes.RegistroEnemigos;
import principal.graficos.SuperficieDibujo;
import principal.herramientas.CalculadoraDistancia;
import principal.herramientas.CargadorRecursos;
import principal.herramientas.DibujoDebug;
import principal.herramientas.EscaladorElementos;
import principal.herramientas.GeneradorTooltip;
import principal.inventario.ContenedorObjetos;
import principal.inventario.Objeto;
import principal.inventario.ObjetoUnicoTiled;
import principal.inventario.RegistroObjetos;
import principal.inventario.RegistroTiendas;
import principal.inventario.TipoObjeto;
import principal.inventario.armaduras.Armadura;
import principal.inventario.armas.Arma;
import principal.inventario.consumibles.Claves;
import principal.inventario.consumibles.Consumible;
import principal.inventario.joyas.Accesorio;
import principal.inventario.joyas.Joya;
import principal.maquinaestado.juego.menu_tienda.Tienda;
import principal.sprites.HojaSprites;
import principal.sprites.Sprite;

/**
 *
 * @author GAMER ARRAX
 */
public class MapaTiled {

    // Atributos del mapa
    private int anchoMapaTiles; // Ancho del mapa en número de tiles
    private int altoMapaTiles; // Alto del mapa en número de tiles
    private String siguienteMapa; // Nombre del mapa al que se accede al salir de este mapa
    private Point puntoInicial; // Punto de inicio del jugador en el mapa
    public Rectangle recMapa; // Rectángulo que representa el área del mapa
    public Tienda tiendaActiva; // Tienda activa en el mapa
    private boolean reproducirMusica = false; // Indica si se debe reproducir música en el mapa

    // Variables para el debounce (evitar múltiples acciones con un solo evento)
    long ultimoTiempoRecogida = 0;
    long tiempoDebouncing = 50; // Tiempo de debounce en milisegundos

    // Zonas de salida del mapa
    public ArrayList<Rectangle> zonasSalida;

    // Capas de sprites del mapa
    private ArrayList<CapaSprites> capaSprites1;
    private ArrayList<CapaSprites> capaSprites2;
    private ArrayList<CapaSprites> capasprites3;

    // Capa de colisiones del mapa
    private ArrayList<CapaColisiones> capaColisiones;

    // Capa de transparencias del mapa
    private ArrayList<CapaTransparencias> capaTransparencias;

    // Áreas de transparencia y colisión originales del mapa
    private ArrayList<Rectangle> areaTransparenciaOriginales;
    private ArrayList<Rectangle> areaColisionOriginales;

    // Paletas de sprites del mapa
    private Sprite[] paletaSprites1;
    private Sprite[] paletaSprites2;

    // Listas de objetos de la tienda
    public ArrayList<Objeto> objetosTiendaMapa; // Objetos disponibles en la tienda del mapa
    public ArrayList<Objeto> objetosTiendaActual; // Objetos actualmente disponibles en la tienda
    private boolean contenedorAbierto = false; // Indica si el contenedor de objetos está abierto

    // Objeto Dijkstra para el cálculo de rutas
    private Dijkstra dijkstra;

    // Listas de objetos y enemigos del mapa
    private ArrayList<ObjetoUnicoTiled> objetosMapa;
    public ArrayList<Objeto> objetosTienda; // Objetos disponibles en la tienda
    private ArrayList<Enemigo> enemigosMapa;

    // Listas de áreas de colisión y transparencia actualizadas
    public ArrayList<Rectangle> areasColisionActualizadas;
    public ArrayList<Rectangle> areasTransparenciaActualizadas;

    // Lista de contenedores de objetos
    public ArrayList<ContenedorObjetos> listaContenedores;
    private ContenedorObjetos contenedorActual; // Contenedor de objetos actualmente activo en el mapa

    // Lista de tiendas del mapa
    public ArrayList<Tienda> tiendas;

    // Ruta de la música del mapa
    private String rutaMusica;

    public MapaTiled(final String ruta) {
        // Inicialización de la lista de zonas de salida
        zonasSalida = new ArrayList<>();
        // Limpiar la lista de salidas del mapa anterior
        Salida.getSalidas().clear();

        // Leer el contenido del archivo JSON del mapa
        String contenido = CargadorRecursos.leerArchivoTexto(ruta);
        // Obtener el objeto JSON global del contenido
        JsonNode globalJSON = getObjetoJson(contenido);

        // Obtener información sobre el siguiente mapa
        obtenerInformacionSiguienteMapa(globalJSON);

        // Inicializar atributos básicos del mapa
        inicializarAtributosBasicos(globalJSON);

        // Inicializar las capas del mapa
        inicializarCapas(globalJSON);

        // Combinar todas las colisiones en un solo ArrayList
        combinarColisiones();

        // Combinar todas las transparencias en un solo ArrayList
        combinarTransparencias();

        // Inicializar el algoritmo de Dijkstra para el cálculo de rutas
        inicializarDijkstra();

        // Inicializar la paleta de sprites del mapa
        inicializarPaletaSprites(globalJSON);

        // Obtener los objetos del mapa
        obtenerObjetosMapa(globalJSON);

        // Obtener los enemigos del mapa
        obtenerEnemigosMapa(globalJSON);

        // Obtener los contenedores de objetos del mapa
        obtenerContenedoresMapa(globalJSON);

        // Obtener las tiendas del mapa
        obtenerTiendas(globalJSON);

        // Obtener la ruta de la música del mapa
        obtenerRutaMusica(globalJSON);
        System.out.println("" + rutaMusica);

        // Inicializar listas para áreas de colisión y transparencia actualizadas
        areasColisionActualizadas = new ArrayList<>();
        areasTransparenciaActualizadas = new ArrayList<>();

        // Inicializar listas de objetos de la tienda
        objetosTiendaMapa = new ArrayList<>();
        objetosTiendaActual = new ArrayList<>();

        // Inicializar la tienda activa
        tiendaActiva = new Tienda();
    }

    public void actualizar() {
        // Actualizar la lógica de los enemigos en el mapa
        actualizarEnemigos();

        // Actualizar las áreas de colisión del mapa
        actualizarAreasColision();

        // Actualizar las áreas de transparencia del mapa
        actualizarAreasTransparencia();

        // Actualizar la lógica de recogida de objetos en el mapa
        actualizarRecogidaObjeto();

        // Actualizar la lógica de los ataques en el mapa
        actualizarAtaques();

        // Actualizar la lógica de las zonas de salida del mapa
        actualizarZonaSalida();

        // Actualizar la lógica de las tiendas en el mapa
        actualizarTiendas();

        // Obtener la posición del jugador en el mapa
        Point punto = new Point(ElementosPrincipales.jugador.getPosicionXInt(),
                ElementosPrincipales.jugador.getPosicionYInt());

        // Obtener el nodo coincidente en la matriz de nodos del algoritmo de Dijkstra
        Point puntoCoincidente = dijkstra.getCoordenadasNodoCoincidente(punto);

        // Reiniciar y evaluar el algoritmo de Dijkstra desde el nodo coincidente
        dijkstra.reiniciarYEvaluar(puntoCoincidente);

        // Mostrar los elementos del contenedor actual, si hay alguno
        mostrarElementoscontenedor();

        // Si no se encuentra en la pantalla de título del juego
        if (!GestorPrincipal.pantallaTitulo) {
            // Si aún no se ha reproducido la música del mapa
            if (!reproducirMusica) {
                // Cambiar el archivo de música y reproducirlo
                GestorPrincipal.musica.cambiarArchivo("" + rutaMusica);
                GestorPrincipal.musica.repetir(0.7f);
                // Marcar que la música se ha reproducido
                reproducirMusica = true;
            }
        }
    }

    public void dibujar(Graphics2D g) {
        // Dibujar sprites del mapa desde la primera capa
        int intentosDibujo = 0;
        for (int i = 0; i < capaSprites1.size(); i++) {
            int[] spritesCapa = capaSprites1.get(i).getSprites();
            for (int y = 0; y < altoMapaTiles; y++) {
                for (int x = 0; x < anchoMapaTiles; x++) {
                    // Obtener el ID del sprite actual en la capa
                    long idSpriteActual = spritesCapa[x + y * anchoMapaTiles];
                    if (idSpriteActual != -1) {
                        // Calcular la posición del sprite en la pantalla relativa al jugador
                        int puntoX = x * Constantes.LADO_SPRITE - ElementosPrincipales.jugador.getPosicionXInt() + Constantes.MARGEN_X;
                        int puntoY = y * Constantes.LADO_SPRITE - ElementosPrincipales.jugador.getPosicionYInt() + Constantes.MARGEN_Y;

                        // OPTIMIZACIÓN DIBUJADO: Evitar dibujar sprites que están fuera de la pantalla
                        if (puntoX < 0 - Constantes.LADO_SPRITE || puntoX > Constantes.ANCHO_JUEGO
                                || puntoY < 0 - Constantes.LADO_SPRITE || puntoY > Constantes.ANCHO_JUEGO - 65) {
                            continue;
                        }

                        // Dibujar el sprite en la pantalla
                        intentosDibujo++;
                        DibujoDebug.dibujarImagen(g, paletaSprites1[(int) idSpriteActual].getImagen(), puntoX, puntoY);
                    }
                }
            }
        }

        // Dibujar objetos del mapa
        for (int i = 0; i < objetosMapa.size(); i++) {
            ObjetoUnicoTiled objetoActual = objetosMapa.get(i);
            // Calcular la posición del objeto en la pantalla relativa al jugador
            int puntoX = objetoActual.getPosicion().x - ElementosPrincipales.jugador.getPosicionXInt() + Constantes.MARGEN_X;
            int puntoY = objetoActual.getPosicion().y - ElementosPrincipales.jugador.getPosicionYInt() + Constantes.MARGEN_Y;
            // Dibujar el objeto en la pantalla
            DibujoDebug.dibujarImagen(g, objetoActual.getObjeto().getSprite().getImagen(), puntoX, puntoY);
        }

        // Dibujar contenedores de objetos del mapa
        for (int i = 0; i < listaContenedores.size(); i++) {
            ContenedorObjetos contenedorAct = listaContenedores.get(i);
            // Calcular la posición del contenedor en la pantalla relativa al jugador
            int puntoX = (int) contenedorAct.getPosicion().x - ElementosPrincipales.jugador.getPosicionXInt() + Constantes.MARGEN_X;
            int puntoY = (int) contenedorAct.getPosicion().y - ElementosPrincipales.jugador.getPosicionYInt() + Constantes.MARGEN_Y;
            // Dibujar el contenedor en la pantalla
            contenedorAct.dibujar(g, puntoX, puntoY);
        }

        // Dibujar enemigos del mapa
        for (int i = 0; i < enemigosMapa.size(); i++) {
            Enemigo enemigo = enemigosMapa.get(i);
            // Calcular la posición del enemigo en la pantalla relativa al jugador
            int puntoX = (int) enemigo.getPosicionX() - ElementosPrincipales.jugador.getPosicionXInt() + Constantes.MARGEN_X;
            int puntoY = (int) enemigo.getPosicionY() - ElementosPrincipales.jugador.getPosicionYInt() + Constantes.MARGEN_Y;
            // Dibujar el enemigo en la pantalla
            enemigo.dibujar(g, puntoX, puntoY);
        }
    }

    public void dibujar2daCapa(Graphics2D g) {

        // Dibujar sprites del mapa desde la segunda capa
        int intentosDibujo = 0;
        for (int i = 0; i < capaSprites2.size(); i++) {
            int[] spritesCapa = capaSprites2.get(i).getSprites();
            for (int y = 0; y < altoMapaTiles; y++) {
                for (int x = 0; x < anchoMapaTiles; x++) {
                    // Obtener el ID del sprite actual en la capa
                    long idSpriteActual = spritesCapa[x + y * anchoMapaTiles];
                    if (idSpriteActual != -1) {
                        // Calcular la posición del sprite en la pantalla relativa al jugador
                        int puntoX = x * Constantes.LADO_SPRITE - ElementosPrincipales.jugador.getPosicionXInt() + Constantes.MARGEN_X;
                        int puntoY = y * Constantes.LADO_SPRITE - ElementosPrincipales.jugador.getPosicionYInt() + Constantes.MARGEN_Y;

                        // OPTIMIZACIÓN DIBUJADO: Evitar dibujar sprites que están fuera de la pantalla
                        if (puntoX < 0 - Constantes.LADO_SPRITE || puntoX > Constantes.ANCHO_JUEGO
                                || puntoY < 0 - Constantes.LADO_SPRITE || puntoY > Constantes.ANCHO_JUEGO - 65) {
                            continue;
                        }

                        // Dibujar el sprite en la pantalla
                        intentosDibujo++;
                        DibujoDebug.dibujarImagen(g, paletaSprites2[(int) idSpriteActual].getImagen(), puntoX, puntoY);
                    }
                }
            }
        }

        /*DibujoDebug.dibujarRectanguloContorno(g, zonaSalida1, Color.red);
        DibujoDebug.dibujarRectanguloContorno(g, zonaSalida2, Color.red);
        DibujoDebug.dibujarRectanguloContorno(g, zonaSalida3, Color.red);
        DibujoDebug.dibujarRectanguloContorno(g, zonaSalida4, Color.red);
        DibujoDebug.dibujarRectanguloContorno(g, zonaSalida5, Color.red);
        DibujoDebug.dibujarRectanguloContorno(g, zonaSalida6, Color.red);
        DibujoDebug.dibujarRectanguloContorno(g, zonaSalida7, Color.red);*/
        // Dibujar áreas de las tiendas
        for (Tienda tiendaActual : tiendas) {
            DibujoDebug.dibujarRectanguloContorno(g, tiendaActual.getAreaTienda());
        }

        /*for (Rectangle rectagulo : areasColisionActualizadas) {
            DibujoDebug.dibujarRectanguloContorno(g, rectagulo, Color.blue);
        }

        for (Rectangle rectagulo : areasTransparenciaActualizadas) {
            DibujoDebug.dibujarRectanguloContorno(g, rectagulo, Color.white);
        }
        dibujarTooltipObjetosMapa(g, GestorPrincipal.sd);

        /*DibujoDebug.dibujarString(g, zonaSalida1.toString(), 10, 90, Color.white);
        DibujoDebug.dibujarString(g, zonaSalida2.toString(), 10, 100, Color.white);
        DibujoDebug.dibujarString(g, zonaSalida3.toString(), 10, 110, Color.white);
        DibujoDebug.dibujarString(g, zonaSalida4.toString(), 10, 120, Color.white);
        DibujoDebug.dibujarString(g, zonaSalida5.toString(), 10, 130, Color.white);
        DibujoDebug.dibujarString(g, zonaSalida6.toString(), 10, 140, Color.white);
        DibujoDebug.dibujarString(g, zonaSalida7.toString(), 10, 150, Color.white);*/
    }

    private void inicializarAtributosBasicos(JsonNode globalJSON) {
        // Obtener el ancho y el alto del mapa desde el objeto JSON global
        anchoMapaTiles = globalJSON.get("width").asInt();
        altoMapaTiles = globalJSON.get("height").asInt();

        // Obtener el objeto JSON asociado con "start" (punto inicial del jugador)
        JsonNode puntoInicialJSON = globalJSON.get("start");

        // Verificar si el nodo "start" existe en el JSON y es un objeto
        if (puntoInicialJSON != null && puntoInicialJSON.isObject()) {
            // Si existe, obtener las coordenadas x e y del nodo JSON de "start"
            int x = puntoInicialJSON.get("x").asInt();
            int y = puntoInicialJSON.get("y").asInt();

            // Actualizar el punto inicial de la instancia de Mapa
            this.puntoInicial = new Point(x, y); // Ajusta el método según la estructura de tu clase Mapa
        }
        else {
            // Si no existe, asignar el punto inicial predeterminado
            this.puntoInicial = Salida.puntoInicialSiguiente; // Ajusta esto según lo que necesites
        }
    }

    private void inicializarCapas(JsonNode globalJSON) {
        // Obtener el nodo JSON que contiene las capas del mapa
        JsonNode capas = globalJSON.get("layers");

        // Inicializar las listas para las diferentes capas
        this.capaSprites1 = new ArrayList<>();
        this.capaSprites2 = new ArrayList<>();
        this.capaColisiones = new ArrayList<>();
        this.capaTransparencias = new ArrayList<>();

        // Verificar si el nodo "layers" existe y es un array
        if (capas != null && capas.isArray()) {
            // Iterar sobre cada nodo de capa en el array
            for (JsonNode capaNode : capas) {
                // Obtener el tipo de capa (identificador)
                String tipo = capaNode.get("id").asText();

                // Según el tipo de capa, inicializar la capa correspondiente
                switch (tipo) {
                    case "1":
                    case "2":
                        inicializarCapaSprites1(capaNode);
                        break;
                    case "3":
                    case "4":
                        inicializarCapaSprites2(capaNode);
                        break;
                }
            }

            // Iterar nuevamente sobre cada nodo de capa
            for (JsonNode capaNode : capas) {
                // Obtener el tipo de capa (objectgroup para colisiones, objectgroup1 para transparencias)
                String tipo = capaNode.get("type").asText();

                // Según el tipo de capa, inicializar la capa correspondiente
                switch (tipo) {
                    case "objectgroup":
                        inicializarCapaColisiones(capaNode);
                        break;
                    case "objectgroup1":
                        inicializarCapaTransparencia(capaNode);
                        break;
                }
            }
        }
        else {
            // Si el nodo "layers" no está presente o no es un array, mostrar un mensaje de error
            System.err.println("La clave 'layers' no está presente o no es un array en el JSON.");
        }
    }

    // Método para combinar todas las áreas de colisión de las diferentes capas en un solo ArrayList
    private void combinarColisiones() {
        // Se crea un ArrayList para almacenar las áreas de colisión originales
        areaColisionOriginales = new ArrayList<>();

        // Iterar sobre cada capa de colisiones
        for (int i = 0; i < capaColisiones.size(); i++) {
            // Obtener los rectángulos de colisión de la capa actual
            Rectangle[] rectangulos = capaColisiones.get(i).getColisionables();

            // Iterar sobre cada rectángulo de colisión y agregarlo al ArrayList de áreas de colisión originales
            for (int j = 0; j < rectangulos.length; j++) {
                areaColisionOriginales.add(rectangulos[j]);
            }
        }
    }

// Método para combinar todas las áreas de transparencia de las diferentes capas en un solo ArrayList
    private void combinarTransparencias() {
        // Se crea un ArrayList para almacenar las áreas de transparencia originales
        areaTransparenciaOriginales = new ArrayList<>();

        // Iterar sobre cada capa de transparencias
        for (int i = 0; i < capaTransparencias.size(); i++) {
            // Obtener los rectángulos de transparencia de la capa actual
            Rectangle[] rectangulos = capaTransparencias.get(i).getColisionables();

            // Iterar sobre cada rectángulo de transparencia y agregarlo al ArrayList de áreas de transparencia originales
            for (int j = 0; j < rectangulos.length; j++) {
                areaTransparenciaOriginales.add(rectangulos[j]);
            }
        }
    }

// Método para inicializar el algoritmo de Dijkstra
    private void inicializarDijkstra() {
        // Crear una nueva instancia de Dijkstra con un punto inicial, el ancho y alto del mapa, y las áreas de colisión originales
        dijkstra = new Dijkstra(new Point(10, 10), anchoMapaTiles, altoMapaTiles, areaColisionOriginales);
    }

    // Método para inicializar la paleta de sprites del mapa
    private void inicializarPaletaSprites(JsonNode globalJSON) {
        // Obtener el objeto JSON asociado con "tilesets"
        JsonNode coleccionSprites = globalJSON.get("tilesets");

        // Verificar si existe y es un array
        if (coleccionSprites != null && coleccionSprites.isArray()) {
            // Inicializar variables para el total de sprites en la paleta
            int totalSprites = 0;

            // Calcular el total de sprites sumando el "tilecount" de cada grupo de sprites
            for (JsonNode datosGrupo : coleccionSprites) {
                totalSprites += datosGrupo.get("tilecount").asInt();
            }

            // Inicializar los arrays para las paletas de sprites
            paletaSprites1 = new Sprite[totalSprites];
            paletaSprites2 = new Sprite[totalSprites];

            // Variable para llevar el índice del sprite actual
            int spriteIndex = 0;

            // Iterar sobre cada grupo de sprites en "tilesets"
            for (JsonNode datosGrupo : coleccionSprites) {
                // Obtener información del grupo de sprites actual
                String nombreImagen = datosGrupo.get("image").asText();
                int anchoTile = datosGrupo.get("tilewidth").asInt();
                int altoTile = datosGrupo.get("tileheight").asInt();

                // Crear una hoja de sprites con la imagen del grupo
                HojaSprites hoja = new HojaSprites("/mapas/" + nombreImagen, anchoTile, altoTile, false);

                // Obtener el rango de IDs de los sprites en el grupo actual
                int primerSpriteColeccion = datosGrupo.get("firstgid").asInt() - 1;
                int ultimoSpriteColeccion = primerSpriteColeccion + datosGrupo.get("tilecount").asInt() - 1;

                // Crear un array de sprites para el grupo actual
                Sprite[] sprites = new Sprite[ultimoSpriteColeccion - primerSpriteColeccion + 1];
                for (int j = 0; j < sprites.length; j++) {
                    sprites[j] = hoja.getSprites(j);
                }

                // Asignar sprites a paletaSprites1 y paletaSprites2
                for (int i = 0; i < this.capaSprites1.size(); i++) {
                    CapaSprites capaActual = this.capaSprites1.get(i);
                    int[] spritesCapa = capaActual.getSprites();

                    for (int j = 0; j < spritesCapa.length; j++) {
                        int idSpriteActual = spritesCapa[j];
                        if (idSpriteActual >= primerSpriteColeccion && idSpriteActual <= ultimoSpriteColeccion) {
                            if (paletaSprites1[idSpriteActual] == null) {
                                paletaSprites1[idSpriteActual] = sprites[idSpriteActual - primerSpriteColeccion];
                            }
                        }
                    }
                }

                for (int i = 0; i < this.capaSprites2.size(); i++) {
                    CapaSprites capaActual = this.capaSprites2.get(i);
                    int[] spritesCapa = capaActual.getSprites();

                    for (int j = 0; j < spritesCapa.length; j++) {
                        int idSpriteActual = spritesCapa[j];
                        if (idSpriteActual >= primerSpriteColeccion && idSpriteActual <= ultimoSpriteColeccion) {
                            if (paletaSprites2[idSpriteActual] == null) {
                                paletaSprites2[idSpriteActual] = sprites[idSpriteActual - primerSpriteColeccion];
                            }
                        }
                    }
                }

                // Actualizar el índice del sprite actual
                spriteIndex += sprites.length;
            }
        }
        else {
            // Si no se encuentra el objeto "tilesets" o no es un array, mostrar un mensaje de error
            System.err.println("La clave 'tilesets' no está presente o no es un array en el JSON.");
        }
    }

    // Método para obtener los objetos del mapa a partir del JSON proporcionado
    private void obtenerObjetosMapa(JsonNode globalJSON) {
        // Inicializar la lista de objetos del mapa
        objetosMapa = new ArrayList<>();

        // Obtener el objeto JSON asociado con "objetos"
        JsonNode coleccionObjetos = globalJSON.get("objetos");

        // Verificar si existe y es un array
        if (coleccionObjetos != null && coleccionObjetos.isArray()) {
            // Iterar sobre cada objeto del array
            for (JsonNode objetoNode : coleccionObjetos) {
                // Obtener los datos del objeto
                int idObjeto = objetoNode.get("id").asInt();
                int cantidad = objetoNode.get("cantidad").asInt();
                int xObjeto = objetoNode.get("x").asInt();
                int yObjeto = objetoNode.get("y").asInt();

                // Crear un punto con la posición del objeto
                Point posicionObjeto = new Point(xObjeto, yObjeto);

                // Obtener el objeto correspondiente al ID del JSON
                Objeto objeto = RegistroObjetos.obtenerObjeto(idObjeto);

                // Establecer la cantidad del objeto
                objeto.setCantidad(cantidad);

                // Crear un objeto único a partir de los datos obtenidos y agregarlo a la lista de objetos del mapa
                ObjetoUnicoTiled objetoUnico = new ObjetoUnicoTiled(posicionObjeto, objeto, objeto.getCantidad());
                objetosMapa.add(objetoUnico);
            }
        }
        else {
            // Si no se encuentra el objeto "objetos" o no es un array, mostrar un mensaje de error
            System.err.println("La clave 'objetos' no está presente o no es un array en el JSON.");
        }
    }

// Método para obtener los enemigos del mapa a partir del JSON proporcionado
    private void obtenerEnemigosMapa(JsonNode globalJSON) {
        // Inicializar la lista de enemigos del mapa
        enemigosMapa = new ArrayList<>();

        // Obtener el objeto JSON asociado con "enemigos"
        JsonNode coleccionEnemigos = globalJSON.get("enemigos");

        // Verificar si existe y es un array
        if (coleccionEnemigos != null && coleccionEnemigos.isArray()) {
            // Iterar sobre cada enemigo del array
            for (JsonNode enemigoNode : coleccionEnemigos) {
                // Obtener los datos del enemigo
                int idEnemigo = getIntJson(enemigoNode, "id");
                int xEnemigo = getIntJson(enemigoNode, "x");
                int yEnemigo = getIntJson(enemigoNode, "y");

                // Verificar si los datos del enemigo no son nulos y son valores numéricos
                if (idEnemigo != 0 && xEnemigo != 0 && yEnemigo != 0) {
                    // Crear un punto con la posición del enemigo
                    Point posicionEnemigo = new Point(xEnemigo, yEnemigo);

                    // Obtener el enemigo correspondiente al ID del JSON
                    Enemigo enemigo = RegistroEnemigos.obtenerEnemigo(idEnemigo);

                    // Establecer la posición del enemigo y agregarlo a la lista de enemigos del mapa
                    enemigo.setPosicion(posicionEnemigo.x, posicionEnemigo.y);
                    enemigosMapa.add(enemigo);
                }
                else {
                    // Mostrar un mensaje de error si uno de los nodos de enemigos es nulo
                    System.err.println("Uno de los nodos de enemigos es nulo.");
                }
            }
        }
        else {
            // Si no se encuentra el objeto "enemigos" o no es un array, mostrar un mensaje de error
            System.err.println("La clave 'enemigos' no está presente o no es un array en el JSON.");
        }
    }

    // Método para obtener los contenedores del mapa a partir del JSON proporcionado
    private void obtenerContenedoresMapa(JsonNode globalJSON) {
        // Inicializar la lista de contenedores del mapa
        listaContenedores = new ArrayList<>();

        // Obtener el objeto JSON asociado con "contenedores"
        JsonNode coleccionContenedores = globalJSON.get("contenedores");

        // Verificar si existe y es un array
        if (coleccionContenedores != null && coleccionContenedores.isArray()) {
            // Iterar sobre cada contenedor del array
            for (JsonNode contenedorNode : coleccionContenedores) {
                // Obtener los datos del contenedor
                int idContenedor = contenedorNode.get("idContenedor").asInt();
                int xContenedor = contenedorNode.get("x").asInt();
                int yContenedor = contenedorNode.get("y").asInt();

                // Crear un punto con la posición del contenedor
                Point posicionContenedor = new Point(xContenedor, yContenedor);

                // Crear un área rectangular para el contenedor
                Rectangle areacontenedor = new Rectangle(xContenedor, yContenedor, 32, 32);

                // Crear el contenedor de objetos con los datos obtenidos
                ContenedorObjetos contenedor = new ContenedorObjetos(posicionContenedor, idContenedor, areacontenedor);

                // Obtener la colección de objetos dentro del contenedor
                JsonNode coleccionObjetos = contenedorNode.get("objetos");
                if (coleccionObjetos != null && coleccionObjetos.isArray()) {
                    // Iterar sobre cada objeto en la colección
                    for (JsonNode objetoNode : coleccionObjetos) {
                        // Obtener los datos del objeto dentro del contenedor
                        int idObjeto = objetoNode.get("idObjeto").asInt();
                        int cantidadObjeto = objetoNode.get("cantidad").asInt();

                        // Obtener el objeto del registro de objetos y establecer su cantidad
                        Objeto objeto = RegistroObjetos.obtenerObjeto(idObjeto);
                        objeto.setCantidad(cantidadObjeto);

                        // Agregar el objeto al contenedor
                        contenedor.getObjetos().add(objeto);
                    }
                }

                // Agregar el contenedor a la lista de contenedores y su área a las áreas de colisión originales
                listaContenedores.add(contenedor);
                areaColisionOriginales.add(contenedor.getArea());
            }
        }
        else {
            // Si no se encuentra el objeto "contenedores" o no es un array, mostrar un mensaje de error
            System.err.println("La clave 'contenedores' no está presente o no es un array en el JSON.");
        }
    }

// Método para obtener las tiendas del mapa a partir del JSON proporcionado
    private void obtenerTiendas(JsonNode globalJSON) {
        // Inicializar la lista de tiendas del mapa
        tiendas = new ArrayList<>();

        // Obtener el objeto JSON asociado con "tiendas"
        JsonNode coleccionTiendas = globalJSON.get("tiendas");
        if (coleccionTiendas != null && coleccionTiendas.isArray()) {
            // Iterar sobre cada tienda del array
            for (JsonNode tiendaNode : coleccionTiendas) {
                // Obtener los datos de la tienda
                int idTienda = getIntJson(tiendaNode, "id");
                int xTienda = getIntJson(tiendaNode, "x");
                int yTienda = getIntJson(tiendaNode, "y");
                int tipo = getIntJson(tiendaNode, "tienda");

                // Crear un punto con la posición de la tienda
                Point posTienda = new Point(xTienda, yTienda);

                // Crear una tienda con los datos obtenidos y agregarla a la lista de tiendas
                Tienda tienda = new Tienda(idTienda, posTienda, tipo);
                tiendas.add(tienda);
            }
        }
        else {
            // Si no se encuentra el objeto "tiendas" o no es un array, mostrar un mensaje de error
            System.err.println("La clave 'tiendas' no está presente o no es un array en el JSON.");
        }
    }

// Método para obtener la ruta de la música del mapa a partir del JSON proporcionado
    private void obtenerRutaMusica(JsonNode globalJSON) {
        // Obtener la ruta de la música del objeto JSON asociado con "ruta"
        JsonNode ruta = globalJSON.get("ruta");
        // Asignar la ruta de la música y eliminar las comillas dobles si existen
        rutaMusica = ruta.toString().replaceAll("\"", "");
    }

    private void obtenerInformacionSiguienteMapa(JsonNode globalJSON) {
        // Obtener el objeto JSON asociado con "salidas"
        JsonNode salidasJSON = globalJSON.get("salidas");

        // Verificar si existe y no está vacío
        if (salidasJSON != null && !salidasJSON.isEmpty()) {
            // Iterar sobre cada salida del array
            for (JsonNode salidaJSON : salidasJSON) {
                // Obtener las coordenadas de la salida en el mapa actual
                int xSalidaMapa = salidaJSON.get("x").asInt();
                int ySalidaMapa = salidaJSON.get("y").asInt();
                Point puntoSalidaMapa = new Point(xSalidaMapa, ySalidaMapa);

                // Obtener el nombre del siguiente mapa y las coordenadas de inicio en él
                String siguienteMapa = salidaJSON.get("mapaDestino").asText();
                JsonNode puntoInicialJSON = salidaJSON.get("punto inicial");
                int xInicioSiguienteMapa = puntoInicialJSON.get("x").asInt();
                int yInicioSiguienteMapa = puntoInicialJSON.get("y").asInt();
                Point puntoInicioSiguienteMapa = new Point(xInicioSiguienteMapa, yInicioSiguienteMapa);

                // Crear una nueva salida y su zona de salida correspondiente, y agregarlas a las listas respectivas
                Salida nuevaSalida = new Salida(puntoInicioSiguienteMapa, puntoSalidaMapa, siguienteMapa);
                Rectangle nuevaZonaSalida = new Rectangle(xSalidaMapa, ySalidaMapa, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);
                Salida.getSalidas().add(nuevaSalida);
                zonasSalida.add(nuevaZonaSalida);
            }
        }
        else {
            System.err.println("La clave 'salida' no está presente o está vacía en el JSON.");
        }
    }

    // Método para inicializar la capa de sprites 1 a partir de los datos proporcionados
    private void inicializarCapaSprites1(JsonNode datosCapa) {
        // Obtener el ancho, alto, posición x e y de la capa
        int anchoCapa = datosCapa.get("width").asInt();
        int altoCapa = datosCapa.get("height").asInt();
        int xCapa = datosCapa.get("x").asInt();
        int yCapa = datosCapa.get("y").asInt();

        // Obtener los datos de los sprites de la capa
        JsonNode spritesNode = datosCapa.get("data");

        // Verificar si los datos de los sprites existen y son un array
        if (spritesNode != null && spritesNode.isArray()) {
            // Inicializar el arreglo para almacenar los sprites de la capa
            int[] spriteCapa = new int[spritesNode.size()];

            // Iterar sobre cada sprite en el array
            for (int j = 0; j < spritesNode.size(); j++) {
                // Obtener el código del sprite y restar 1 (debido a la indexación base 1)
                int codigoSprite = spritesNode.get(j).asInt();
                spriteCapa[j] = codigoSprite - 1;
            }

            // Agregar la capa de sprites inicializada a la lista de capas de sprites 1
            this.capaSprites1.add(new CapaSprites(anchoCapa, altoCapa, xCapa, yCapa, spriteCapa));
        }
        else {
            // Mostrar un mensaje de error si no se encontraron datos válidos en la capa de sprites 1
            System.err.println("No se encontraron datos válidos en la capa de sprites 1.");
        }
    }

// Método para inicializar la capa de sprites 2 a partir de los datos proporcionados
    private void inicializarCapaSprites2(JsonNode datosCapa) {
        // Obtener el ancho, alto, posición x e y de la capa
        int anchoCapa = datosCapa.get("width").asInt();
        int altoCapa = datosCapa.get("height").asInt();
        int xCapa = datosCapa.get("x").asInt();
        int yCapa = datosCapa.get("y").asInt();

        // Obtener los datos de los sprites de la capa
        JsonNode spritesNode = datosCapa.get("data");

        // Verificar si los datos de los sprites existen y son un array
        if (spritesNode != null && spritesNode.isArray()) {
            // Inicializar el arreglo para almacenar los sprites de la capa
            int[] spriteCapa = new int[spritesNode.size()];

            // Iterar sobre cada sprite en el array
            for (int j = 0; j < spritesNode.size(); j++) {
                // Obtener el código del sprite y restar 1 (debido a la indexación base 1)
                int codigoSprite = spritesNode.get(j).asInt();
                spriteCapa[j] = codigoSprite - 1;
            }

            // Agregar la capa de sprites inicializada a la lista de capas de sprites 2
            this.capaSprites2.add(new CapaSprites(anchoCapa, altoCapa, xCapa, yCapa, spriteCapa));
        }
        else {
            // Mostrar un mensaje de error si no se encontraron datos válidos en la capa de sprites 2
            System.err.println("No se encontraron datos válidos en la capa de sprites 2.");
        }
    }

// Método para inicializar la capa de colisiones a partir de los datos proporcionados
    private void inicializarCapaColisiones(JsonNode datosCapa) {
        // Obtener el ancho, alto, posición x e y de la capa
        int anchoCapa = getIntJson(datosCapa, "width");
        int altoCapa = getIntJson(datosCapa, "height");
        int xCapa = getIntJson(datosCapa, "x");
        int yCapa = getIntJson(datosCapa, "y");

        // Obtener los datos de los rectángulos de colisión de la capa
        JsonNode rectangulosNode = datosCapa.get("objects");

        // Verificar si los datos de los rectángulos existen y son un array
        if (rectangulosNode != null && rectangulosNode.isArray()) {
            // Inicializar el arreglo para almacenar los rectángulos de colisión de la capa
            Rectangle[] rectangulosCapa = new Rectangle[rectangulosNode.size()];

            // Iterar sobre cada rectángulo en el array
            for (int j = 0; j < rectangulosNode.size(); j++) {
                JsonNode datosRectangulo = rectangulosNode.get(j);

                // Obtener las coordenadas y dimensiones del rectángulo
                int x = getIntJson(datosRectangulo, "x");
                int y = getIntJson(datosRectangulo, "y");
                int ancho = getIntJson(datosRectangulo, "width");
                int alto = getIntJson(datosRectangulo, "height");

                // Ajustar las dimensiones mínimas a 1 si son 0
                if (x == 0) {
                    x = 1;
                }
                if (y == 0) {
                    y = 1;
                }
                if (ancho == 0) {
                    ancho = 1;
                }
                if (alto == 0) {
                    alto = 1;
                }

                // Crear el rectángulo de colisión y agregarlo al arreglo
                Rectangle rectangulo = new Rectangle(x, y, ancho, alto);
                rectangulosCapa[j] = rectangulo;
            }

            // Agregar la capa de colisiones inicializada a la lista de capas de colisiones
            this.capaColisiones.add(new CapaColisiones(anchoCapa, altoCapa, xCapa, yCapa, rectangulosCapa));
        }
        else {
            // Mostrar un mensaje de error si no se encontraron datos válidos en la capa de colisiones
            System.err.println("No se encontraron datos válidos en la capa de colisiones.");
        }
    }

    // Método para inicializar la capa de transparencia a partir de los datos proporcionados
    private void inicializarCapaTransparencia(JsonNode datosCapa) {
        // Obtener el ancho, alto, posición x e y de la capa de transparencia
        int anchoCapa = getIntJson(datosCapa, "width");
        int altoCapa = getIntJson(datosCapa, "height");
        int xCapa = getIntJson(datosCapa, "x");
        int yCapa = getIntJson(datosCapa, "y");

        // Obtener los datos de los rectángulos de la capa de transparencia
        JsonNode rectangulosNode = datosCapa.get("objects");

        // Verificar si los datos de los rectángulos existen y son un array
        if (rectangulosNode != null && rectangulosNode.isArray()) {
            // Inicializar el arreglo para almacenar los rectángulos de la capa de transparencia
            Rectangle[] rectangulosCapa = new Rectangle[rectangulosNode.size()];

            // Iterar sobre cada rectángulo en el array
            for (int j = 0; j < rectangulosNode.size(); j++) {
                JsonNode datosRectangulo = rectangulosNode.get(j);

                // Obtener las coordenadas y dimensiones del rectángulo
                int x = getIntJson(datosRectangulo, "x");
                int y = getIntJson(datosRectangulo, "y");
                int ancho = getIntJson(datosRectangulo, "width");
                int alto = getIntJson(datosRectangulo, "height");

                // Ajustar las dimensiones mínimas a 1 si son 0
                if (x == 0) {
                    x = 1;
                }
                if (y == 0) {
                    y = 1;
                }
                if (ancho == 0) {
                    ancho = 1;
                }
                if (alto == 0) {
                    alto = 1;
                }

                // Crear el rectángulo de transparencia y agregarlo al arreglo
                Rectangle rectangulo = new Rectangle(x, y, ancho, alto);
                rectangulosCapa[j] = rectangulo;
            }

            // Agregar la capa de transparencia inicializada a la lista de capas de transparencia
            this.capaTransparencias.add(new CapaTransparencias(anchoCapa, altoCapa, xCapa, yCapa, rectangulosCapa));
        }
        else {
            // Mostrar un mensaje de error si no se encontraron datos válidos en la capa de transparencia
            System.err.println("No se encontraron datos válidos en la capa de transparencia.");
        }
    }

// Método para actualizar los ataques del jugador
    private void actualizarAtaques() {
        // Verificar si no hay enemigos en el mapa o si el alcance del jugador está vacío
        if (enemigosMapa.isEmpty() || ElementosPrincipales.jugador.getAlcanceActual().isEmpty()) {
            return;
        }

        // Verificar si hay enemigos dentro del alcance del jugador
        boolean hayEnemigosEnAlcance = false;
        for (Enemigo enemigo : enemigosMapa) {
            if (ElementosPrincipales.jugador.getAlcanceActual().get(0).intersects(enemigo.getArea())) {
                hayEnemigosEnAlcance = true;
                break;
            }
        }

        // Si no hay enemigos en el alcance del jugador, salir del método
        if (!hayEnemigosEnAlcance) {
            return;
        }

        // Verificar si el jugador está atacando
        if (ElementosPrincipales.jugador.atacando) {
            // Inicializar una lista para almacenar los enemigos alcanzados por el ataque
            ArrayList<Enemigo> enemigosAlcanzados = new ArrayList<>();

            // Obtener el arma equipada por el jugador
            Arma arma = ElementosPrincipales.jugador.getAe().getArma1();

            // Inicializar el atributo de ataque del jugador
            int atributo = 0;

            // Calcular el atributo de ataque basado en el tipo de arma equipada por el jugador
            if (arma != null) {
                if (arma.getTipoObjeto() == TipoObjeto.ARCO) {
                    atributo = ElementosPrincipales.jugador.getGa().getDestreza();
                }
                else if (arma.getTipoObjeto() == TipoObjeto.ESPADA_LIGERA) {
                    atributo = (int) (ElementosPrincipales.jugador.getGa().getFuerza() / 2
                            + ElementosPrincipales.jugador.getGa().getDestreza() / 2);
                }
                else if (arma.getTipoObjeto() == TipoObjeto.ESPADA_MEDIA) {
                    atributo = (int) ElementosPrincipales.jugador.getGa().getFuerza();
                }
                else if (arma.getTipoObjeto() == TipoObjeto.ESPADA_PESADA) {
                    atributo = (int) (ElementosPrincipales.jugador.getGa().getFuerza()
                            + ElementosPrincipales.jugador.getGa().getDestreza() / 2);
                }

                // Realizar el ataque con el arma equipada y actualizar la lista de enemigos alcanzados
                arma.atacar(enemigosAlcanzados, atributo);
            }

            // Iterar sobre la lista de enemigos del mapa y eliminar los derrotados
            Iterator<Enemigo> iterador = enemigosMapa.iterator();
            while (iterador.hasNext()) {
                Enemigo enemigo = iterador.next();
                if (enemigo.getVidaActual() <= 0) {
                    iterador.remove();
                }
            }

            // Marcar el fin del ataque del jugador
            ElementosPrincipales.jugador.atacando = false;
        }
    }

    // Método para actualizar la recogida de objetos por parte del jugador
    private void actualizarRecogidaObjeto() {
        // Obtener el tiempo actual
        long tiempoActual = System.currentTimeMillis();

        // Verificar si ha pasado suficiente tiempo desde la última recogida
        if (tiempoActual - ultimoTiempoRecogida < tiempoDebouncing) {
            return; // Ignorar la recogida si está dentro del tiempo de debouncing
        }

        // Actualizar el tiempo de la última recogida
        ultimoTiempoRecogida = tiempoActual;

        // Obtener el área del jugador
        Rectangle areaJugador = ElementosPrincipales.jugador.getArea();

        // Iterar sobre los objetos en el mapa
        Iterator<ObjetoUnicoTiled> iterador = objetosMapa.iterator();
        while (iterador.hasNext()) {
            ObjetoUnicoTiled objetoActual = iterador.next();
            Rectangle posicionObjetoActual = new Rectangle(
                    objetoActual.getPosicion().x,
                    objetoActual.getPosicion().y,
                    Constantes.LADO_SPRITE,
                    Constantes.LADO_SPRITE);

            // Verificar si el jugador intersecta con el objeto y está recogiendo
            if (areaJugador.intersects(posicionObjetoActual) && GestorPrincipal.sd.getRaton().isRecogiendo()) {
                // Verificar si el jugador está sobrepeso
                if (ElementosPrincipales.jugador.isSobrepeso()) {
                    return;
                }
                // Recoger el objeto y eliminarlo de la lista de objetos en el mapa
                ElementosPrincipales.inventario.recogerObjetos(objetoActual);
                iterador.remove();
                break; // Salir del bucle después de recoger un objeto
            }
        }

        // Iterar sobre los contenedores de objetos en el mapa
        Iterator<ContenedorObjetos> iterador2 = listaContenedores.iterator();
        while (iterador2.hasNext()) {
            ContenedorObjetos contenedor = iterador2.next();
            // Verificar si el contenedor está vacío y eliminarlo si es así
            if (contenedor.getObjetos().isEmpty()) {
                iterador2.remove();
            }
            else if (areaJugador.intersects(contenedor.getArea()) && GestorPrincipal.sd.getRaton().isClick()) {
                // Verificar si el jugador intersecta con el contenedor y está haciendo clic
                abrirContenedor(contenedor);
            }
        }
    }

// Método para abrir un contenedor de objetos
    private void abrirContenedor(ContenedorObjetos contenedor) {
        contenedorAbierto = true;
        contenedorActual = contenedor;
    }

// Método para mostrar los elementos de un contenedor de objetos y permitir la recogida
    private void mostrarElementoscontenedor() {
        Rectangle posicionRaton = GestorPrincipal.sd.getRaton().getPosicionRectangle();

        // Verificar si el contenedor está abierto y actual
        if (contenedorAbierto && contenedorActual != null) {
            int puntoX = contenedorActual.getPosicion().x - ElementosPrincipales.jugador.getPosicionXInt() + Constantes.MARGEN_X;
            int puntoY = contenedorActual.getPosicion().y - ElementosPrincipales.jugador.getPosicionYInt() + Constantes.MARGEN_Y;
            Rectangle areaContenedor = new Rectangle(puntoX, puntoY, 32, 32);
            contenedorActual.setArea(areaContenedor);

            // Verificar si el ratón intersecta con el área del contenedor y está haciendo clic
            if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(areaContenedor))
                    && GestorPrincipal.sd.getRaton().isClick()) {
                recogerObjetosDelContenedor(contenedorActual);
            }
            else {
                // Cerrar el contenedor si el ratón no está sobre él
                contenedorActual = null;
                contenedorAbierto = false;
            }
        }
    }

    // Método para recoger objetos del contenedor y agregarlos al mapa como objetos únicos
    private void recogerObjetosDelContenedor(ContenedorObjetos contenedor) {
        // Obtener la posición actual del jugador
        int x = ElementosPrincipales.jugador.getPosicionXInt();
        int y = ElementosPrincipales.jugador.getPosicionYInt();

        // Recorrer todos los objetos en el contenedor y agregarlos al mapa como objetos únicos
        for (Objeto objetoActual : contenedor.getObjetos()) {
            ObjetoUnicoTiled objeto = new ObjetoUnicoTiled(new Point(x, y), objetoActual, objetoActual.getCantidad());
            objetosMapa.add(objeto);
        }

        // Limpiar la lista de objetos en el contenedor después de recogerlos todos
        contenedor.getObjetos().clear();
    }

// Método para actualizar el comportamiento de los enemigos en el mapa
    private void actualizarEnemigos() {
        // Verificar si hay enemigos en el mapa
        if (!enemigosMapa.isEmpty()) {
            // Iterar sobre todos los enemigos en el mapa
            for (Enemigo enemigo : enemigosMapa) {
                // Actualizar el siguiente nodo para el enemigo utilizando el algoritmo de Dijkstra
                enemigo.setSiguienteNodo(dijkstra.enconcontrarSiguienteNodoParaEnemigo(enemigo));
                // Actualizar el enemigo en función de su comportamiento y la posición de otros enemigos
                enemigo.actualizar(enemigosMapa);
            }
        }
    }

    private JsonNode getObjetoJson(final String codigoJson) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode objetoJson = mapper.readTree(codigoJson);
            return objetoJson;
        }
        catch (IOException e) {
            System.err.println("Error al analizar el JSON: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private int getIntJson(JsonNode objSon, String clave) {
        JsonNode valorNode = objSon.get(clave);

        int valor = 0;
        if (valorNode != null) {
            if (valorNode.isNumber()) {
                valor = valorNode.intValue(); // Convertir el valor del nodo a un entero
            }
            else if (valorNode.isTextual()) {
                // Intenta convertir el valor del nodo a un entero
                try {
                    valor = Integer.parseInt(valorNode.asText());
                }
                catch (NumberFormatException e) {
                    System.err.println("No se pudo convertir el valor del nodo a un entero: " + e.getMessage());
                }
            }
        }
        return valor;
    }

    public static JsonNode getNodeFromJsonObject(JsonNode jsonObject, String key) {
        // Verificar si el JSON contiene la clave
        if (jsonObject.has(key)) {
            return jsonObject.get(key);
        }
        else {
            // Manejar el caso en el que la clave no está presente en el JSON
            System.out.println("La clave '" + key + "' no está presente en el JSON.");
            // Devolver null u otro valor predeterminado según tus necesidades
            return null;
        }
    }

    // Método para actualizar las zonas de salida en relación con la posición del jugador
    public void actualizarZonaSalida() {
        for (Rectangle zonaSalida : zonasSalida) {
            // Calcula las coordenadas relativas de la zona de salida en relación con el jugador
            int puntoX = zonaSalida.x - ElementosPrincipales.jugador.getPosicionXInt() + Constantes.MARGEN_X;
            int puntoY = zonaSalida.y - ElementosPrincipales.jugador.getPosicionYInt() + Constantes.MARGEN_Y;

            // Actualiza la zona de salida con las nuevas coordenadas
            zonaSalida = new Rectangle(puntoX - 18, puntoY, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);
        }
    }

// Método para actualizar las áreas de las tiendas en relación con la posición del jugador
    private void actualizarTiendas() {
        for (Tienda tiendaActual : tiendas) {
            // Calcula las coordenadas relativas de la tienda en relación con el jugador
            int puntoX = tiendaActual.getPosicion().x - ElementosPrincipales.jugador.getPosicionXInt() + Constantes.MARGEN_X;
            int puntoY = tiendaActual.getPosicion().y - ElementosPrincipales.jugador.getPosicionYInt() + Constantes.MARGEN_Y;

            // Crea un nuevo rectángulo que representa el área de la tienda con las nuevas coordenadas
            Rectangle nuevaAreaTienda = new Rectangle(puntoX - 18, puntoY, 16, 16);
            tiendaActual.setAreaTienda(nuevaAreaTienda);

            // Verifica si el jugador ha hecho clic en la tienda para interactuar con ella
            if (ElementosPrincipales.jugador.getLIMITE_ABAJO().intersects(tiendaActual.getAreaTienda())
                    && GestorPrincipal.sd.getRaton().isClick2()) {
                // Establece la tienda activa y realiza las acciones correspondientes
                tiendaActiva = tiendaActual;
                System.out.println("Tipo: " + tiendaActiva.getTipo());
                obtenerObjetosMapa(tiendas.get(0).getIdTienda());
                objetosTiendaActual = verificarTipoTienda(tiendaActiva);
                GestorControles.teclado.tiendaActiva = true;
            }
        }
    }

// Método para actualizar las áreas de colisión en relación con la posición del jugador
    private void actualizarAreasColision() {
        // Borra las áreas de colisión previamente actualizadas si las hay
        if (!areasColisionActualizadas.isEmpty()) {
            areasColisionActualizadas.clear();
        }

        // Itera sobre todas las áreas de colisión originales y las actualiza en función de la posición del jugador
        for (int i = 0; i < areaColisionOriginales.size(); i++) {
            Rectangle rInicial = areaColisionOriginales.get(i);
            int puntoX = rInicial.x - ElementosPrincipales.jugador.getPosicionXInt() + Constantes.MARGEN_X;
            int puntoY = rInicial.y - ElementosPrincipales.jugador.getPosicionYInt() + Constantes.MARGEN_Y;
            final Rectangle rFinal = new Rectangle(puntoX, puntoY, rInicial.width, rInicial.height);
            areasColisionActualizadas.add(rFinal);
        }
    }

// Método para actualizar las áreas de transparencia en relación con la posición del jugador
    private void actualizarAreasTransparencia() {
        // Borra las áreas de transparencia previamente actualizadas si las hay
        if (!areasTransparenciaActualizadas.isEmpty()) {
            areasTransparenciaActualizadas.clear();
        }

        // Itera sobre todas las áreas de transparencia originales y las actualiza en función de la posición del jugador
        for (int i = 0; i < areaTransparenciaOriginales.size(); i++) {
            Rectangle rInicial = areaTransparenciaOriginales.get(i);
            int puntoX = rInicial.x - ElementosPrincipales.jugador.getPosicionXInt() + Constantes.MARGEN_X;
            int puntoY = rInicial.y - ElementosPrincipales.jugador.getPosicionYInt() + Constantes.MARGEN_Y;
            final Rectangle rFinal = new Rectangle(puntoX, puntoY, rInicial.width, rInicial.height);
            areasTransparenciaActualizadas.add(rFinal);
        }
    }

// Método para obtener los límites del área del mapa en relación con la posición del jugador
    public Rectangle getBordes(final int posicionX, final int posicionY) {
        int x = Constantes.MARGEN_X - posicionX + ElementosPrincipales.jugador.getANCHO_JUGADOR();
        int y = Constantes.MARGEN_Y - posicionY + ElementosPrincipales.jugador.getALTO_JUGADOR();
        int ancho = this.anchoMapaTiles * Constantes.LADO_SPRITE - ElementosPrincipales.jugador.getANCHO_JUGADOR() * 2;
        int alto = this.altoMapaTiles * Constantes.LADO_SPRITE - ElementosPrincipales.jugador.getALTO_JUGADOR() * 2;
        return new Rectangle(x, y, ancho, alto);
    }

    // Método para dibujar tooltips de objetos en el mapa cuando el ratón se superpone a ellos
    private void dibujarTooltipObjetosMapa(final Graphics g, final SuperficieDibujo sd) {
        Rectangle posicionRaton = sd.getRaton().getPosicionRectangle();

        // Itera sobre los objetos en el mapa y verifica si el ratón está sobre ellos para mostrar el tooltip
        for (ObjetoUnicoTiled objeto : objetosMapa) {
            int puntoX = (int) objeto.getPosicion().x - ElementosPrincipales.jugador.getPosicionXInt() + Constantes.MARGEN_X;
            int puntoY = (int) objeto.getPosicion().y - ElementosPrincipales.jugador.getPosicionYInt() + Constantes.MARGEN_Y;
            Rectangle nuevaArea = new Rectangle(puntoX, puntoY, 32, 32);

            if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(nuevaArea))) {
                // Si el ratón está sobre el objeto, dibuja un rectángulo contorno y muestra el tooltip del objeto
                DibujoDebug.dibujarRectanguloContorno(g, nuevaArea, Color.DARK_GRAY);
                dibujarTooltipObjeto(g, sd, objeto.getObjeto());
            }
        }
    }

// Método para dibujar el tooltip de un objeto en el mapa
    private void dibujarTooltipObjeto(Graphics g, SuperficieDibujo sd, Object objeto) {
        // Dibuja el tooltip personalizado según el tipo de objeto
        if (objeto instanceof Consumible) {
            Consumible consumible = (Consumible) objeto;
            GeneradorTooltip.dibujarTooltipMejorado(g, sd, consumible.getNombre() + "\nPESO: " + consumible.getPeso() + " oz.");
        }
        else if (objeto instanceof Arma) {
            Arma arma = (Arma) objeto;
            GeneradorTooltip.dibujarTooltipMejorado(g, sd, arma.getNombre() + "\nPESO: " + arma.getPeso() + " oz.");
        }
        else if (objeto instanceof Armadura) {
            Armadura armadura = (Armadura) objeto;
            GeneradorTooltip.dibujarTooltipMejorado(g, sd, armadura.getNombre() + "\nPESO: " + armadura.getPeso() + " oz.");
        }
        else if (objeto instanceof Joya) {
            Joya joya = (Joya) objeto;
            GeneradorTooltip.dibujarTooltipMejorado(g, sd, joya.getNombre() + "\nPESO: " + joya.getPeso() + " oz.");
        }
        else if (objeto instanceof Claves) {
            Claves claves = (Claves) objeto;
            GeneradorTooltip.dibujarTooltipMejorado(g, sd, claves.getNombre() + "\nPESO: " + claves.getPeso() + " oz.");
        }
    }

// Método para obtener los objetos de una tienda en el mapa
    private void obtenerObjetosMapa(int idMapa) {
        objetosTiendaMapa.clear();
        ArrayList<String> listaObjetos = RegistroTiendas.obtenerTienda(idMapa);
        // Itera sobre los IDs de los objetos en la tienda y los añade a la lista de objetos de la tienda en el mapa
        for (String idObjeto : listaObjetos) {
            Objeto objeto = RegistroObjetos.obtenerObjeto(Integer.parseInt(idObjeto));
            objetosTiendaMapa.add(objeto);
        }
    }

// Método para filtrar los objetos de la tienda actual según su tipo
    private ArrayList<Objeto> verificarTipoTienda(Tienda tienda) {
        objetosTiendaActual.clear();

        // Filtra los objetos de la tienda actual según el tipo de tienda
        switch (tienda.getTipo()) {
            case 1:
                for (Objeto objetoMapa : objetosTiendaMapa) {
                    if (objetoMapa instanceof Armadura) {
                        objetosTiendaActual.add(objetoMapa);
                    }
                }
                break;
            case 2:
                for (Objeto objetoMapa : objetosTiendaMapa) {
                    if (objetoMapa instanceof Arma) {
                        objetosTiendaActual.add(objetoMapa);
                    }
                }
                break;
            case 3:
                for (Objeto objetoMapa : objetosTiendaMapa) {
                    if (objetoMapa instanceof Accesorio) {
                        objetosTiendaActual.add(objetoMapa);
                    }
                }
                break;
            case 4:
                for (Objeto objetoMapa : objetosTiendaMapa) {
                    if (objetoMapa instanceof Consumible) {
                        objetosTiendaActual.add(objetoMapa);
                    }
                }
                break;
        }
        return objetosTiendaActual;
    }

    // Método para obtener el punto inicial del mapa
    public Point getPuntoInicial() {
        return puntoInicial;
    }

    // Método para obtener el nombre del siguiente mapa
    public String getSiguienteMapa() {
        return siguienteMapa;
    }

    // Método para establecer el nombre del siguiente mapa
    public void setSiguienteMapa(String siguienteMapa) {
        this.siguienteMapa = siguienteMapa;
    }

    // Método para establecer el punto inicial del mapa
    public void setPuntoInicial(Point puntoInicial) {
        this.puntoInicial = puntoInicial;
    }

    // Método para obtener la lista de enemigos del mapa
    public ArrayList<Enemigo> getEnemigosMapa() {
        return enemigosMapa;
    }

    // Método para obtener la lista de nodos del mapa utilizada por el algoritmo Dijkstra
    public ArrayList<Nodo> getNodosMapa() {
        return dijkstra.getNodosMapa();
    }

}
