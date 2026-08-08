package clases;

import implementacion.Juego;

public class PowerUp extends ObjetoJuego {

    private String tipo;

    public PowerUp(double x, double y, String tipo, String nombreImagen) {
        super(x, y, 52, 52, nombreImagen, 0.8);

        this.tipo = tipo;
    }

    @Override
    public void mover() {
        y += velocidad;

        if (y > Juego.ALTO + 60) {
            activo = false;
        }
    }

    public String getTipo() {
        return tipo;
    }
}