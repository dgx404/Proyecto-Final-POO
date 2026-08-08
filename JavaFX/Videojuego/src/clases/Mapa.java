package clases;

public class Mapa {

    private String nombre;
    private String fondo;

    private double velocidadFondo;

    private long intervaloEnemigos;
    private long intervaloObstaculos;
    private long intervaloPowerUp;

    private int vidaJefe;
    private boolean tieneJefe;

    private String comunicador;
    private String retrato;
    private String mensaje;

    public Mapa(String nombre, String fondo, double velocidadFondo,
                long intervaloEnemigos, long intervaloObstaculos,
                long intervaloPowerUp, int vidaJefe, boolean tieneJefe,
                String comunicador, String retrato, String mensaje) {

        this.nombre = nombre;
        this.fondo = fondo;
        this.velocidadFondo = velocidadFondo;
        this.intervaloEnemigos = intervaloEnemigos;
        this.intervaloObstaculos = intervaloObstaculos;
        this.intervaloPowerUp = intervaloPowerUp;
        this.vidaJefe = vidaJefe;
        this.tieneJefe = tieneJefe;
        this.comunicador = comunicador;
        this.retrato = retrato;
        this.mensaje = mensaje;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFondo() {
        return fondo;
    }

    public double getVelocidadFondo() {
        return velocidadFondo;
    }

    public long getIntervaloEnemigos() {
        return intervaloEnemigos;
    }

    public long getIntervaloObstaculos() {
        return intervaloObstaculos;
    }

    public long getIntervaloPowerUp() {
        return intervaloPowerUp;
    }

    public int getVidaJefe() {
        return vidaJefe;
    }

    public boolean isTieneJefe() {
        return tieneJefe;
    }

    public String getComunicador() {
        return comunicador;
    }

    public String getRetrato() {
        return retrato;
    }

    public String getMensaje() {
        return mensaje;
    }
}
