package clases;

import implementacion.Juego;

public class Obstaculo extends ObjetoJuego {

    private boolean magnetita;
    private int vida;

    public Obstaculo(double x, double y, String nombreImagen, double velocidad, boolean magnetita) {
        super(x, y, 64, 64, nombreImagen, velocidad);

        this.magnetita = magnetita;
        this.vida = 90;
    }

    @Override
    public void mover() {

        y += velocidad;

        if (y > Juego.ALTO + 70) {
            activo = false;
        }
    }

    public void recibirDaño(int daño) {

        vida -= daño;

        if (vida <= 0) {
            activo = false;
        }
    }

    public boolean isMagnetita() {
        return magnetita;
    }

    public int getVida() {
        return vida;
    }
}














