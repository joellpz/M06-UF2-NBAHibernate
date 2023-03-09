package view;

import java.util.Scanner;

public class Menu {
    private final Scanner br = new Scanner(System.in);
    public int mainMenu() {
        int option = 0;
        do {
            System.out.println(" \nMENU PRINCIPAL \n");

            System.out.println("1. Mostrar tabla");
            System.out.println("2. Introducir Datos");
            System.out.println("3. Actualizar Datos");
            System.out.println("4. Eliminar Datos");
            System.out.println("5. Borrar y Generar Tablas.");
            System.out.println("0. Salir");
            System.out.println("Escoger opción: ");
            try {
                option = Integer.parseInt(br.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("valor no válido");
                option = -5;
            }
        } while (option < 0 || option > 5);

        return option;
    }

    /**
     * Data Menu
     *
     * @return int
     */
    public int dataMenu() {
        int option = 0;
        do {
            System.out.println(" \n*** Trabajar los Datos, escoge la tabla: *** \n");
            System.out.println("1. Jugadores");
            System.out.println("2. Equipos");
            System.out.println("3. Temporadas");
            System.out.println("4. Jugadores Por Temporada");
            System.out.println("0. Atrás");
            System.out.println("Escoger opción: ");
            try {
                option = Integer.parseInt(br.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("valor no válido");
                option = -5;
            }
        } while (option < 0 || option > 4);
        return option;
    }
}
