package clases;

import implementacion.Juego;

public class EnemigoMini extends ObjetoJuego {

    private int vida;
    private long ultimoDisparo;

    public EnemigoMini(double x, double y) {
        super(x, y, 48, 48, "enemigoMini", 3.4);

        this.vida = 60;
        this.ultimoDisparo = System.nanoTime();
    }

    @Override
    public void mover() {

        y += velocidad;

        if (y > Juego.ALTO + 60) {
            activo = false;
        }
    }

    public Bala intentarDisparar(long tiempoActual) {

        if (tiempoActual - ultimoDisparo > 1_500_000_000L) {

            ultimoDisparo = tiempoActual;

            return new Bala(
                    x + ancho / 2 - 8,
                    y + alto,
                    "balaEnemigo",
                    4.0,
                    false,
                    70,
                    16,
                    28
            );
        }

        return null;
    }

    public void recibirDaño(int daño) {

        vida -= daño;

        if (vida <= 0) {
            activo = false;
        }
    }

    public int getVida() {
        return vida;
    }
}









