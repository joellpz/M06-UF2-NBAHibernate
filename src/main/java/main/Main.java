package main;

import database.Additions;
import database.ConnectionFactory;
import controller.PlayerController;
import controller.PlayerSeasonController;
import controller.SeasonController;
import controller.TeamController;
import model.Player;
import model.PlayerSeason;
import model.Season;
import model.Team;
import view.Menu;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Connection;
import java.text.ParseException;
import java.util.Scanner;

//TODO Los pasos a seguir son los siguientes:

//TO DO  Definir el esquema de la base de datos. Al menos debe existir una relación uno-ene, o una ene-ene.
//TO DO  Creación de una base de datos. (Esta opción puede realizarse directamente en PostgreSQL). El resto de opciones deberían realizarse mediante un menú de terminal específicamente creado para nuestra práctica.
//TO DO  Definir las sentencias de creación de las tablas que guardarán la información. El proyecto deberá incluir un archivo schema.sql con las sentencias de creación de la base de datos.
//TO DO  Conexión con la base de datos mediante una unidad de persistencia definida en el archivo persistence.xml.
//TODO  Manejo de la conexión mediante un menú de terminal que debe tener:
//TO DO  Una opción que permita borrar las tablas de la base de datos y su información.
//TO DO  Una opción que permita crear las tablas de la base de datos.
//TO DO  Una opción que permita poblar masivamente las tablas de la base de datos leyendo los ficheros generados en la primera práctica.
//TODO  Diferentes opciones de consulta sobre la información. Ejemplos:
//TO DO  Seleccionar todos los elementos que contengan un texto concreto.
//TO DO  Seleccionar todos los elementos que cumplan una condición.
//TO DO  Seleccionar elementos concretos.
//TODO  Posibilidad de modificar un registro concreto de información. Ejemplo:
//TODO  Seleccionar un elemento concreto y permitir su modificación.
//TODO  Posibilidad de modificar diferentes registros de información.
//TODO  Posibilidad de eliminar un registro concreto de información.
//TODO  Posibilidad de eliminar un conjunto de registros de información que cumplan un condición.


public class Main {

    public static void main(String[] args) throws ParseException {
//    ArrayList<Magazine> revistes = new ArrayList();

        Scanner sc = new Scanner(System.in);
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection c = connectionFactory.connect();
        EntityManagerFactory entityManagerFactory = createEntityManagerFactory();


        PlayerController playerController = new PlayerController(c, entityManagerFactory);
        TeamController teamController = new TeamController(c, entityManagerFactory);
        SeasonController seasonController = new SeasonController(c, entityManagerFactory);
        PlayerSeasonController playerSeasonController = new PlayerSeasonController(c, entityManagerFactory);

//        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD");
//        Player player1 = new Player("Joel1", "position", "college", "draftTeam", 5 , dateFormat.parse("1990-12-12"),25, 1990, 5);
//        playerController.addPlayer(player1);
//        playerController.addPlayer(new Player("Joel2", "position", "college", "draftTeam", 5 , dateFormat.parse("1990-12-12"),25, 1990, 5));
//        playerController.listPlayers();

//        player1.setName("NEWJOEL");
//        System.out.println(player1.getPlayerId()+"+++");
//        playerController.updatePlayer(player1.getPlayerId());
//        playerController.listPlayers();
//        playerController.deletePlayer();
//        playerController.listPlayers();
//        playerController.listPlayers();
//        playerController.deletePlayer(1);
//        playerController.listPlayers();

        //playerController.clearPlayers();

        Menu menu = new Menu();
        int option = menu.mainMenu();
        while (option >= 0 && option < 6) {
            switch (option) {
                case 1 -> {
                    //Show Tables
                    switch (menu.dataMenu()) {
                        case 1 -> playerController.listPlayers();
                        case 2 -> teamController.listTeams();
                        case 3 -> seasonController.listSeasons();
                        case 4 -> playerSeasonController.listPlayerSeasons();
                    }
                }
                case 2 -> {
                    //Insert Data
                    switch (menu.dataMenu()) {
                        case 1 -> playerController.newPlayer();
                        case 2 -> teamController.newTeam();
                        case 3 -> seasonController.newSeason();
                        case 4 -> playerSeasonController.newPlayerSeason();
                    }
                }
                case 3 -> {
                    //Update Data
                    switch (menu.dataMenu()) {
                        case 1 -> playerController.updatePlayer();
                        case 2 -> teamController.updateTeam();
                        case 3 -> seasonController.updateSeason();
                        case 4 -> playerSeasonController.updatePlayerSeason();
                    }
                }
                case 4 -> {
                    //Delete data
                    switch (menu.dataMenu()) {
                        case 1 -> playerController.deletePlayer();
                        case 2 -> teamController.deleteTeam();
                        case 3 -> seasonController.deleteSeason();
                        case 4 -> playerSeasonController.deletePlayerSeason();

                    }
                }
                case 5 -> {
                    Additions.resetDB(c, "src/main/resources/edr/postgres-script-nba.sql");
                    System.out.println(" ** Do you want to restore data from Source Files (Y/N)? **");
                    if (sc.nextLine().equalsIgnoreCase("Y")) {
                        for (Player p : playerController.readPlayerFile("src/main/resources/csv/players.csv")) {
                            System.out.println(p);
                            playerController.addPlayer(p);
                        }

                        for (Team t : teamController.readTeamFile("src/main/resources/csv/teams.csv")) {
                            System.out.println(t);
                            teamController.addTeam(t);
                        }

                        for (Season s : seasonController.readSeasonFile("src/main/resources/csv/seasons.csv")) {
                            System.out.println(s);
                            seasonController.addSeason(s);
                        }

                        for (PlayerSeason ps : playerSeasonController.readPlayerSeasonFile("src/main/resources/csv/playerseasons.csv")) {
                            System.out.println(ps);
                            playerSeasonController.addPlayerSeason(ps);
                        }

                    }
                }
                case 0 -> System.exit(0);

                default -> System.out.println("*** Introduce un valor válido! ***");
            }
            option = menu.mainMenu();
        }


    }

    static SessionFactory sessionFactoryObj;

/*
  private static SessionFactory buildSessionFactory() {
    // Creating Configuration Instance & Passing Hibernate Configuration File
    Configuration configObj = new Configuration();
    configObj.configure("hibernate.cfg.xml");

    // Since Hibernate Version 4.x, ServiceRegistry Is Being Used
    ServiceRegistry serviceRegistryObj = new StandardServiceRegistryBuilder().applySettings(configObj.getProperties()).build();

    // Creating Hibernate SessionFactory Instance
    sessionFactoryObj = configObj.buildSessionFactory(serviceRegistryObj);
    return sessionFactoryObj;
  } */

    private static SessionFactory buildSessionFactory() {
        try {
            StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                    .configure("hibernate.cfg.xml").build();
            Metadata metadata = new MetadataSources(standardRegistry).getMetadataBuilder().build();
            return metadata.getSessionFactoryBuilder().build();

        } catch (HibernateException he) {
            System.out.println("Session Factory creation failure");
            throw he;
        }
    }

    public static EntityManagerFactory createEntityManagerFactory() {
        EntityManagerFactory emf;
        try {
            emf = Persistence.createEntityManagerFactory("NBA");
        } catch (Throwable ex) {
            System.err.println("Failed to create EntityManagerFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        return emf;
    }
}
