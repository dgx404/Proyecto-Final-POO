package clases;

import java.util.ArrayList;
import java.util.List;

import implementacion.Juego;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Jefe extends Enemigo {

    private double direccion;
    private double velocidadLateral;
    private double velocidadEntrada;

    private long ultimoAtaque;

    private int vidaMaxima;

    public Jefe(double x, double y, int vida, int nivel) {
        super(x, y, "jefe", 0.5, vida);

        this.ancho = 180;
        this.alto = 240;

        this.direccion = 1;
        this.vidaMaxima = vida;

        this.velocidadEntrada = 0.75 + nivel * 0.08;
        this.velocidadLateral = 1.4 + nivel * 0.30;

        this.ultimoAtaque = System.nanoTime();
    }

    @Override
    public void mover() {

        if (y < 20) {

            y += velocidadEntrada;

        } else {

            x += velocidadLateral * direccion;

            if (x <= 0) {
                x = 0;
                direccion = 1;
            }

            if (x + ancho >= Juego.ANCHO) {
                x = Juego.ANCHO - ancho;
                direccion = -1;
            }
        }
    }

    @Override
    public void pintar(GraphicsContext graficos) {

        Image imagen = Juego.imagenes.get("jefe");

        if (imagen != null) {
            graficos.drawImage(imagen, x, y, ancho, alto);
        }
    }

    public List<Bala> disparar(long tiempoActual) {

        List<Bala> balas = new ArrayList<Bala>();

        if (y < 0) {
            return balas;
        }

        if (tiempoActual - ultimoAtaque > 1_800_000_000L) {

            ultimoAtaque = tiempoActual;

            balas.add(new Bala(
                    x + 35,
                    y + alto - 20,
                    "balaEnemigo",
                    3.2,
                    false,
                    12,
                    24,
                    40
            ));

            balas.add(new Bala(
                    x + ancho - 59,
                    y + alto - 20,
                    "balaEnemigo",
                    3.2,
                    false,
                    12,
                    24,
                    40
            ));
        }

        return balas;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }
}











