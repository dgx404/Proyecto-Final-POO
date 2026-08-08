package clases;

import implementacion.Juego;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Fondo extends ObjetoJuego {

    private double posicionY;

    public Fondo(String nombreImagen, double velocidad) {
        super(0, 0, Juego.ANCHO, Juego.ALTO, nombreImagen, velocidad);

        reiniciarRecorrido();
    }

    public void reiniciarRecorrido() {
        Image imagen = Juego.imagenes.get(nombreImagen);

        if (imagen == null) {
            posicionY = 0;
            return;
        }

        posicionY = Math.max(0, imagen.getHeight() - Juego.ALTO);
    }

    public void cambiarMapa(String nombreImagen, double velocidad) {
        this.nombreImagen = nombreImagen;
        this.velocidad = velocidad;

        reiniciarRecorrido();
    }

    @Override
    public void mover() {
        if (posicionY > 0) {
            posicionY -= velocidad;

            if (posicionY < 0) {
                posicionY = 0;
            }
        }
    }

    @Override
    public void pintar(GraphicsContext graficos) {
        Image imagen = Juego.imagenes.get(nombreImagen);

        if (imagen == null) {
            return;
        }

        double altoFuente = Math.min(Juego.ALTO, imagen.getHeight());

        graficos.drawImage(
                imagen,
                0,
                posicionY,
                imagen.getWidth(),
                altoFuente,
                0,
                0,
                Juego.ANCHO,
                Juego.ALTO
        );
    }

    public boolean terminoRecorrido() {
        return posicionY <= 0;
    }
}