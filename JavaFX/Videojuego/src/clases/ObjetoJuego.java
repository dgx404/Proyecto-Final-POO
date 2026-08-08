package clases;

import implementacion.Juego;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class ObjetoJuego {

    protected double x;
    protected double y;
    protected double ancho;
    protected double alto;
    protected double velocidad;
    protected String nombreImagen;
    protected boolean activo;

    public ObjetoJuego(double x, double y, double ancho, double alto, String nombreImagen, double velocidad) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.nombreImagen = nombreImagen;
        this.velocidad = velocidad;
        this.activo = true;
    }

    public void pintar(GraphicsContext graficos) {
        Image imagen = Juego.imagenes.get(nombreImagen);

        if (imagen != null) {
            graficos.drawImage(imagen, x, y, ancho, alto);
        }
    }

    public boolean colisionaCon(ObjetoJuego otro) {
        Rectangle2D rectangulo1 = new Rectangle2D(x, y, ancho, alto);
        Rectangle2D rectangulo2 = new Rectangle2D(otro.x, otro.y, otro.ancho, otro.alto);

        return rectangulo1.intersects(rectangulo2);
    }

    public abstract void mover();

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getAncho() {
        return ancho;
    }

    public double getAlto() {
        return alto;
    }

    public double getVelocidad() {
        return velocidad;
    }

    public String getNombreImagen() {
        return nombreImagen;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen = nombreImagen;
    }

    public void setVelocidad(double velocidad) {
        this.velocidad = velocidad;
    }
}















