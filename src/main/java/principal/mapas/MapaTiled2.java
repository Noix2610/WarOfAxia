/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.mapas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
import principal.inventario.joyas.Joya;
import principal.maquinaestado.juego.menu_tienda.Tienda;
import principal.sprites.HojaSprites;
import principal.sprites.Sprite;

/**
 *
 * @author GAMER ARRAX
 */
public class MapaTiled2 {

    private int anchoMapaTiles;
    private int altoMapaTiles;
    private String siguienteMapa;
    private Point puntoInicial;
    public Rectangle recMapa;

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
    private boolean dibujarX = false;
    HojaSprites hojaBotonX= new HojaSprites("/icons/RatonClick2.png", 16, 16, false);
    BufferedImage botonX;

    public MapaTiled2(final String ruta) {
        Salida.getSalidas().clear();

        String contenido = CargadorRecursos.leerArchivoTexto(ruta);

        JSONObject globalJSON = getObjetoJson(contenido);

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
        botonX = hojaBotonX.getSprites(0).getImagen();
        

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

        if (dibujarX) {
            
            DibujoDebug.dibujarImagen(g, botonX,ElementosPrincipales.jugador.getLIMITE_ABAJO().x,
                    ElementosPrincipales.jugador.getLIMITE_ABAJO().y);
        }

        /*for (Rectangle rectagulo : areasColisionActualizadas) {
            DibujoDebug.dibujarRectanguloContorno(g, rectagulo, Color.blue);
        }

        for (Rectangle rectagulo : areasTransparenciaActualizadas) {
            DibujoDebug.dibujarRectanguloContorno(g, rectagulo, Color.white);
        }*/
        dibujarTooltipObjetosMapa(g, GestorPrincipal.sd);

        /*DibujoDebug.dibujarString(g, zonaSalida1.toString(), 10, 90, Color.white);
        DibujoDebug.dibujarString(g, zonaSalida2.toString(), 10, 100, Color.white);
        DibujoDebug.dibujarString(g, zonaSalida3.toString(), 10, 110, Color.white);
        DibujoDebug.dibujarString(g, zonaSalida4.toString(), 10, 120, Color.white);
        DibujoDebug.dibujarString(g, zonaSalida5.toString(), 10, 130, Color.white);
        DibujoDebug.dibujarString(g, zonaSalida6.toString(), 10, 140, Color.white);
        DibujoDebug.dibujarString(g, zonaSalida7.toString(), 10, 150, Color.white);*/
    }

    private void inicializarAtributosBasicos(JSONObject globalJSON) {
        anchoMapaTiles = getIntJson(globalJSON, "width");
        altoMapaTiles = getIntJson(globalJSON, "height");

        // Obtener el objeto JSON asociado con "start"
        JSONObject puntoInicialJSON = null;

        // Verificar si la clave "start" existe en el JSON
        if (globalJSON.containsKey("start")) {
            // Si existe, obtener el objeto JSON asociado con la clave "start"
            puntoInicialJSON = getObjetoJson(globalJSON.get("start").toString());
        }
        else {

            puntoInicial = Salida.puntoInicialSiguiente;
        }

        if (puntoInicialJSON != null) {
            // Obtener las coordenadas x e y del objeto JSON de "start"
            int x = getIntJson(puntoInicialJSON, "x");
            int y = getIntJson(puntoInicialJSON, "y");

            // Actualizar el punto inicial de la instancia de Mapa
            this.puntoInicial = new Point(x, y); // Ajusta el método según la estructura de tu clase Mapa
        }
    }

    private void inicializarCapas(JSONObject globalJSON) {
        JSONArray capas = getArrayJson(globalJSON.get("layers").toString());
        this.capaSprites1 = new ArrayList<>();
        this.capaSprites2 = new ArrayList<>();
        this.capaColisiones = new ArrayList<>();
        this.capaTransparencias = new ArrayList<>();

        for (int i = 0; i < capas.size(); i++) {
            JSONObject datosCapa = getObjetoJson(capas.get(i).toString());
            String tipo = datosCapa.get("id").toString();

            switch (tipo) {
                case "1", "2":
                    inicializarCapaSprites1(datosCapa);
                    break;
                case "3", "4":
                    inicializarCapaSprites2(datosCapa);
                    break;
            }
        }
        for (int i = 0; i < capas.size(); i++) {
            JSONObject datosCapa = getObjetoJson(capas.get(i).toString());
            String tipo = datosCapa.get("type").toString();

            switch (tipo) {
                case "objectgroup":
                    inicializarCapaColisiones(datosCapa);
                    break;
                case "objectgroup1":
                    inicializarCapaTransparencia(datosCapa);
                    break;
            }
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

    private void inicializarPaletaSprites(JSONObject globalJSON) {
        // Lógica para inicializar la paleta de sprites
        JSONArray coleccionSprites = getArrayJson(globalJSON.get("tilesets").toString());
        int totalSprites = 0;
        for (int i = 0; i < coleccionSprites.size(); i++) {
            JSONObject datosGrupo = getObjetoJson(coleccionSprites.get(i).toString());
            totalSprites += getIntJson(datosGrupo, "tilecount");
        }
        paletaSprites1 = new Sprite[totalSprites];

        for (int i = 0; i < coleccionSprites.size(); i++) {
            JSONObject datosGrupo = getObjetoJson(coleccionSprites.get(i).toString());
            String nombreImagen = datosGrupo.get("image").toString();
            int anchoTile = getIntJson(datosGrupo, "tilewidth");
            int altoTile = getIntJson(datosGrupo, "tileheight");

            HojaSprites hoja = new HojaSprites("/mapas/" + nombreImagen, anchoTile, altoTile, false);

            int primerSpriteColeccion = getIntJson(datosGrupo, "firstgid") - 1;
            int ultimoSpriteColeccion = primerSpriteColeccion + getIntJson(datosGrupo, "tilecount") - 1;

            // Obtener sprites solo una vez
            Sprite[] sprites = new Sprite[ultimoSpriteColeccion - primerSpriteColeccion + 1];
            for (int j = 0; j < sprites.length; j++) {
                sprites[j] = hoja.getSprites(j);
            }

            for (int j = 0; j < this.capaSprites1.size(); j++) {
                CapaSprites capaActual = this.capaSprites1.get(j);
                int[] spritesCapa = capaActual.getSprites();

                for (int k = 0; k < spritesCapa.length; k++) {
                    int idSpriteActual = spritesCapa[k];

                    if (idSpriteActual >= primerSpriteColeccion && idSpriteActual <= ultimoSpriteColeccion) {
                        if (paletaSprites1[idSpriteActual] == null) {
                            paletaSprites1[idSpriteActual] = sprites[idSpriteActual - primerSpriteColeccion];
                        }
                    }
                }
            }
        }
        totalSprites = 0;
        for (int i = 0; i < coleccionSprites.size(); i++) {
            JSONObject datosGrupo = getObjetoJson(coleccionSprites.get(i).toString());
            totalSprites += getIntJson(datosGrupo, "tilecount");
        }

        paletaSprites2 = new Sprite[totalSprites];

        for (int i = 0; i < coleccionSprites.size(); i++) {
            JSONObject datosGrupo = getObjetoJson(coleccionSprites.get(i).toString());
            String nombreImagen = datosGrupo.get("image").toString();
            int anchoTile = getIntJson(datosGrupo, "tilewidth");
            int altoTile = getIntJson(datosGrupo, "tileheight");

            HojaSprites hoja = new HojaSprites("/mapas/" + nombreImagen, anchoTile, altoTile, false);

            int primerSpriteColeccion = getIntJson(datosGrupo, "firstgid") - 1;
            int ultimoSpriteColeccion = primerSpriteColeccion + getIntJson(datosGrupo, "tilecount") - 1;

            // Obtener sprites solo una vez
            Sprite[] sprites = new Sprite[ultimoSpriteColeccion - primerSpriteColeccion + 1];
            for (int j = 0; j < sprites.length; j++) {
                sprites[j] = hoja.getSprites(j);
            }

            for (int j = 0; j < this.capaSprites2.size(); j++) {
                CapaSprites capaActual = this.capaSprites2.get(j);
                int[] spritesCapa = capaActual.getSprites();

                for (int k = 0; k < spritesCapa.length; k++) {
                    int idSpriteActual = spritesCapa[k];

                    if (idSpriteActual >= primerSpriteColeccion && idSpriteActual <= ultimoSpriteColeccion) {
                        if (paletaSprites2[idSpriteActual] == null) {
                            paletaSprites2[idSpriteActual] = sprites[idSpriteActual - primerSpriteColeccion];
                        }
                    }
                }
            }
        }

    }

    private void obtenerObjetosMapa(JSONObject globalJSON) {
        // Lógica para obtener objetos del mapa
        objetosMapa = new ArrayList<>();
        JSONArray coleccionObjetos = getArrayJson(globalJSON.get("objetos").toString());
        for (int i = 0; i < coleccionObjetos.size(); i++) {
            JSONObject datosObjeto = getObjetoJson(coleccionObjetos.get(i).toString());

            int idObjeto = getIntJson(datosObjeto, "id");
            int cantidad = getIntJson(datosObjeto, "cantidad");
            int xObjeto = getIntJson(datosObjeto, "x");
            int yObjeto = getIntJson(datosObjeto, "y");

            Point posicionObjeto = new Point(xObjeto, yObjeto);
            Objeto objeto = RegistroObjetos.obtenerObjeto(idObjeto);
            objeto.setCantidad(cantidad);

            ObjetoUnicoTiled objetoUnico = new ObjetoUnicoTiled(posicionObjeto, objeto, objeto.getCantidad());
            objetosMapa.add(objetoUnico);

        }
    }

    private void obtenerEnemigosMapa(JSONObject globalJSON) {
        // Lógica para obtener enemigos del mapa
        enemigosMapa = new ArrayList<>();
        JSONArray coleccionEnemigos = getArrayJson(globalJSON.get("enemigos").toString());
        for (int i = 0; i < coleccionEnemigos.size(); i++) {
            JSONObject datosEnemigo = getObjetoJson(coleccionEnemigos.get(i).toString());

            int idEnemigo = getIntJson(datosEnemigo, "id");
            int xEnemigo = getIntJson(datosEnemigo, "x");
            int yEnemigo = getIntJson(datosEnemigo, "y");

            if (idEnemigo != 0) {
                Point posicionenemigo = new Point(xEnemigo, yEnemigo);
                Enemigo enemigo = RegistroEnemigos.obtenerEnemigo(idEnemigo);

                enemigo.setPosicion(posicionenemigo.x, posicionenemigo.y);

                enemigosMapa.add(enemigo);
            }

        }
    }

    private void obtenerContenedoresMapa(JSONObject globalJSON) {
        listaContenedores = new ArrayList<>();
        JSONArray coleccionContenedores = getArrayJson(globalJSON.get("contenedores").toString());

        for (int i = 0; i < coleccionContenedores.size(); i++) {
            JSONObject datosContenedor = getObjetoJson(coleccionContenedores.get(i).toString());

            int idContenedor = getIntJson(datosContenedor, "idContenedor");
            int xContenedor = getIntJson(datosContenedor, "x");
            int yContenedor = getIntJson(datosContenedor, "y");

            Point posicionContenedor = new Point(xContenedor, yContenedor);
            Rectangle areacontenedor = new Rectangle(xContenedor, yContenedor, 32, 32);
            ContenedorObjetos contenedor = new ContenedorObjetos(posicionContenedor, idContenedor, areacontenedor);

            JSONArray coleccionObjetos = getArrayJson(datosContenedor.get("objetos").toString());
            for (int j = 0; j < coleccionObjetos.size(); j++) {
                JSONObject datosObjeto = getObjetoJson(coleccionObjetos.get(j).toString()); // Cambio aquí

                int idObjeto = getIntJson(datosObjeto, "idObjeto");
                int cantidadObjeto = getIntJson(datosObjeto, "cantidad");

                Objeto objeto = RegistroObjetos.obtenerObjeto(idObjeto);
                objeto.setCantidad(cantidadObjeto);

                contenedor.getObjetos().add(objeto);
            }

            listaContenedores.add(contenedor);
            areaColisionOriginales.add(contenedor.getArea());
        }
    }

    private void obtenerTiendas(JSONObject globalJSON) {
        tiendas = new ArrayList<>();
        JSONArray coleccionTiendas = getArrayJson(globalJSON.get("tiendas").toString());
        for (int i = 0; i < coleccionTiendas.size(); i++) {
            JSONObject datosContenedor = getObjetoJson(coleccionTiendas.get(i).toString());

            int idTienda = getIntJson(datosContenedor, "id");
            int xTienda = getIntJson(datosContenedor, "x");
            int yTienda = getIntJson(datosContenedor, "y");
            Point posTienda = new Point(xTienda, yTienda);

            Tienda tienda = new Tienda(idTienda, posTienda);
            ArrayList<String> listaObjetosTienda = RegistroTiendas.obtenerTienda(tienda.getIdTienda());
            for (String id : listaObjetosTienda) {
                int idInt = Integer.parseInt(id);
                Objeto objetoTienda = RegistroObjetos.obtenerObjeto(idInt);
                tienda.getObjetosTienda().add(objetoTienda);
            }

            tiendas.add(tienda);
        }
    }

    private void obtenerInformacionSiguienteMapa(JSONObject globalJSON) {
        JSONArray salidasJSON = (JSONArray) globalJSON.get("salidas");

        if (salidasJSON != null && !salidasJSON.isEmpty()) {

            int i = 0;
            for (Object salidaObj : salidasJSON) {
                if (salidaObj instanceof JSONObject) {
                    JSONObject salidaJSON = (JSONObject) salidaObj;

                    // Si no existe, agregar la nueva salida
                    int xSalidaMapa = getIntJson(salidaJSON, "x");
                    int ySalidaMapa = getIntJson(salidaJSON, "y");

                    Point puntoSalidaMapa = new Point(xSalidaMapa, ySalidaMapa);

                    siguienteMapa = getStringFromJsonObject(salidaJSON, "mapaDestino");

                    // Obtener las coordenadas de inicio en el siguiente mapa desde salidaJSON
                    JSONObject puntoInicialJSON = getObjetoJson(salidaJSON.get("punto inicial").toString());
                    int xInicioSiguienteMapa = getIntJson(puntoInicialJSON, "x");
                    int yInicioSiguienteMapa = getIntJson(puntoInicialJSON, "y");
                    Point puntoInicioSiguienteMapa = new Point(xInicioSiguienteMapa, yInicioSiguienteMapa);

                    Salida nuevaSalida = new Salida(puntoInicioSiguienteMapa, puntoSalidaMapa, siguienteMapa);
                    Rectangle nuevaZonaSalida = new Rectangle(xSalidaMapa, ySalidaMapa, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);
                    Salida.getSalidas().add(nuevaSalida);
                    zonasSalida.add(nuevaZonaSalida);

                }
                i++;
            }
        }
        else {
            System.err.println("La clave 'salidas' no está presente o está vacía en el JSON.");
        }

    }

    private void inicializarCapaSprites1(JSONObject datosCapa) {
        int anchoCapa = getIntJson(datosCapa, "width");
        int altoCapa = getIntJson(datosCapa, "height");
        int xCapa = getIntJson(datosCapa, "x");
        int yCapa = getIntJson(datosCapa, "y");

        JSONArray sprites = getArrayJson(datosCapa.get("data").toString());
        int[] spriteCapa = new int[sprites.size()];
        for (int j = 0; j < sprites.size(); j++) {
            int codigoSprite = Integer.parseInt(sprites.get(j).toString());
            spriteCapa[j] = codigoSprite - 1;
        }

        this.capaSprites1.add(new CapaSprites(anchoCapa, altoCapa, xCapa, yCapa, spriteCapa));
    }

    private void inicializarCapaSprites2(JSONObject datosCapa) {
        int anchoCapa = getIntJson(datosCapa, "width");
        int altoCapa = getIntJson(datosCapa, "height");
        int xCapa = getIntJson(datosCapa, "x");
        int yCapa = getIntJson(datosCapa, "y");

        JSONArray sprites = getArrayJson(datosCapa.get("data").toString());
        int[] spriteCapa = new int[sprites.size()];
        for (int j = 0; j < sprites.size(); j++) {
            int codigoSprite = Integer.parseInt(sprites.get(j).toString());
            spriteCapa[j] = codigoSprite - 1;
        }

        this.capaSprites2.add(new CapaSprites(anchoCapa, altoCapa, xCapa, yCapa, spriteCapa));
    }

    private void inicializarCapaColisiones(JSONObject datosCapa) {
        int anchoCapa = getIntJson(datosCapa, "width");
        int altoCapa = getIntJson(datosCapa, "height");
        int xCapa = getIntJson(datosCapa, "x");
        int yCapa = getIntJson(datosCapa, "y");

        JSONArray rectangulos = getArrayJson(datosCapa.get("objects").toString());
        Rectangle[] rectangulosCapa = new Rectangle[rectangulos.size()];

        for (int j = 0; j < rectangulos.size(); j++) {
            JSONObject datosRectangulos = getObjetoJson(rectangulos.get(j).toString());

            int x = getIntJson(datosRectangulos, "x");
            int y = getIntJson(datosRectangulos, "y");
            int ancho = getIntJson(datosRectangulos, "width");
            int alto = getIntJson(datosRectangulos, "height");

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

    private void inicializarCapaTransparencia(JSONObject datosCapa) {
        int anchoCapa = getIntJson(datosCapa, "width");
        int altoCapa = getIntJson(datosCapa, "height");
        int xCapa = getIntJson(datosCapa, "x");
        int yCapa = getIntJson(datosCapa, "y");

        JSONArray rectangulos = getArrayJson(datosCapa.get("objects").toString());
        Rectangle[] rectangulosCapa = new Rectangle[rectangulos.size()];

        for (int j = 0; j < rectangulos.size(); j++) {
            JSONObject datosRectangulos = getObjetoJson(rectangulos.get(j).toString());

            int x = getIntJson(datosRectangulos, "x");
            int y = getIntJson(datosRectangulos, "y");
            int ancho = getIntJson(datosRectangulos, "width");
            int alto = getIntJson(datosRectangulos, "height");

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

    private void actualizarAtaques() {
        if (enemigosMapa.isEmpty() || ElementosPrincipales.jugador.getAlcanceActual().isEmpty()) {
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
            else if (ElementosPrincipales.jugador.getAe().getArma2() != null && ElementosPrincipales.jugador.getAe().getArma2().isPenetrante()) {
                // Verifica si el arma2 no es nulo y es penetrante
                for (Enemigo enemigo : enemigosMapa) {
                    if (ElementosPrincipales.jugador.getAlcanceActual().get(0).intersects(enemigo.getArea())) {
                        enemigosAlcanzados.add(enemigo);
                    }
                }
            }
            else {
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
                System.out.println("Destreza");
            }
            else if (arma.getTipoObjeto() == TipoObjeto.ESPADA_LIGERA) {
                atributo = ElementosPrincipales.jugador.getGa().getFuerza();
                System.out.println("Fuerza");
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

    private JSONObject getObjetoJson(final String codigoJson) {
        JSONParser lector = new JSONParser();
        JSONObject objetoJson = null;
        try {
            Object recuperado = lector.parse(codigoJson);
            objetoJson = (JSONObject) recuperado;
            return objetoJson;
        }
        catch (ParseException e) {
            System.err.println("Error al analizar el JSON: " + e.getMessage());
            e.printStackTrace();
            System.out.println(e);
            return null;
        }
    }

    private JSONArray getArrayJson(final String codigoJson) {
        JSONParser lector = new JSONParser();
        JSONArray arrayJson = null;

        try {
            Object recuperado = lector.parse(codigoJson);
            arrayJson = (JSONArray) recuperado;
        }
        catch (ParseException e) {
            e.printStackTrace();
            System.out.println(e);
        }

        return arrayJson;
    }

    private int getIntJson(final JSONObject objSon, final String clave) {
        Object valorObjeto = objSon.get(clave);
        int valor = 0;
        if (valorObjeto != null) {
            valor = (int) Double.parseDouble(valorObjeto.toString());

        }
        return valor;
    }

    public void actualizarZonaSalida() {
        int cantidadZonas = zonasSalida.size();
        for (int i = 0; i < cantidadZonas; i++) {
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
            if (ElementosPrincipales.jugador.getLIMITE_ABAJO().intersects(tiendaActual.getAreaTienda())) {
                dibujarX = true;
                if (GestorPrincipal.sd.getRaton().isClick2()) {

                    ElementosPrincipales.inventario.getObjetosTienda().clear();
                    for (Objeto objetoTienda : tiendaActual.getObjetosTienda()) {
                        ElementosPrincipales.inventario.getObjetosTienda().add(objetoTienda);
                    }
                    GestorControles.teclado.tiendaActiva = true;
                    break;
                }
            }
            else {
                dibujarX = false;
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

    public static String getStringFromJsonObject(JSONObject jsonObject, String key) {
        // Verificar si el JSON contiene la clave
        if (jsonObject.containsKey(key)) {
            Object value = jsonObject.get(key);
            // Verificar si el valor es de tipo String
            if (value instanceof String) {
                return (String) value;
            }
            else {
                // Manejar el caso en el que el valor no es de tipo String
                System.out.println("El valor para la clave '" + key + "' no es de tipo String.");
            }
        }
        else {
            // Manejar el caso en el que la clave no está presente en el JSON
            System.out.println("La clave '" + key + "' no está presente en el JSON.");
        }
        // En caso de error, devolver una cadena vacía o null según tus necesidades
        return "";
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
