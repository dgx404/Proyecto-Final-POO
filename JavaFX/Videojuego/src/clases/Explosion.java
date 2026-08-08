package clases;

public class Explosion extends ObjetoJuego {

    private long inicio;

    public Explosion(double x, double y) {
        super(x, y, 90, 90, "explosion", 0);

        this.inicio = System.nanoTime();
    }

    @Override
    public void mover() {
        if (System.nanoTime() - inicio > 450_000_000L) {
            activo = false;
        }
    }
}