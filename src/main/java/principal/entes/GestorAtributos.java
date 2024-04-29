/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.entes;

/**
 * Clase que gestiona los atributos de una entidad en el juego, como la vida, fuerza, etc. Esta clase se utiliza para
 * mantener y manipular los atributos de los personajes y enemigos del juego.
 *
 * @author GAMER ARRAX
 */
public class GestorAtributos {

    // Atributos de la entidad
    private int nivel;
    private int vida;
    private int vidaMaxima;
    private int fuerza;
    private int destreza;
    private int constitucion;
    private int inteligencia;
    private int suerte;
    private int ataque;
    private int defensaFisica;
    private int defensaMagica;
    private int magia;
    private double evasion;
    private double critico;
    private double resFisica;
    private double resMagica;
    private int resistenciaMaxima;
    private int resistencia;
    private int mana;
    private int manaMaximo;
    private int experiencia;
    private int experienciaMaxima;
    private double limitePeso;
    private double pesoActual;
    private int puntosAtributos;

    // Constructor
    public GestorAtributos(int nivel, int fuerza, int destreza, int constitucion, int inteligencia, int suerte, int experiencia, int experienciaMaxima) {
        this.nivel = nivel;
        this.fuerza = fuerza;
        this.destreza = destreza;
        this.constitucion = constitucion;
        this.inteligencia = inteligencia;
        this.suerte = suerte;
        this.experiencia = experiencia;
        this.experienciaMaxima = experienciaMaxima;
        this.puntosAtributos = 0;
    }

    // Constructor vacío
    public GestorAtributos() {
    }

    // Métodos getters y setters para cada atributo
    // (Omitidos para no repetir el mismo comentario para cada método)
    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public void setVidaMaxima(int vidaMaxima) {
        this.vidaMaxima = vidaMaxima;
    }

    public int getFuerza() {
        return fuerza;
    }

    public void setFuerza(int fuerza) {
        this.fuerza = fuerza;
    }

    public int getDestreza() {
        return destreza;
    }

    public void setDestreza(int destreza) {
        this.destreza = destreza;
    }

    public int getConstitucion() {
        return constitucion;
    }

    public void setConstitucion(int constitucion) {
        this.constitucion = constitucion;
    }

    public int getInteligencia() {
        return inteligencia;
    }

    public void setInteligencia(int inteligencia) {
        this.inteligencia = inteligencia;
    }

    public int getSuerte() {
        return suerte;
    }

    public void setSuerte(int suerte) {
        this.suerte = suerte;
    }

    public int getAtaque() {
        return ataque;
    }

    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    public int getDefensaFisica() {
        return defensaFisica;
    }

    public void setDefensaFisica(int defensaFisica) {
        this.defensaFisica = defensaFisica;
    }

    public int getDefensaMagica() {
        return defensaMagica;
    }

    public void setDefensaMagica(int defensaMagica) {
        this.defensaMagica = defensaMagica;
    }

    public int getMagia() {
        return magia;
    }

    public void setMagia(int magia) {
        this.magia = magia;
    }

    public double getEvasion() {
        return evasion;
    }

    public void setEvasion(double evasion) {
        this.evasion = evasion;
    }

    public double getCritico() {
        return critico;
    }

    public void setCritico(double critico) {
        this.critico = critico;
    }

    public double getResFisica() {
        return resFisica;
    }

    public void setResFisica(double resFisica) {
        this.resFisica = resFisica;
    }

    public double getResMagica() {
        return resMagica;
    }

    public void setResMagica(double resMagica) {
        this.resMagica = resMagica;
    }

    public int getResistenciaMaxima() {
        return resistenciaMaxima;
    }

    public void setResistenciaMaxima(int resistenciaMaxima) {
        this.resistenciaMaxima = resistenciaMaxima;
    }

    public int getResistencia() {
        return resistencia;
    }

    public void setResistencia(int resistencia) {
        this.resistencia = resistencia;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getManaMaximo() {
        return manaMaximo;
    }

    public void setManaMaximo(int manaMaximo) {
        this.manaMaximo = manaMaximo;
    }

    public int getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(int experiencia) {
        this.experiencia = experiencia;
    }

    public int getExperienciaMaxima() {
        return experienciaMaxima;
    }

    public void setExperienciaMaxima(int experienciaMaxima) {
        this.experienciaMaxima = experienciaMaxima;
    }

    public double getLimitePeso() {
        return limitePeso;
    }

    public void setLimitePeso(double limitePeso) {
        this.limitePeso = limitePeso;
    }

    public double getPesoActual() {
        return pesoActual;
    }

    public void setPesoActual(double pesoActual) {
        this.pesoActual = pesoActual;
    }

    public int getPuntosAtributos() {
        return puntosAtributos;
    }

    public void setPuntosAtributos(int puntosAtributos) {
        this.puntosAtributos = puntosAtributos;
    }

}
