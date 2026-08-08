package clases;

import implementacion.Juego;

public class Aliado extends ObjetoJuego {

    private boolean falso;

    public Aliado(double x, double y, boolean falso) {
        super(x, y, 52, 52, "aliado", 0.8);

        this.falso = falso;
    }

    @Override
    public void mover() {
        y += velocidad;

        if (y > Juego.ALTO + 60) {
            activo = false;
        }
    }

    public boolean isFalso() {
        return falso;
    }
}





