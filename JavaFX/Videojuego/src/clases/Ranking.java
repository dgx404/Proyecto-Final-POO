package clases;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Ranking {

    private static final String ARCHIVO = "ranking.txt";

    public static void guardar(String nombre, int puntos) {

        List<String> registros = new ArrayList<String>();
        File archivo = new File(ARCHIVO);

        if (archivo.exists()) {
            try {
                BufferedReader lector = new BufferedReader(new FileReader(archivo));
                String linea;

                while ((linea = lector.readLine()) != null) {
                    registros.add(linea);
                }

                lector.close();

            } catch (IOException e) {
                System.out.println("Error al leer el ranking.");
                e.printStackTrace();
            }
        }

        registros.add(nombre + ";" + puntos);

        registros.sort((a, b) -> {
            int puntosA = Integer.parseInt(a.split(";")[1]);
            int puntosB = Integer.parseInt(b.split(";")[1]);

            return Integer.compare(puntosB, puntosA);
        });

        if (registros.size() > 10) {
            registros = new ArrayList<String>(registros.subList(0, 10));
        }

        try {
            PrintWriter escritor = new PrintWriter(new FileWriter(archivo));

            for (String registro : registros) {
                escritor.println(registro);
            }

            escritor.close();

        } catch (IOException e) {
            System.out.println("Error al guardar el ranking.");
            e.printStackTrace();
        }
    }
}











