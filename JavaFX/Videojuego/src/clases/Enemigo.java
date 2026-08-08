package clases;

import implementacion.Juego;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Enemigo extends ObjetoJuego {

    protected int vida;

    private long ultimoDisparo;

    public Enemigo(double x, double y, String nombreImagen, double velocidad, int vida) {
        super(x, y, 58, 58, nombreImagen, velocidad);

        this.vida = vida;
        this.ultimoDisparo = System.nanoTime();
    }

    @Override
    public void mover() {
        y += velocidad;

        if (y > Juego.ALTO + 70) {
            activo = false;
        }
    }

    @Override
    public void pintar(GraphicsContext graficos) {
        Image imagen = Juego.imagenes.get(nombreImagen);

        if (imagen == null) {
            return;
        }

        graficos.save();

        graficos.translate(x + ancho / 2, y + alto / 2);
        graficos.rotate(180);
        graficos.drawImage(imagen, -ancho / 2, -alto / 2, ancho, alto);

        graficos.restore();
    }

    public Bala intentarDisparar(long tiempoActual) {
        if (tiempoActual - ultimoDisparo > 3_000_000_000L) {
            ultimoDisparo = tiempoActual;

            return new Bala(
                    x + ancho / 2 - 12,
                    y + alto,
                    "balaEnemigo",
                    2.2,
                    false,
                    10
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



