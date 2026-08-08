package clases;

import implementacion.Juego;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Bala extends ObjetoJuego {

    private boolean delJugador;
    private int daño;

    public Bala(double x, double y, String nombreImagen, double velocidad, boolean delJugador, int daño) {
        super(x, y, 24, delJugador ? 48 : 40, nombreImagen, velocidad);

        this.delJugador = delJugador;
        this.daño = daño;
    }

    public Bala(double x, double y, String nombreImagen, double velocidad, boolean delJugador, int daño, double ancho, double alto) {
        super(x, y, ancho, alto, nombreImagen, velocidad);

        this.delJugador = delJugador;
        this.daño = daño;
    }

    @Override
    public void mover() {

        y += velocidad;

        if (y < -70 || y > Juego.ALTO + 70) {
            activo = false;
        }
    }

    @Override
    public void pintar(GraphicsContext graficos) {

        Image imagen = Juego.imagenes.get(nombreImagen);

        if (imagen == null) {

            if (delJugador) {
                graficos.setFill(Color.CYAN);
            } else {
                graficos.setFill(Color.RED);
            }

            graficos.fillRect(x, y, ancho, alto);

            return;
        }

        if (delJugador) {

            graficos.drawImage(imagen, x, y, ancho, alto);

        } else {

            graficos.save();

            graficos.translate(x + ancho / 2, y + alto / 2);
            graficos.rotate(180);
            graficos.drawImage(imagen, -ancho / 2, -alto / 2, ancho, alto);

            graficos.restore();
        }
    }

    public boolean isDelJugador() {
        return delJugador;
    }

    public int getDaño() {
        return daño;
    }
}