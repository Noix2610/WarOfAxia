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

    private int anchoMapaTiles;
    private int altoMapaTiles;
    private String siguienteMapa;
    private Point puntoInicial;
    public Rectangle recMapa;
    public Tienda tiendaActiva;

    long ultimoTiempoRecogida = 0;
    long tiempoDebouncing = 50; // 50 milisegundos de tiempo de debouncing

    public Rectangle zonaSalida1 = new Rectangle();
    public Rectangle zonaSalida2 = new Rectangle();
    public Rectangle zonaSalida3 = new Rectangle();
    public Rectangle zonaSalida4 = new Rectangle();
    public Rectangle zonaSalida5 = new Rectangle();
    public Rectangle zonaSalida6 = new Rectangle();
    public Rectangle zonaSalida7 = new Rectangle();
    public Rectangle zonaSalida8 = new Rectangle();
    public ArrayList<Rectangle> zonasSalida = new ArrayList<>();

    private ArrayList<CapaSprites> capaSprites1;
    private ArrayList<CapaSprites> capaSprites2;
    private ArrayList<CapaSprites> capasprites3;
    private ArrayList<CapaColisiones> capaColisiones;
    private ArrayList<CapaTransparencias> capaTransparencias;
    private ArrayList<Rectangle> areaTransparenciaOriginales;
    private ArrayList<Rectangle> areaColisionOriginales;
    private Sprite[] paletaSprites1;
    private Sprite[] paletaSprites2;
    public final ArrayList<Objeto> objetosTiendaMapa;
    public ArrayList<Objeto> objetosTiendaActual;
    private boolean contenedorAbierto = false;

    private Dijkstra dijkstra;

    private ArrayList<ObjetoUnicoTiled> objetosMapa;
    public ArrayList<Objeto> objetosTienda;
    private ArrayList<Enemigo> enemigosMapa;
    public ArrayList<Rectangle> areasColisionActualizadas;
    public ArrayList<Rectangle> areasTransparenciaActualizadas;
    public ArrayList<ContenedorObjetos> listaContenedores;
    private ContenedorObjetos contenedorActual;
    public ArrayList<Tienda> tiendas;

    public MapaTiled(final String ruta) {
        Salida.getSalidas().clear();

        String contenido = CargadorRecursos.leerArchivoTexto(ruta);

        JsonNode globalJSON = getObjetoJson(contenido);

        obtenerInformacionSiguienteMapa(globalJSON);
        // Inicializar atributos básicos
        inicializarAtributosBasicos(globalJSON);
        // Inicializar capas
        inicializarCapas(globalJSON);
        // Combinar colisiones en un solo ArrayList
        combinarColisiones();
        // Combinar transparencias en un solo ArrayList
        combinarTransparencias();
        // Inicializar Dijkstra
        inicializarDijkstra();
        // Inicializar paleta de sprites
        inicializarPaletaSprites(globalJSON);

        // Obtener objetos del mapa
        obtenerObjetosMapa(globalJSON);
        // Obtener enemigos del mapa
        obtenerEnemigosMapa(globalJSON);

        obtenerContenedoresMapa(globalJSON);

        obtenerTiendas(globalJSON);
        areasColisionActualizadas = new ArrayList<>();
        areasTransparenciaActualizadas = new ArrayList<>();
        objetosTiendaMapa = new ArrayList<>();
        objetosTiendaActual = new ArrayList<>();
        tiendaActiva = new Tienda();

    }

    public void actualizar() {
        actualizarEnemigos();
        actualizarAreasColision();
        actualizarAreasTransparencia();
        actualizarRecogidaObjeto();
        actualizarAtaques();
        actualizarZonaSalida();
        actualizarTiendas();

        Point punto = new Point(ElementosPrincipales.jugador.getPosicionXInt(),
                ElementosPrincipales.jugador.getPosicionYInt());

        Point puntoCoincidente = dijkstra.getCoordenadasNodoCoincidente(punto);
        dijkstra.reiniciarYEvaluar(puntoCoincidente);
        mostrarElementoscontenedor();

    }

    public void dibujar(Graphics2D g) {

        // Dibujar sprites del mapa
        int intentosDibujo = 0;
        for (int i = 0; i < capaSprites1.size(); i++) {
            int[] spritesCapa = capaSprites1.get(i).getSprites();
            for (int y = 0; y < altoMapaTiles; y++) {
                for (int x = 0; x < anchoMapaTiles; x++) {
                    long idSpriteActual = spritesCapa[x + y * anchoMapaTiles];
                    if (idSpriteActual != -1) {
                        int puntoX = x * Constantes.LADO_SPRITE
                                - ElementosPrincipales.jugador.getPosicionXInt() + Constantes.MARGEN_X;
                        int puntoY = y * Constantes.LADO_SPRITE
                                - ElementosPrincipales.jugador.getPosicionYInt() + Constantes.MARGEN_Y;

                        // OPTIMIZACION DIBUJADO
                        if (puntoX < 0 - Constantes.LADO_SPRITE || puntoX > Constantes.ANCHO_JUEGO
                                || puntoY < 0 - Constantes.LADO_SPRITE || puntoY > Constantes.ANCHO_JUEGO - 65) {
                            continue;
                        }

                        intentosDibujo++;
                        DibujoDebug.dibujarImagen(g, paletaSprites1[(int) idSpriteActual].getImagen(), puntoX, puntoY);
                    }
                }
            }
        }

        for (int i = 0; i < objetosMapa.size(); i++) {
            ObjetoUnicoTiled objetoActual = objetosMapa.get(i);
            int puntoX = objetoActual.getPosicion().x - ElementosPrincipales.jugador.getPosicionXInt() + Constantes.MARGEN_X;
            int puntoY = objetoActual.getPosicion().y - ElementosPrincipales.jugador.getPosicionYInt() + Constantes.MARGEN_Y;
            DibujoDebug.dibujarImagen(g, objetoActual.getObjeto().getSprite().getImagen(), puntoX, puntoY);
        }

        for (int i = 0; i < listaContenedores.size(); i++) {
            ContenedorObjetos contenedorAct = listaContenedores.get(i);
            int puntoX = (int) contenedorAct.getPosicion().x - ElementosPrincipales.jugador.getPosicionXInt() + Constantes.MARGEN_X;
            int puntoY = (int) contenedorAct.getPosicion().y - ElementosPrincipales.jugador.getPosicionYInt() + Constantes.MARGEN_Y;

            contenedorAct.dibujar(g, puntoX, puntoY);
        }

        for (int i = 0; i < enemigosMapa.size(); i++) {
            Enemigo enemigo = enemigosMapa.get(i);
            int puntoX = (int) enemigo.getPosicionX() - ElementosPrincipales.jugador.getPosicionXInt() + Constantes.MARGEN_X;
            int puntoY = (int) enemigo.getPosicionY() - ElementosPrincipales.jugador.getPosicionYInt() + Constantes.MARGEN_Y;
            enemigo.dibujar(g, puntoX, puntoY);

        }

    }

    public void dibujar2daCapa(Graphics2D g) {

        // Dibujar sprites del mapa
        int intentosDibujo = 0;
        for (int i = 0; i < capaSprites2.size(); i++) {
            int[] spritesCapa = capaSprites2.get(i).getSprites();
            for (int y = 0; y < altoMapaTiles; y++) {
                for (int x = 0; x < anchoMapaTiles; x++) {
                    long idSpriteActual = spritesCapa[x + y * anchoMapaTiles];
                    if (idSpriteActual != -1) {
                        int puntoX = x * Constantes.LADO_SPRITE
                                - ElementosPrincipales.jugador.getPosicionXInt() + Constantes.MARGEN_X;
                        int puntoY = y * Constantes.LADO_SPRITE
                                - ElementosPrincipales.jugador.getPosicionYInt() + Constantes.MARGEN_Y;

                        // OPTIMIZACION DIBUJADO
                        if (puntoX < 0 - Constantes.LADO_SPRITE || puntoX > Constantes.ANCHO_JUEGO
                                || puntoY < 0 - Constantes.LADO_SPRITE || puntoY > Constantes.ANCHO_JUEGO - 65) {
                            continue;
                        }

                        intentosDibujo++;
                        DibujoDebug.dibujarImagen(g, paletaSprites2[(int) idSpriteActual].getImagen(), puntoX, puntoY);
                    }
                }
            }
        }

        DibujoDebug.dibujarRectanguloContorno(g, zonaSalida1, Color.red);
        DibujoDebug.dibujarRectanguloContorno(g, zonaSalida2, Color.red);
        DibujoDebug.dibujarRectanguloContorno(g, zonaSalida3, Color.red);
        DibujoDebug.dibujarRectanguloContorno(g, zonaSalida4, Color.red);
        DibujoDebug.dibujarRectanguloContorno(g, zonaSalida5, Color.red);
        DibujoDebug.dibujarRectanguloContorno(g, zonaSalida6, Color.red);
        DibujoDebug.dibujarRectanguloContorno(g, zonaSalida7, Color.red);

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
        anchoMapaTiles = globalJSON.get("width").asInt();
        altoMapaTiles = globalJSON.get("height").asInt();

        // Obtener el objeto JSON asociado con "start"
        JsonNode puntoInicialJSON = globalJSON.get("start");

        // Verificar si el nodo "start" existe en el JSON
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
        JsonNode capas = globalJSON.get("layers");
        this.capaSprites1 = new ArrayList<>();
        this.capaSprites2 = new ArrayList<>();
        this.capaColisiones = new ArrayList<>();
        this.capaTransparencias = new ArrayList<>();

        if (capas != null && capas.isArray()) {
            for (JsonNode capaNode : capas) {
                String tipo = capaNode.get("id").asText();

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

            for (JsonNode capaNode : capas) {
                String tipo = capaNode.get("type").asText();

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
            System.err.println("La clave 'layers' no está presente o no es un array en el JSON.");
        }
    }

    private void combinarColisiones() {
        // Lógica para combinar colisiones en un solo ArrayList
        areaColisionOriginales = new ArrayList<>();

        for (int i = 0; i < capaColisiones.size(); i++) {
            Rectangle[] rectangulos = capaColisiones.get(i).getColisionables();

            for (int j = 0; j < rectangulos.length; j++) {
                areaColisionOriginales.add(rectangulos[j]);
            }
        }
    }

    private void combinarTransparencias() {
        // Lógica para combinar colisiones en un solo ArrayList
        areaTransparenciaOriginales = new ArrayList<>();

        for (int i = 0; i < capaTransparencias.size(); i++) {
            Rectangle[] rectangulos = capaTransparencias.get(i).getColisionables();

            for (int j = 0; j < rectangulos.length; j++) {
                areaTransparenciaOriginales.add(rectangulos[j]);
            }
        }
    }

    private void inicializarDijkstra() {
        dijkstra = new Dijkstra(new Point(10, 10), anchoMapaTiles, altoMapaTiles, areaColisionOriginales);
    }

    private void inicializarPaletaSprites(JsonNode globalJSON) {
        // Lógica para inicializar la paleta de sprites
        JsonNode coleccionSprites = globalJSON.get("tilesets");
        if (coleccionSprites != null && coleccionSprites.isArray()) {
            int totalSprites = 0;
            for (JsonNode datosGrupo : coleccionSprites) {
                totalSprites += datosGrupo.get("tilecount").asInt();
            }

            paletaSprites1 = new Sprite[totalSprites];
            paletaSprites2 = new Sprite[totalSprites];

            int spriteIndex = 0;
            for (JsonNode datosGrupo : coleccionSprites) {
                String nombreImagen = datosGrupo.get("image").asText();
                int anchoTile = datosGrupo.get("tilewidth").asInt();
                int altoTile = datosGrupo.get("tileheight").asInt();

                HojaSprites hoja = new HojaSprites("/mapas/" + nombreImagen, anchoTile, altoTile, false);

                int primerSpriteColeccion = datosGrupo.get("firstgid").asInt() - 1;
                int ultimoSpriteColeccion = primerSpriteColeccion + datosGrupo.get("tilecount").asInt() - 1;

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

                spriteIndex += sprites.length;
            }
        }
        else {
            System.err.println("La clave 'tilesets' no está presente o no es un array en el JSON.");
        }
    }

    private void obtenerObjetosMapa(JsonNode globalJSON) {
        objetosMapa = new ArrayList<>();
        JsonNode coleccionObjetos = globalJSON.get("objetos");

        if (coleccionObjetos != null && coleccionObjetos.isArray()) {
            for (JsonNode objetoNode : coleccionObjetos) {
                int idObjeto = objetoNode.get("id").asInt();
                int cantidad = objetoNode.get("cantidad").asInt();
                int xObjeto = objetoNode.get("x").asInt();
                int yObjeto = objetoNode.get("y").asInt();

                Point posicionObjeto = new Point(xObjeto, yObjeto);
                Objeto objeto = RegistroObjetos.obtenerObjeto(idObjeto);
                objeto.setCantidad(cantidad);

                ObjetoUnicoTiled objetoUnico = new ObjetoUnicoTiled(posicionObjeto, objeto, objeto.getCantidad());
                objetosMapa.add(objetoUnico);
            }
        }
        else {
            System.err.println("La clave 'objetos' no está presente o no es un array en el JSON.");
        }
    }

    private void obtenerEnemigosMapa(JsonNode globalJSON) {
        enemigosMapa = new ArrayList<>();
        JsonNode coleccionEnemigos = globalJSON.get("enemigos");

        if (coleccionEnemigos != null && coleccionEnemigos.isArray()) {
            for (JsonNode enemigoNode : coleccionEnemigos) {
                JsonNode idNode = enemigoNode.get("id");
                JsonNode xNode = enemigoNode.get("x");
                JsonNode yNode = enemigoNode.get("y");

                // Verificar si los nodos no son nulos y son valores numéricos
                if (idNode != null && xNode != null && yNode != null) {
                    int idEnemigo = getIntJson(enemigoNode, "id");
                    int xEnemigo = getIntJson(enemigoNode, "x");
                    int yEnemigo = getIntJson(enemigoNode, "y");

                    if (idEnemigo != 0) {
                        Point posicionEnemigo = new Point(xEnemigo, yEnemigo);
                        Enemigo enemigo = RegistroEnemigos.obtenerEnemigo(idEnemigo);
                        enemigo.setPosicion(posicionEnemigo.x, posicionEnemigo.y);
                        enemigosMapa.add(enemigo);
                    }
                }
                else {
                    System.err.println("Uno de los nodos de enemigos es nulo.");
                }
            }
        }
        else {
            System.err.println("La clave 'enemigos' no está presente o no es un array en el JSON.");
        }
    }

    private void obtenerContenedoresMapa(JsonNode globalJSON) {
        listaContenedores = new ArrayList<>();
        JsonNode coleccionContenedores = globalJSON.get("contenedores");

        if (coleccionContenedores != null && coleccionContenedores.isArray()) {
            for (JsonNode contenedorNode : coleccionContenedores) {
                int idContenedor = contenedorNode.get("idContenedor").asInt();
                int xContenedor = contenedorNode.get("x").asInt();
                int yContenedor = contenedorNode.get("y").asInt();

                Point posicionContenedor = new Point(xContenedor, yContenedor);
                Rectangle areacontenedor = new Rectangle(xContenedor, yContenedor, 32, 32);
                ContenedorObjetos contenedor = new ContenedorObjetos(posicionContenedor, idContenedor, areacontenedor);

                JsonNode coleccionObjetos = contenedorNode.get("objetos");
                if (coleccionObjetos != null && coleccionObjetos.isArray()) {
                    for (JsonNode objetoNode : coleccionObjetos) {
                        int idObjeto = objetoNode.get("idObjeto").asInt();
                        int cantidadObjeto = objetoNode.get("cantidad").asInt();

                        Objeto objeto = RegistroObjetos.obtenerObjeto(idObjeto);
                        objeto.setCantidad(cantidadObjeto);

                        contenedor.getObjetos().add(objeto);
                    }
                }

                listaContenedores.add(contenedor);
                areaColisionOriginales.add(contenedor.getArea());
            }
        }
        else {
            System.err.println("La clave 'contenedores' no está presente o no es un array en el JSON.");
        }
    }

    private void obtenerTiendas(JsonNode globalJSON) {
        tiendas = new ArrayList<>();

        JsonNode coleccionTiendas = globalJSON.get("tiendas");
        if (coleccionTiendas != null && coleccionTiendas.isArray()) {
            for (JsonNode tiendaNode : coleccionTiendas) {
                int idTienda = getIntJson(tiendaNode, "id");
                System.out.println("idTienda: " + idTienda);
                int xTienda = getIntJson(tiendaNode, "x");
                int yTienda = getIntJson(tiendaNode, "y");
                int tipo = getIntJson(tiendaNode, "tienda");

                Point posTienda = new Point(xTienda, yTienda);
                Tienda tienda = new Tienda(idTienda, posTienda, tipo);
                tiendas.add(tienda);
            }
        }
        else {
            System.err.println("La clave 'tiendas' no está presente o no es un array en el JSON.");
        }
    }

    private void obtenerInformacionSiguienteMapa(JsonNode globalJSON) {
        JsonNode salidasJSON = globalJSON.get("salidas");

        if (salidasJSON != null && !salidasJSON.isEmpty()) {
            for (JsonNode salidaJSON : salidasJSON) {
                // Si no existe, agregar la nueva salida
                int xSalidaMapa = salidaJSON.get("x").asInt();
                int ySalidaMapa = salidaJSON.get("y").asInt();
                Point puntoSalidaMapa = new Point(xSalidaMapa, ySalidaMapa);

                String siguienteMapa = salidaJSON.get("mapaDestino").asText();

                // Obtener las coordenadas de inicio en el siguiente mapa desde salidaJSON
                JsonNode puntoInicialJSON = salidaJSON.get("punto inicial");
                int xInicioSiguienteMapa = puntoInicialJSON.get("x").asInt();
                int yInicioSiguienteMapa = puntoInicialJSON.get("y").asInt();
                Point puntoInicioSiguienteMapa = new Point(xInicioSiguienteMapa, yInicioSiguienteMapa);

                Salida nuevaSalida = new Salida(puntoInicioSiguienteMapa, puntoSalidaMapa, siguienteMapa);
                Rectangle nuevaZonaSalida = new Rectangle(xSalidaMapa, ySalidaMapa, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);
                Salida.getSalidas().add(nuevaSalida);
                zonasSalida.add(nuevaZonaSalida);
            }
        }
        else {
            System.err.println("La clave 'salidas' no está presente o está vacía en el JSON.");
        }
    }

    private void inicializarCapaSprites1(JsonNode datosCapa) {
        int anchoCapa = datosCapa.get("width").asInt();
        int altoCapa = datosCapa.get("height").asInt();
        int xCapa = datosCapa.get("x").asInt();
        int yCapa = datosCapa.get("y").asInt();

        JsonNode spritesNode = datosCapa.get("data");
        if (spritesNode != null && spritesNode.isArray()) {
            int[] spriteCapa = new int[spritesNode.size()];
            for (int j = 0; j < spritesNode.size(); j++) {
                int codigoSprite = spritesNode.get(j).asInt();
                spriteCapa[j] = codigoSprite - 1;
            }

            this.capaSprites1.add(new CapaSprites(anchoCapa, altoCapa, xCapa, yCapa, spriteCapa));
        }
        else {
            System.err.println("No se encontraron datos válidos en la capa de sprites 1.");
        }
    }

    private void inicializarCapaSprites2(JsonNode datosCapa) {
        int anchoCapa = datosCapa.get("width").asInt();
        int altoCapa = datosCapa.get("height").asInt();
        int xCapa = datosCapa.get("x").asInt();
        int yCapa = datosCapa.get("y").asInt();

        JsonNode spritesNode = datosCapa.get("data");
        if (spritesNode != null && spritesNode.isArray()) {
            int[] spriteCapa = new int[spritesNode.size()];
            for (int j = 0; j < spritesNode.size(); j++) {
                int codigoSprite = spritesNode.get(j).asInt();
                spriteCapa[j] = codigoSprite - 1;
            }

            this.capaSprites2.add(new CapaSprites(anchoCapa, altoCapa, xCapa, yCapa, spriteCapa));
        }
        else {
            System.err.println("No se encontraron datos válidos en la capa de sprites 2.");
        }
    }

    private void inicializarCapaColisiones(JsonNode datosCapa) {
        int anchoCapa = getIntJson(datosCapa, "width");
        int altoCapa = getIntJson(datosCapa, "height");
        int xCapa = getIntJson(datosCapa, "x");
        int yCapa = getIntJson(datosCapa, "y");

        JsonNode rectangulosNode = datosCapa.get("objects");
        if (rectangulosNode != null && rectangulosNode.isArray()) {
            Rectangle[] rectangulosCapa = new Rectangle[rectangulosNode.size()];

            for (int j = 0; j < rectangulosNode.size(); j++) {
                JsonNode datosRectangulo = rectangulosNode.get(j);

                int x = getIntJson(datosRectangulo, "x");
                int y = getIntJson(datosRectangulo, "y");
                int ancho = getIntJson(datosRectangulo, "width");
                int alto = getIntJson(datosRectangulo, "height");

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

                Rectangle rectangulo = new Rectangle(x, y, ancho, alto);
                rectangulosCapa[j] = rectangulo;
            }

            this.capaColisiones.add(new CapaColisiones(anchoCapa, altoCapa, xCapa, yCapa, rectangulosCapa));
        }
        else {
            System.err.println("No se encontraron datos válidos en la capa de colisiones.");
        }
    }

    private void inicializarCapaTransparencia(JsonNode datosCapa) {
        int anchoCapa = getIntJson(datosCapa, "width");
        int altoCapa = getIntJson(datosCapa, "height");
        int xCapa = getIntJson(datosCapa, "x");
        int yCapa = getIntJson(datosCapa, "y");

        JsonNode rectangulosNode = datosCapa.get("objects");
        if (rectangulosNode != null && rectangulosNode.isArray()) {
            Rectangle[] rectangulosCapa = new Rectangle[rectangulosNode.size()];

            for (int j = 0; j < rectangulosNode.size(); j++) {
                JsonNode datosRectangulo = rectangulosNode.get(j);

                int x = getIntJson(datosRectangulo, "x");
                int y = getIntJson(datosRectangulo, "y");
                int ancho = getIntJson(datosRectangulo, "width");
                int alto = getIntJson(datosRectangulo, "height");

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

                Rectangle rectangulo = new Rectangle(x, y, ancho, alto);
                rectangulosCapa[j] = rectangulo;
            }

            this.capaTransparencias.add(new CapaTransparencias(anchoCapa, altoCapa, xCapa, yCapa, rectangulosCapa));
        }
        else {
            System.err.println("No se encontraron datos válidos en la capa de transparencia.");
        }
    }

    private void actualizarAtaques() {
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

        if (ElementosPrincipales.jugador.atacando) {
            ArrayList<Enemigo> enemigosAlcanzados = new ArrayList<>();
            if (ElementosPrincipales.jugador.getAe().getArma1() != null && ElementosPrincipales.jugador.getAe().getArma1().isPenetrante()) {
                for (Enemigo enemigo : enemigosMapa) {
                    if (ElementosPrincipales.jugador.getAlcanceActual().get(0).intersects(enemigo.getArea())) {
                        enemigosAlcanzados.add(enemigo);
                    }
                }
            }
            else {
                /*
                Este fragmento de código se encarga de realizar el proceso de ataque del jugador.
                Primero, encuentra el enemigo más cercano dentro del alcance del jugador,
                luego calcula el atributo de ataque del jugador y realiza el ataque con el arma equipada.
                Finalmente, elimina los enemigos derrotados y marca el fin del ataque.
                 */
                Enemigo enemigoCercano = null;
                Double distanciaCercana = null;

                for (Enemigo enemigo : enemigosMapa) {
                    if (ElementosPrincipales.jugador.getAlcanceActual().get(0)
                            .intersects(enemigo.getArea())) {
                        Point puntoJugador = new Point(ElementosPrincipales.jugador.getPosicionXInt() / 32,
                                ElementosPrincipales.jugador.getPosicionYInt() / 32);

                        Point puntoEnemigo = new Point(
                                (int) enemigo.getPosicionX() / 32,
                                (int) enemigo.getPosicionY());
                        Double distanciaActual = CalculadoraDistancia.getDistanciaEntrePuntos(puntoJugador, puntoEnemigo);

                        if (enemigoCercano == null) {
                            enemigoCercano = enemigo;
                            distanciaCercana = distanciaActual;
                        }
                        else if (distanciaActual > distanciaCercana) {
                            enemigoCercano = enemigo;
                            distanciaCercana = distanciaActual;
                        }
                    }
                }
                enemigosAlcanzados.add(enemigoCercano);
            }
            Arma arma = ElementosPrincipales.jugador.getAe().getArma1();
            int atributo = 0;

            if (arma.getTipoObjeto() == TipoObjeto.ARCO) {
                atributo = ElementosPrincipales.jugador.getGa().getDestreza();

            }
            else if (arma.getTipoObjeto() == TipoObjeto.ESPADA_LIGERA) {
                atributo = (int) ElementosPrincipales.jugador.getGa().getFuerza() / 2
                        + (int) ElementosPrincipales.jugador.getGa().getDestreza() / 2;

            }
            else if (arma.getTipoObjeto() == TipoObjeto.ESPADA_MEDIA) {
                atributo = (int) ElementosPrincipales.jugador.getGa().getFuerza();

            }
            else if (arma.getTipoObjeto() == TipoObjeto.ESPADA_PESADA) {
                atributo = (int) ElementosPrincipales.jugador.getGa().getFuerza()
                        + (int) ElementosPrincipales.jugador.getGa().getDestreza() / 2;

            }
            if (arma != null) {
                arma.atacar(enemigosAlcanzados, atributo);
            }

        }
        Iterator<Enemigo> iterador = enemigosMapa.iterator();

        while (iterador.hasNext()) {
            Enemigo enemigo = iterador.next();
            if (enemigo.getVidaActual() <= 0) {
                iterador.remove();
            }
        }
        ElementosPrincipales.jugador.atacando = false;
    }

    private void actualizarRecogidaObjeto() {

        long tiempoActual = System.currentTimeMillis();

        // Verifica si ha pasado suficiente tiempo desde la última recogida
        if (tiempoActual - ultimoTiempoRecogida < tiempoDebouncing) {
            return; // Ignora la recogida si está dentro del tiempo de debouncing
        }

        // Actualiza el tiempo de la última recogida
        ultimoTiempoRecogida = tiempoActual;
        Iterator<ObjetoUnicoTiled> iterador = objetosMapa.iterator();
        Iterator<ContenedorObjetos> iterador2 = listaContenedores.iterator();
        Rectangle areaJugador = ElementosPrincipales.jugador.getArea();

        while (iterador.hasNext()) {
            ObjetoUnicoTiled objetoActual = iterador.next();
            Rectangle posicionObjetoActual = new Rectangle(
                    objetoActual.getPosicion().x,
                    objetoActual.getPosicion().y,
                    Constantes.LADO_SPRITE,
                    Constantes.LADO_SPRITE);

            if (areaJugador.intersects(posicionObjetoActual) && GestorPrincipal.sd.getRaton().isRecogiendo()) {
                if (ElementosPrincipales.jugador.isSobrepeso()) {
                    return;
                }
                ElementosPrincipales.inventario.recogerObjetos(objetoActual);
                iterador.remove();
                break; // Salir del bucle después de recoger un objeto
            }
        }

        while (iterador2.hasNext()) {
            ContenedorObjetos contenedor = iterador2.next();
            if (contenedor.getObjetos().isEmpty()) {
                iterador2.remove();
            }
            else if (areaJugador.intersects(contenedor.getArea()) && GestorPrincipal.sd.getRaton().isClick()) {
                abrirContenedor(contenedor);
            }
        }
    }

    private void abrirContenedor(ContenedorObjetos contenedor) {
        contenedorAbierto = true;
        contenedorActual = contenedor;
    }

    private void mostrarElementoscontenedor() {
        Rectangle posicionRaton = GestorPrincipal.sd.getRaton().getPosicionRectangle();

        if (contenedorAbierto && contenedorActual != null) {
            int puntoX = contenedorActual.getPosicion().x - ElementosPrincipales.jugador.getPosicionXInt() + Constantes.MARGEN_X;
            int puntoY = contenedorActual.getPosicion().y - ElementosPrincipales.jugador.getPosicionYInt() + Constantes.MARGEN_Y;
            Rectangle areaContenedor = new Rectangle(puntoX, puntoY, 32, 32);
            contenedorActual.setArea(areaContenedor);

            if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(areaContenedor))
                    && GestorPrincipal.sd.getRaton().isClick()) {
                recogerObjetosDelContenedor(contenedorActual);
            }
            else {
                contenedorActual = null;
                contenedorAbierto = false;
            }
        }
    }

    private void recogerObjetosDelContenedor(ContenedorObjetos contenedor) {
        int x = ElementosPrincipales.jugador.getPosicionXInt();
        int y = ElementosPrincipales.jugador.getPosicionYInt();

        for (Objeto objetoActual : contenedor.getObjetos()) {
            ObjetoUnicoTiled objeto = new ObjetoUnicoTiled(new Point(x, y), objetoActual, objetoActual.getCantidad());
            objetosMapa.add(objeto);
        }

        contenedor.getObjetos().clear();
    }

    private void actualizarEnemigos() {
        if (!enemigosMapa.isEmpty()) {
            for (Enemigo enemigo : enemigosMapa) {
                enemigo.setSiguienteNodo(dijkstra.enconcontrarSiguienteNodoParaEnemigo(enemigo));
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

    public void actualizarZonaSalida() {

        for (int i = 0; i < zonasSalida.size(); i++) {
            int puntoX = zonasSalida.get(i).x - ElementosPrincipales.jugador.getPosicionXInt() + Constantes.MARGEN_X;
            int puntoY = zonasSalida.get(i).y - ElementosPrincipales.jugador.getPosicionYInt() + Constantes.MARGEN_Y;
            switch (i) {
                case 0:
                    zonaSalida1 = new Rectangle(puntoX - 18, puntoY, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);
                    break;
                case 1:
                    zonaSalida2 = new Rectangle(puntoX - 18, puntoY, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);
                    break;
                case 2:
                    zonaSalida3 = new Rectangle(puntoX - 18, puntoY, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);
                    break;
                case 3:
                    zonaSalida4 = new Rectangle(puntoX - 18, puntoY, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);
                    break;
                case 4:
                    zonaSalida5 = new Rectangle(puntoX - 18, puntoY, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);
                    break;
                case 5:
                    zonaSalida6 = new Rectangle(puntoX - 18, puntoY, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);
                    break;
                case 6:
                    zonaSalida7 = new Rectangle(puntoX - 18, puntoY, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);
                    break;
                case 7:
                    zonaSalida8 = new Rectangle(puntoX - 18, puntoY, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);
                    break;

            }
        }
    }

    private void actualizarTiendas() {
        for (Tienda tiendaActual : tiendas) {

            int puntoX = tiendaActual.getPosicion().x - ElementosPrincipales.jugador.getPosicionXInt() + Constantes.MARGEN_X;
            int puntoY = tiendaActual.getPosicion().y - ElementosPrincipales.jugador.getPosicionYInt() + Constantes.MARGEN_Y;

            Rectangle nuevaAreaTienda = new Rectangle(puntoX - 18, puntoY, 16, 16);
            tiendaActual.setAreaTienda(nuevaAreaTienda);

            if (ElementosPrincipales.jugador.getLIMITE_ABAJO().intersects(tiendaActual.getAreaTienda())
                    && GestorPrincipal.sd.getRaton().isClick2()) {
                tiendaActiva = tiendaActual;
                System.out.println("Tipo: " + tiendaActiva.getTipo());
                obtenerObjetosMapa(tiendas.get(0).getIdTienda());
                objetosTiendaActual = verificarTipoTienda(tiendaActiva);

                GestorControles.teclado.tiendaActiva = true;
            }
        }
    }

    private void actualizarAreasColision() {
        if (!areasColisionActualizadas.isEmpty()) {
            areasColisionActualizadas.clear();
        }

        for (int i = 0; i < areaColisionOriginales.size(); i++) {
            Rectangle rInicial = areaColisionOriginales.get(i);

            int puntoX = rInicial.x - ElementosPrincipales.jugador.getPosicionXInt() + Constantes.MARGEN_X;
            int puntoY = rInicial.y - ElementosPrincipales.jugador.getPosicionYInt() + Constantes.MARGEN_Y;

            final Rectangle rFinal = new Rectangle(puntoX, puntoY, rInicial.width, rInicial.height);
            areasColisionActualizadas.add(rFinal);
        }
    }

    private void actualizarAreasTransparencia() {
        if (!areasTransparenciaActualizadas.isEmpty()) {
            areasTransparenciaActualizadas.clear();
        }

        for (int i = 0; i < areaTransparenciaOriginales.size(); i++) {
            Rectangle rInicial = areaTransparenciaOriginales.get(i);

            int puntoX = rInicial.x - ElementosPrincipales.jugador.getPosicionXInt() + Constantes.MARGEN_X;
            int puntoY = rInicial.y - ElementosPrincipales.jugador.getPosicionYInt() + Constantes.MARGEN_Y;

            final Rectangle rFinal = new Rectangle(puntoX, puntoY, rInicial.width, rInicial.height);
            areasTransparenciaActualizadas.add(rFinal);
        }
    }

    public Rectangle getBordes(final int posicionX, final int posicionY) {
        int x = Constantes.MARGEN_X - posicionX + ElementosPrincipales.jugador.getANCHO_JUGADOR();
        int y = Constantes.MARGEN_Y - posicionY + ElementosPrincipales.jugador.getALTO_JUGADOR();

        int ancho = this.anchoMapaTiles * Constantes.LADO_SPRITE - ElementosPrincipales.jugador.getANCHO_JUGADOR() * 2;
        int alto = this.altoMapaTiles * Constantes.LADO_SPRITE - ElementosPrincipales.jugador.getALTO_JUGADOR() * 2;

        return new Rectangle(x, y, ancho, alto);
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

    private void dibujarTooltipObjetosMapa(final Graphics g, final SuperficieDibujo sd) {
        Rectangle posicionRaton = sd.getRaton().getPosicionRectangle();

        for (ObjetoUnicoTiled objeto : objetosMapa) {
            int puntoX = (int) objeto.getPosicion().x - ElementosPrincipales.jugador.getPosicionXInt() + Constantes.MARGEN_X;
            int puntoY = (int) objeto.getPosicion().y - ElementosPrincipales.jugador.getPosicionYInt() + Constantes.MARGEN_Y;
            Rectangle nuevaArea = new Rectangle(puntoX, puntoY, 32, 32);

            if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(nuevaArea))) {
                DibujoDebug.dibujarRectanguloContorno(g, nuevaArea, Color.DARK_GRAY);
                dibujarTooltipObjeto(g, sd, objeto.getObjeto());
            }
        }
    }

    private void dibujarTooltipObjeto(Graphics g, SuperficieDibujo sd, Object objeto) {
        // Aquí puedes personalizar la apariencia del tooltip según tus necesidades
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

    private void obtenerObjetosMapa(int idMapa) {
        objetosTiendaMapa.clear();
        ArrayList<String> listaObjetos = RegistroTiendas.obtenerTienda(idMapa);
        for (String idObjeto : listaObjetos) {
            Objeto objeto = RegistroObjetos.obtenerObjeto(Integer.parseInt(idObjeto));
            objetosTiendaMapa.add(objeto);
        }
    }

    private ArrayList<Objeto> verificarTipoTienda(Tienda tienda) {
        objetosTiendaActual.clear();

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

    public Point getPuntoInicial() {
        return puntoInicial;
    }

    public String getSiguienteMapa() {
        return siguienteMapa;
    }

    public void setSiguienteMapa(String siguienteMapa) {
        this.siguienteMapa = siguienteMapa;
    }

    public void setPuntoInicial(Point puntoInicial) {
        this.puntoInicial = puntoInicial;
    }

    public ArrayList<Enemigo> getEnemigosMapa() {
        return enemigosMapa;
    }

    public ArrayList<Nodo> getNodosMapa() {
        return dijkstra.getNodosMapa();
    }

}
