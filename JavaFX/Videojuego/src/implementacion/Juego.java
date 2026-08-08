package implementacion;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import clases.Aliado;
import clases.Bala;
import clases.EnemigoMini;
import clases.Explosion;
import clases.Fondo;
import clases.Jefe;
import clases.Jugador;
import clases.Mapa;
import clases.Obstaculo;
import clases.PowerUp;
import clases.Ranking;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Juego extends Application {

    public static final int ANCHO = 700;
    public static final int ALTO = 500;

    public static boolean derecha;
    public static boolean izquierda;
    public static boolean arriba;
    public static boolean abajo;

    public static HashMap<String, Image> imagenes;

    private Canvas lienzo;
    private GraphicsContext graficos;

    private Jugador jugador;
    private Fondo fondo;
    private Jefe jefe;

    private List<Bala> balasJugador;
    private List<Bala> balasEnemigas;
    private List<Obstaculo> obstaculos;
    private List<PowerUp> powerUps;
    private List<Explosion> explosiones;
    private List<Aliado> aliados;
    private List<EnemigoMini> enemigosMini;

    private Mapa[] mapas;
    private int mapaActual;

    private Random random;

    private boolean disparando;
    private boolean jefeActivo;
    private boolean mostrarInstrucciones;
    private boolean rankingGuardado;

    private long ultimoObstaculo;
    private long ultimoPowerUp;
    private long ultimoAliado;
    private long ultimoDisparoJugador;

    private long dialogoHasta;
    private String dialogoNombre;
    private String dialogoRetrato;
    private String dialogoTexto;

    private long alertaHasta;
    private String alertaTexto;

    private EstadoJuego estado;

    private enum EstadoJuego {
        MENU,
        JUGANDO,
        GAME_OVER,
        VICTORIA
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage ventana) {

        inicializarComponentes();

        Group root = new Group();
        root.getChildren().add(lienzo);

        Scene escena = new Scene(root, ANCHO, ALTO);

        gestionEventos(escena);

        ventana.setScene(escena);
        ventana.setTitle("Star Fracture");
        ventana.setResizable(false);
        ventana.show();

        cicloJuego();
    }

    public void inicializarComponentes() {

        lienzo = new Canvas(ANCHO, ALTO);
        graficos = lienzo.getGraphicsContext2D();

        imagenes = new HashMap<String, Image>();

        cargarImagenes();

        balasJugador = new ArrayList<Bala>();
        balasEnemigas = new ArrayList<Bala>();
        obstaculos = new ArrayList<Obstaculo>();
        powerUps = new ArrayList<PowerUp>();
        explosiones = new ArrayList<Explosion>();
        aliados = new ArrayList<Aliado>();
        enemigosMini = new ArrayList<EnemigoMini>();

        random = new Random();

        dialogoNombre = "";
        dialogoRetrato = "";
        dialogoTexto = "";
        alertaTexto = "";

        crearMapas();

        estado = EstadoJuego.MENU;
    }

    public void crearMapas() {

        mapas = new Mapa[5];

        mapas[0] = new Mapa(
                "TERRITORIO PUMA",
                "mapa1",
                0.12,
                4_000_000_000L,
                3_600_000_000L,
                13_000_000_000L,
                0,
                false,
                "TALI THORNE",
                "tali",
                "Sterling, abandona Territorio Puma. Mantén estable la Fracture-One y sigue el corredor de evacuación."
        );

        mapas[1] = new Mapa(
                "NEBULOSA DE NEON",
                "mapa2",
                0.11,
                4_000_000_000L,
                3_400_000_000L,
                13_000_000_000L,
                500,
                true,
                "TALI THORNE",
                "tali",
                "Entramos en la Nebulosa de Neon. La gravedad es inestable. Rescata las naves civiles que encuentres."
        );

        mapas[2] = new Mapa(
                "EL COLMILLO",
                "mapa3",
                0.10,
                4_000_000_000L,
                3_200_000_000L,
                12_500_000_000L,
                750,
                true,
                "TALI THORNE",
                "tali",
                "Entramos a El Colmillo. La magnetita puede interferir con los controles. Mantente alerta."
        );

        mapas[3] = new Mapa(
                "ESTACION ORBITAL VIGILANTE",
                "mapa4",
                0.09,
                4_000_000_000L,
                3_000_000_000L,
                12_000_000_000L,
                1000,
                true,
                "TALI THORNE",
                "tali",
                "La Estacion Vigilante esta colapsando. Hay señales civiles mezcladas con transmisiones desconocidas."
        );

        mapas[4] = new Mapa(
                "STAR FRACTURE VACIO",
                "mapa5",
                0.08,
                4_000_000_000L,
                2_800_000_000L,
                11_500_000_000L,
                3000,
                true,
                "DR. ELIAN VANCE",
                "vance",
                "Sterling, el refugio esta al final del corredor. La Formula de Salto Luminico esta lista. Debemos llegar."
        );
    }

    public void iniciarNuevaPartida() {

        derecha = false;
        izquierda = false;
        arriba = false;
        abajo = false;

        disparando = false;
        jefeActivo = false;
        rankingGuardado = false;

        mapaActual = 0;

        jugador = new Jugador(ANCHO / 2.0 - 36, ALTO - 105, "nave", 3);

        fondo = new Fondo(
                mapas[mapaActual].getFondo(),
                mapas[mapaActual].getVelocidadFondo()
        );

        estado = EstadoJuego.JUGANDO;

        iniciarMapa();
    }

    public void iniciarMapa() {

        balasJugador.clear();
        balasEnemigas.clear();
        obstaculos.clear();
        powerUps.clear();
        explosiones.clear();
        aliados.clear();
        enemigosMini.clear();

        jefe = null;
        jefeActivo = false;

        long ahora = System.nanoTime();

        ultimoObstaculo = ahora;
        ultimoPowerUp = ahora;
        ultimoAliado = ahora;
        ultimoDisparoJugador = ahora;

        if (fondo == null) {

            fondo = new Fondo(
                    mapas[mapaActual].getFondo(),
                    mapas[mapaActual].getVelocidadFondo()
            );

        } else {

            fondo.cambiarMapa(
                    mapas[mapaActual].getFondo(),
                    mapas[mapaActual].getVelocidadFondo()
            );
        }

        mostrarDialogo(
                mapas[mapaActual].getComunicador(),
                mapas[mapaActual].getRetrato(),
                mapas[mapaActual].getMensaje(),
                5
        );
    }

    public void cicloJuego() {

        AnimationTimer timer = new AnimationTimer() {

            @Override
            public void handle(long tiempoActual) {

                if (estado == EstadoJuego.JUGANDO) {
                    actualizarEstado(tiempoActual);
                }

                pintar();
            }
        };

        timer.start();
    }

    public void actualizarEstado(long tiempoActual) {

        aplicarMagnetita();

        jugador.mover();
        fondo.mover();

        dispararJugador(tiempoActual);
        generarElementos(tiempoActual);
        moverObjetos(tiempoActual);
        comprobarColisiones();
        limpiarObjetos();
        controlarMapa();

        if (jugador.getVidas() <= 0) {
            estado = EstadoJuego.GAME_OVER;
            guardarRanking();
        }
    }

    public void aplicarMagnetita() {

        jugador.setFactorMovimiento(1.0);

        for (Obstaculo obstaculo : obstaculos) {

            if (!obstaculo.isMagnetita()) {
                continue;
            }

            double dx = obstaculo.getX() - jugador.getX();
            double dy = obstaculo.getY() - jugador.getY();
            double distancia = Math.sqrt(dx * dx + dy * dy);

            if (distancia < 120) {
                jugador.setFactorMovimiento(0.65);
                mostrarAlerta("INTERFERENCIA MAGNETICA", 1);
                break;
            }
        }
    }

    public void dispararJugador(long tiempoActual) {

        if (!disparando) {
            return;
        }

        if (tiempoActual - ultimoDisparoJugador >= jugador.getIntervaloDisparo()) {
            ultimoDisparoJugador = tiempoActual;
            balasJugador.add(jugador.disparar());
        }
    }

    public void generarElementos(long tiempoActual) {

        if (jefeActivo || fondo.terminoRecorrido()) {
            return;
        }

        Mapa mapa = mapas[mapaActual];

        if (tiempoActual - ultimoObstaculo > mapa.getIntervaloObstaculos()) {

            ultimoObstaculo = tiempoActual;

            boolean magnetita = mapaActual >= 2 && random.nextInt(100) < 18;
            String imagenAsteroide;

            if (magnetita) {
                imagenAsteroide = "asteroideMagnetita";
            } else {
                imagenAsteroide = "asteroide" + (random.nextInt(3) + 1);
            }

            obstaculos.add(new Obstaculo(
                    random.nextInt(ANCHO - 75),
                    -75,
                    imagenAsteroide,
                    0.75 + mapaActual * 0.05,
                    magnetita
            ));
        }

        if (tiempoActual - ultimoAliado > 10_000_000_000L) {

            ultimoAliado = tiempoActual;

            boolean falso = random.nextInt(100) < 25;

            aliados.add(new Aliado(
                    random.nextInt(ANCHO - 60),
                    -60,
                    falso
            ));
        }

        if (tiempoActual - ultimoPowerUp > mapa.getIntervaloPowerUp()) {

            ultimoPowerUp = tiempoActual;

            generarPowerUp();
        }
    }

    public void generarPowerUp() {

        int tipo = random.nextInt(4);

        String tipoPowerUp;
        String imagenPowerUp;

        if (tipo == 0) {

            tipoPowerUp = "escudo";
            imagenPowerUp = "escudo";

        } else if (tipo == 1) {

            tipoPowerUp = "kitReparacion";
            imagenPowerUp = "kitReparacion";

        } else if (tipo == 2) {

            tipoPowerUp = "potenciadorDaño";
            imagenPowerUp = "potenciadorDaño";

        } else {

            tipoPowerUp = "celulaEnergia";
            imagenPowerUp = "celulaEnergia";
        }

        powerUps.add(new PowerUp(
                random.nextInt(ANCHO - 60),
                -60,
                tipoPowerUp,
                imagenPowerUp
        ));
    }

    public void moverObjetos(long tiempoActual) {

        for (Bala bala : balasJugador) {
            bala.mover();
        }

        for (Bala bala : balasEnemigas) {
            bala.mover();
        }

        for (Obstaculo obstaculo : obstaculos) {
            obstaculo.mover();
        }

        for (PowerUp powerUp : powerUps) {
            powerUp.mover();
        }

        for (Aliado aliado : aliados) {
            aliado.mover();
        }

        for (Explosion explosion : explosiones) {
            explosion.mover();
        }

        for (EnemigoMini enemigoMini : enemigosMini) {

            enemigoMini.mover();

            Bala bala = enemigoMini.intentarDisparar(tiempoActual);

            if (bala != null) {
                balasEnemigas.add(bala);
            }
        }

        if (jefeActivo && jefe != null && jefe.isActivo()) {
            jefe.mover();
            balasEnemigas.addAll(jefe.disparar(tiempoActual));
        }
    }

    public void comprobarColisiones() {

        comprobarBalasContraMiniEnemigos();
        comprobarBalasContraAsteroides();
        comprobarBalasContraJefe();
        comprobarBalasEnemigasContraJugador();
        comprobarMiniEnemigosContraJugador();
        comprobarAsteroidesContraJugador();
        comprobarAliados();
        comprobarPowerUps();
    }

    public void comprobarBalasContraMiniEnemigos() {

        for (Bala bala : balasJugador) {

            if (!bala.isActivo()) {
                continue;
            }

            for (EnemigoMini enemigoMini : enemigosMini) {

                if (!enemigoMini.isActivo()) {
                    continue;
                }

                if (bala.colisionaCon(enemigoMini)) {

                    bala.setActivo(false);
                    enemigoMini.recibirDaño(bala.getDaño());

                    if (!enemigoMini.isActivo()) {

                        jugador.agregarPuntos(250);

                        explosiones.add(new Explosion(
                                enemigoMini.getX() - 20,
                                enemigoMini.getY() - 20
                        ));

                        mostrarAlerta("ENEMIGO KRELL DESTRUIDO +250", 2);
                    }

                    break;
                }
            }
        }
    }

    public void comprobarBalasContraAsteroides() {

        for (Bala bala : balasJugador) {

            if (!bala.isActivo()) {
                continue;
            }

            for (Obstaculo obstaculo : obstaculos) {

                if (!obstaculo.isActivo()) {
                    continue;
                }

                if (bala.colisionaCon(obstaculo)) {

                    bala.setActivo(false);
                    obstaculo.recibirDaño(bala.getDaño());

                    if (!obstaculo.isActivo()) {

                        jugador.agregarPuntos(50);

                        explosiones.add(new Explosion(
                                obstaculo.getX() - 13,
                                obstaculo.getY() - 13
                        ));
                    }

                    break;
                }
            }
        }
    }

    public void comprobarBalasContraJefe() {

        if (!jefeActivo || jefe == null || !jefe.isActivo()) {
            return;
        }

        for (Bala bala : balasJugador) {

            if (!bala.isActivo()) {
                continue;
            }

            if (bala.colisionaCon(jefe)) {

                bala.setActivo(false);
                jefe.recibirDaño(bala.getDaño());

                if (!jefe.isActivo()) {

                    jugador.agregarPuntos(1000);

                    explosiones.add(new Explosion(
                            jefe.getX() + jefe.getAncho() / 2 - 45,
                            jefe.getY() + jefe.getAlto() / 2 - 45
                    ));
                }
            }
        }
    }

    public void comprobarBalasEnemigasContraJugador() {

        for (Bala bala : balasEnemigas) {

            if (bala.isActivo() && bala.colisionaCon(jugador)) {
                bala.setActivo(false);
                jugador.recibirDaño(bala.getDaño());
            }
        }
    }

    public void comprobarMiniEnemigosContraJugador() {

        for (EnemigoMini enemigoMini : enemigosMini) {

            if (enemigoMini.isActivo() && enemigoMini.colisionaCon(jugador)) {

                enemigoMini.setActivo(false);

                jugador.recibirDaño(70);

                explosiones.add(new Explosion(
                        enemigoMini.getX() - 20,
                        enemigoMini.getY() - 20
                ));

                mostrarAlerta("IMPACTO KRELL -70 ESCUDO", 2);
            }
        }
    }

    public void comprobarAsteroidesContraJugador() {

        for (Obstaculo obstaculo : obstaculos) {

            if (obstaculo.isActivo() && obstaculo.colisionaCon(jugador)) {

                obstaculo.setActivo(false);
                jugador.perderVida();

                mostrarAlerta("IMPACTO CRITICO", 2);
            }
        }
    }

    public void comprobarAliados() {

        for (Aliado aliado : aliados) {

            if (!aliado.isActivo()) {
                continue;
            }

            if (aliado.colisionaCon(jugador)) {

                aliado.setActivo(false);

                if (!aliado.isFalso()) {

                    jugador.agregarPuntos(500);
                    jugador.recuperarEscudo(25);

                    mostrarAlerta("REFUGIADOS RESCATADOS +500", 2);

                } else {

                    mostrarAlerta("¡SEÑAL FALSA! EMBOSCADA KRELL", 2);

                    enemigosMini.add(new EnemigoMini(
                            aliado.getX(),
                            Math.max(-50, aliado.getY() - 40)
                    ));
                }
            }
        }
    }

    public void comprobarPowerUps() {

        for (PowerUp powerUp : powerUps) {

            if (!powerUp.isActivo()) {
                continue;
            }

            if (powerUp.colisionaCon(jugador)) {
                powerUp.setActivo(false);
                aplicarPowerUp(powerUp);
            }
        }
    }

    public void aplicarPowerUp(PowerUp powerUp) {

        String tipo = powerUp.getTipo();

        if (tipo.equals("escudo")) {

            jugador.recuperarEscudo(35);
            mostrarAlerta("ESCUDO +35", 2);

        } else if (tipo.equals("kitReparacion")) {

            jugador.repararCompleto();
            mostrarAlerta("REPARACION COMPLETA", 2);

        } else if (tipo.equals("potenciadorDaño")) {

            jugador.activarPotenciadorDaño();
            mostrarAlerta("DAÑO x2 ACTIVADO", 2);

        } else if (tipo.equals("celulaEnergia")) {

            jugador.activarCelulaEnergia();
            mostrarAlerta("DISPARO RAPIDO ACTIVADO", 2);
        }
    }

    public void limpiarObjetos() {

        balasJugador.removeIf(bala -> !bala.isActivo());
        balasEnemigas.removeIf(bala -> !bala.isActivo());
        obstaculos.removeIf(obstaculo -> !obstaculo.isActivo());
        powerUps.removeIf(powerUp -> !powerUp.isActivo());
        explosiones.removeIf(explosion -> !explosion.isActivo());
        aliados.removeIf(aliado -> !aliado.isActivo());
        enemigosMini.removeIf(enemigoMini -> !enemigoMini.isActivo());
    }

    public void controlarMapa() {

        if (!fondo.terminoRecorrido()) {
            return;
        }

        Mapa mapa = mapas[mapaActual];

        if (!jefeActivo) {

            if (mapa.isTieneJefe()) {

                jefeActivo = true;

                obstaculos.clear();
                powerUps.clear();
                aliados.clear();
                enemigosMini.clear();
                balasEnemigas.clear();

                jefe = new Jefe(
                        ANCHO / 2.0 - 90,
                        -250,
                        mapa.getVidaJefe(),
                        mapaActual
                );

                mostrarAlerta("OVERLORD VONDRAK DETECTADO", 3);

                if (mapaActual == 4) {

                    mostrarDialogo(
                            "OVERLORD VONDRAK",
                            "vondrak",
                            "Se termino la huida, Sterling. El refugio sera tu tumba.",
                            4
                    );

                } else {

                    mostrarDialogo(
                            "OVERLORD VONDRAK",
                            "vondrak",
                            "Otra vez en mi territorio, piloto. Veamos cuanto tiempo sobrevives.",
                            4
                    );
                }

            } else {

                avanzarMapa();
            }

            return;
        }

        if (jefe != null && !jefe.isActivo()) {
            avanzarMapa();
        }
    }

    public void avanzarMapa() {

        mapaActual++;

        if (mapaActual >= mapas.length) {

            estado = EstadoJuego.VICTORIA;
            guardarRanking();

            return;
        }

        iniciarMapa();
    }

    public void pintar() {

        if (estado == EstadoJuego.MENU) {
            pintarMenu();
            return;
        }

        graficos.clearRect(0, 0, ANCHO, ALTO);

        fondo.pintar(graficos);

        for (Obstaculo obstaculo : obstaculos) {
            obstaculo.pintar(graficos);
        }

        for (Aliado aliado : aliados) {
            aliado.pintar(graficos);
        }

        for (PowerUp powerUp : powerUps) {
            powerUp.pintar(graficos);
        }

        for (EnemigoMini enemigoMini : enemigosMini) {
            enemigoMini.pintar(graficos);
        }

        for (Bala bala : balasJugador) {
            bala.pintar(graficos);
        }

        for (Bala bala : balasEnemigas) {
            bala.pintar(graficos);
        }

        if (jefeActivo && jefe != null && jefe.isActivo()) {
            jefe.pintar(graficos);
            pintarVidaJefe();
        }

        for (Explosion explosion : explosiones) {
            explosion.pintar(graficos);
        }

        if (jugador != null && jugador.debePintarse()) {
            jugador.pintar(graficos);
        }

        pintarHUD();
        pintarDialogo();
        pintarAlerta();

        if (estado == EstadoJuego.GAME_OVER) {
            pintarGameOver();
        }

        if (estado == EstadoJuego.VICTORIA) {
            pintarVictoria();
        }
    }

    public void pintarMenu() {

        graficos.clearRect(0, 0, ANCHO, ALTO);

        Image fondoMenu = imagenes.get("fondoMenu");

        if (fondoMenu != null) {
            graficos.drawImage(fondoMenu, 0, 0, ANCHO, ALTO);
        } else {
            graficos.setFill(Color.BLACK);
            graficos.fillRect(0, 0, ANCHO, ALTO);
        }

        Image logo = imagenes.get("logo");

        if (logo != null) {
            graficos.drawImage(logo, 125, 15, 450, 270);
        }

        graficos.setFill(Color.CYAN);
        graficos.setFont(Font.font("Consolas", FontWeight.BOLD, 24));
        graficos.fillText("ENTER - JUGAR", 255, 330);

        graficos.setFill(Color.WHITE);
        graficos.setFont(Font.font("Consolas", 18));

        graficos.fillText("I - INSTRUCCIONES", 255, 375);
        graficos.fillText("ESC - SALIR", 285, 410);

        if (mostrarInstrucciones) {
            pintarInstrucciones();
        }
    }

    public void pintarInstrucciones() {

        graficos.setFill(Color.rgb(0, 0, 0, 0.90));
        graficos.fillRect(100, 125, 500, 275);

        graficos.setFill(Color.CYAN);
        graficos.setFont(Font.font("Consolas", FontWeight.BOLD, 22));
        graficos.fillText("CONTROLES", 285, 165);

        graficos.setFill(Color.WHITE);
        graficos.setFont(Font.font("Consolas", 17));

        graficos.fillText("Flechas - Mover Fracture-One", 180, 205);
        graficos.fillText("SPACE - Mantener para disparar", 180, 240);
        graficos.fillText("SHIFT - Turbo", 180, 275);
        graficos.fillText("Rescata aliados y destruye asteroides.", 150, 315);
        graficos.fillText("Cuidado con las señales falsas.", 180, 345);
        graficos.fillText("Presiona I para cerrar.", 225, 375);
    }

    public void pintarHUD() {

        if (jugador == null) {
            return;
        }

        graficos.setFill(Color.WHITE);
        graficos.setFont(Font.font("Consolas", FontWeight.BOLD, 14));

        graficos.fillText("VIDAS", 12, 18);

        Image naveVida = imagenes.get("nave");

        if (naveVida != null) {

            for (int i = 0; i < jugador.getVidas(); i++) {
                graficos.drawImage(naveVida, 15 + i * 27, 25, 22, 26);
            }
        }

        graficos.fillText("ESCUDO", 115, 18);

        graficos.setFill(Color.DARKGRAY);
        graficos.fillRect(115, 26, 120, 12);

        graficos.setFill(Color.CYAN);
        graficos.fillRect(115, 26, 120 * jugador.getEscudo() / 100.0, 12);

        graficos.setFill(Color.WHITE);
        graficos.fillText("PUNTOS: " + jugador.getPuntos(), 280, 25);
        graficos.fillText("SECTOR " + (mapaActual + 1) + "/5", 570, 25);

        if (jugador.tieneDañoPotenciado()) {
            graficos.setFill(Color.ORANGE);
            graficos.fillText("DAÑO x2", 280, 48);
        }

        if (jugador.tieneEnergiaActiva()) {
            graficos.setFill(Color.CYAN);
            graficos.fillText("ENERGIA: DISPARO RAPIDO", 380, 48);
        }
    }

    public void pintarVidaJefe() {

        double porcentaje = (double) jefe.getVida() / jefe.getVidaMaxima();

        if (porcentaje < 0) {
            porcentaje = 0;
        }

        graficos.setFill(Color.rgb(40, 10, 10, 0.85));
        graficos.fillRect(175, 55, 350, 15);

        graficos.setFill(Color.RED);
        graficos.fillRect(175, 55, 350 * porcentaje, 15);

        graficos.setFill(Color.WHITE);
        graficos.setFont(Font.font("Consolas", FontWeight.BOLD, 13));
        graficos.fillText("OVERLORD VONDRAK", 275, 87);
    }

    public void mostrarDialogo(String nombre, String retrato, String texto, int segundos) {

        dialogoNombre = nombre;
        dialogoRetrato = retrato;
        dialogoTexto = texto;

        dialogoHasta = System.nanoTime() + segundos * 1_000_000_000L;
    }

    public void pintarDialogo() {

        if (System.nanoTime() > dialogoHasta) {
            return;
        }

        Image marco = imagenes.get("marcoDialogo");

        if (marco != null) {
            graficos.drawImage(marco, 40, 340, 620, 140);
        }

        Image retrato = imagenes.get(dialogoRetrato);

        if (retrato != null) {
            graficos.drawImage(retrato, 55, 360, 90, 90);
        }

        graficos.setFill(Color.CYAN);
        graficos.setFont(Font.font("Consolas", FontWeight.BOLD, 14));
        graficos.fillText(dialogoNombre, 160, 370);

        graficos.setFill(Color.WHITE);
        graficos.setFont(Font.font("Consolas", 12));

        dibujarTextoAjustado(dialogoTexto, 160, 395, 475, 4);
    }

    public void dibujarTextoAjustado(String texto, double x, double y, double anchoMaximo, int maximoLineas) {

        String[] palabras = texto.split(" ");
        String linea = "";
        int numeroLinea = 0;

        for (String palabra : palabras) {

            String prueba = linea.isEmpty() ? palabra : linea + " " + palabra;

            Text medidor = new Text(prueba);
            medidor.setFont(graficos.getFont());

            if (medidor.getLayoutBounds().getWidth() > anchoMaximo && !linea.isEmpty()) {

                graficos.fillText(linea, x, y + numeroLinea * 17);

                numeroLinea++;

                if (numeroLinea >= maximoLineas) {
                    return;
                }

                linea = palabra;

            } else {

                linea = prueba;
            }
        }

        if (!linea.isEmpty() && numeroLinea < maximoLineas) {
            graficos.fillText(linea, x, y + numeroLinea * 17);
        }
    }

    public void mostrarAlerta(String texto, int segundos) {

        alertaTexto = texto;
        alertaHasta = System.nanoTime() + segundos * 1_000_000_000L;
    }

    public void pintarAlerta() {

        if (System.nanoTime() > alertaHasta) {
            return;
        }

        Image marco = imagenes.get("marcoAlerta");

        if (marco != null) {
            graficos.drawImage(marco, 170, 65, 360, 100);
        }

        graficos.setFill(Color.WHITE);
        graficos.setFont(Font.font("Consolas", FontWeight.BOLD, 15));
        graficos.fillText(alertaTexto, 220, 120, 280);
    }

    public void pintarGameOver() {

        graficos.setFill(Color.rgb(0, 0, 0, 0.82));
        graficos.fillRect(0, 0, ANCHO, ALTO);

        graficos.setFill(Color.RED);
        graficos.setFont(Font.font("Consolas", FontWeight.BOLD, 42));
        graficos.fillText("GAME OVER", 220, 220);

        graficos.setFill(Color.WHITE);
        graficos.setFont(Font.font("Consolas", 18));

        graficos.fillText("PUNTUACION: " + jugador.getPuntos(), 260, 265);
        graficos.fillText("R - REINTENTAR", 275, 310);
    }

    public void pintarVictoria() {

        graficos.setFill(Color.rgb(0, 0, 0, 0.68));
        graficos.fillRect(0, 0, ANCHO, ALTO);

        graficos.setFill(Color.CYAN);
        graficos.setFont(Font.font("Consolas", FontWeight.BOLD, 32));
        graficos.fillText("REFUGIO ALCANZADO", 180, 205);

        graficos.setFill(Color.WHITE);
        graficos.setFont(Font.font("Consolas", 18));

        graficos.fillText("La Formula de Salto Luminico esta a salvo.", 150, 250);
        graficos.fillText("Puntuacion final: " + jugador.getPuntos(), 245, 290);
        graficos.fillText("ENTER - MENU PRINCIPAL", 225, 340);
    }

    public void gestionEventos(Scene escena) {

        escena.setOnKeyPressed(evento -> {

            KeyCode tecla = evento.getCode();

            if (estado == EstadoJuego.MENU) {

                if (tecla == KeyCode.ENTER) {
                    iniciarNuevaPartida();
                }

                if (tecla == KeyCode.I) {
                    mostrarInstrucciones = !mostrarInstrucciones;
                }

                if (tecla == KeyCode.ESCAPE) {
                    System.exit(0);
                }

                return;
            }

            if (estado == EstadoJuego.GAME_OVER) {

                if (tecla == KeyCode.R) {
                    iniciarNuevaPartida();
                }

                return;
            }

            if (estado == EstadoJuego.VICTORIA) {

                if (tecla == KeyCode.ENTER) {
                    estado = EstadoJuego.MENU;
                }

                return;
            }

            if (estado != EstadoJuego.JUGANDO) {
                return;
            }

            if (tecla == KeyCode.RIGHT) {
                derecha = true;
            }

            if (tecla == KeyCode.LEFT) {
                izquierda = true;
            }

            if (tecla == KeyCode.UP) {
                arriba = true;
            }

            if (tecla == KeyCode.DOWN) {
                abajo = true;
            }

            if (tecla == KeyCode.SPACE) {
                disparando = true;
            }

            if (tecla == KeyCode.SHIFT) {
                jugador.activarTurbo(true);
            }
        });

        escena.setOnKeyReleased(evento -> {

            KeyCode tecla = evento.getCode();

            if (tecla == KeyCode.RIGHT) {
                derecha = false;
            }

            if (tecla == KeyCode.LEFT) {
                izquierda = false;
            }

            if (tecla == KeyCode.UP) {
                arriba = false;
            }

            if (tecla == KeyCode.DOWN) {
                abajo = false;
            }

            if (tecla == KeyCode.SPACE) {
                disparando = false;
            }

            if (tecla == KeyCode.SHIFT && jugador != null) {
                jugador.activarTurbo(false);
            }
        });
    }

    public void guardarRanking() {

        if (rankingGuardado) {
            return;
        }

        Ranking.guardar("JAX", jugador.getPuntos());

        rankingGuardado = true;
    }

    public void cargarImagenes() {

        cargar("nave", "nave.png");
        cargar("naveTurbo", "naveTurbo.png");
        cargar("explosion", "explosion.png");

        cargar("mapa1", "mapa1.png");
        cargar("mapa2", "mapa2.png");
        cargar("mapa3", "mapa3.png");
        cargar("mapa4", "mapa4.png");
        cargar("mapa5", "mapa5.png");

        cargar("jefe", "jefe.png");
        cargar("enemigoMini", "enemigoMini.png");

        cargar("balaJugador", "balaJugador.png");
        cargar("balaEnemigo", "balaEnemigo.png");

        cargar("asteroide1", "asteroide1.png");
        cargar("asteroide2", "asteroide2.png");
        cargar("asteroide3", "asteroide3.png");
        cargar("asteroideMagnetita", "asteroideMagnetita.png");

        cargar("aliado", "aliado.png");

        cargar("escudo", "escudo.png");
        cargar("kitReparacion", "kitReparacion.png");
        cargar("potenciadorDaño", "potenciadorDaño.png");
        cargar("celulaEnergia", "celulaEnergia.png");

        cargar("logo", "logo.png");
        cargar("fondoMenu", "fondoMenu.png");

        cargar("tali", "tali.png");
        cargar("vance", "vance.png");
        cargar("vondrak", "vondrak.png");

        cargar("marcoDialogo", "marcoDialogo.png");
        cargar("marcoAlerta", "marcoAlerta.png");
    }

    public void cargar(String clave, String archivo) {

        URL ruta = getClass().getResource("/imagenes/" + archivo);

        if (ruta == null) {
            ruta = getClass().getResource("/" + archivo);
        }

        if (ruta == null) {
            System.out.println("No se encontro: " + archivo);
            return;
        }

        imagenes.put(clave, new Image(ruta.toExternalForm()));

        System.out.println("Imagen cargada: " + archivo);
    }
}





























