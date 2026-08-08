package clases;

import implementacion.Juego;

public class Jugador extends ObjetoJuego {

    private int vidas;
    private int escudo;
    private int puntos;

    private double velocidadNormal;
    private double factorMovimiento;

    private long invulnerableHasta;
    private long dañoPotenciadoHasta;
    private long energiaActivaHasta;

    public Jugador(double x, double y, String nombreImagen, double velocidad) {
        super(x, y, 72, 84, nombreImagen, velocidad);

        this.velocidadNormal = velocidad;
        this.factorMovimiento = 1.0;
        this.vidas = 3;
        this.escudo = 100;
        this.puntos = 0;
    }

    @Override
    public void mover() {

        double movimiento = velocidad * factorMovimiento;

        if (Juego.derecha) {
            x += movimiento;
        }

        if (Juego.izquierda) {
            x -= movimiento;
        }

        if (Juego.arriba) {
            y -= movimiento;
        }

        if (Juego.abajo) {
            y += movimiento;
        }

        if (x < 0) {
            x = 0;
        }

        if (x + ancho > Juego.ANCHO) {
            x = Juego.ANCHO - ancho;
        }

        if (y < 45) {
            y = 45;
        }

        if (y + alto > Juego.ALTO) {
            y = Juego.ALTO - alto;
        }
    }

    public Bala disparar() {
        return new Bala(x + ancho / 2 - 12, y - 45, "balaJugador", -6.5, true, getDañoDisparo());
    }

    public int getDañoDisparo() {

        if (tieneDañoPotenciado()) {
            return 60;
        }

        return 30;
    }

    public void recibirDaño(int daño) {

        if (esInvulnerable()) {
            return;
        }

        escudo -= daño;

        if (escudo <= 0) {
            perderVida();
        }
    }

    public void perderVida() {

        if (esInvulnerable()) {
            return;
        }

        vidas--;
        escudo = 100;

        x = Juego.ANCHO / 2.0 - ancho / 2.0;
        y = Juego.ALTO - alto - 20;

        invulnerableHasta = System.nanoTime() + 3_000_000_000L;
    }

    public boolean esInvulnerable() {
        return System.nanoTime() < invulnerableHasta;
    }

    public boolean debePintarse() {

        if (!esInvulnerable()) {
            return true;
        }

        return (System.currentTimeMillis() / 120) % 2 == 0;
    }

    public void activarTurbo(boolean turbo) {

        if (turbo) {
            velocidad = velocidadNormal * 2.0;
            nombreImagen = "naveTurbo";
        } else {
            velocidad = velocidadNormal;
            nombreImagen = "nave";
        }
    }

    public void recuperarEscudo(int cantidad) {

        escudo += cantidad;

        if (escudo > 100) {
            escudo = 100;
        }
    }

    public void repararCompleto() {
        escudo = 100;
    }

    public void agregarVida() {

        if (vidas < 3) {
            vidas++;
        }
    }

    public void activarPotenciadorDaño() {
        dañoPotenciadoHasta = System.nanoTime() + 8_000_000_000L;
    }

    public boolean tieneDañoPotenciado() {
        return System.nanoTime() < dañoPotenciadoHasta;
    }

    public void activarCelulaEnergia() {
        energiaActivaHasta = System.nanoTime() + 10_000_000_000L;
    }

    public boolean tieneEnergiaActiva() {
        return System.nanoTime() < energiaActivaHasta;
    }

    public long getIntervaloDisparo() {

        if (tieneEnergiaActiva()) {
            return 110_000_000L;
        }

        return 240_000_000L;
    }

    public void agregarPuntos(int cantidad) {
        puntos += cantidad;
    }

    public void setFactorMovimiento(double factorMovimiento) {
        this.factorMovimiento = factorMovimiento;
    }

    public int getVidas() {
        return vidas;
    }

    public int getEscudo() {
        return escudo;
    }

    public int getPuntos() {
        return puntos;
    }
}














