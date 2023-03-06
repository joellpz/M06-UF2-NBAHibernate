package newController;

import newModel.Player;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class PlayerController {
    private final Connection connection;
    private EntityManagerFactory entityManagerFactory;
    private final Scanner sc;

    public PlayerController(Connection connection) {
        this.connection = connection;
        sc = new Scanner(System.in);
    }

    public PlayerController(Connection connection, EntityManagerFactory entityManagerFactory) {
        this.connection = connection;
        this.entityManagerFactory = entityManagerFactory;
        sc = new Scanner(System.in);
    }

    public void addPlayer(Player player) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Player playerExists = em.find(Player.class, player.getPlayerId());
        if (playerExists == null) {
            em.persist(player);
            System.out.println("*** Player Inserted ***");
        }
        em.getTransaction().commit();
        em.close();
    }

    public void listPlayers() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        List<Player> result = em.createQuery("from Player", Player.class)
                .getResultList();
        for (Player player : result) {
            System.out.println(player.toString());
        }
        em.getTransaction().commit();
        em.close();
    }

    public void updatePlayer(int playerId) {
        boolean rep;
        List<Field> fields = Arrays.stream(Player.class.getDeclaredFields()).toList();
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Player player = em.find(Player.class, playerId);
//        player.setName("NEW JOEL");
        do {
            rep = false;
            System.out.println("What attribute do you want to update?");
            for (int i = 1; i < Player.class.getDeclaredFields().length; i++) {
                System.out.println((i) + ": " + fields.get(i).getName());
            }
            System.out.println("0: Exit");
            System.out.print("Selected: ");
            try {
                int opt = Integer.parseInt(sc.nextLine());

                if (opt < 0 || opt > fields.size() - 1) throw new NumberFormatException();
                else if (opt != 0) {
                    System.out.print("New " + fields.get(opt).getName() + " value: ");
                    switch (fields.get(opt).getName()) {
                        case "name" -> player.setName(sc.nextLine());
                        case "age" -> player.setAge(Integer.parseInt(sc.nextLine()));
                        case "position" -> player.setPosition(sc.nextLine());
                        case "college" -> player.setCollege(sc.nextLine());
                        case "draftTeam" -> player.setDraftTeam(sc.nextLine());
                        case "draftPos" -> player.setDraftPos(Integer.parseInt(sc.nextLine()));
                        case "draftYear" -> player.setDraftYear(Integer.parseInt(sc.nextLine()));
                        case "born" -> {
                            System.out.println(" -- Format: yyyy-MM-dd -- ");
                            player.setBorn(new SimpleDateFormat("yyyy-MM-dd").parse(sc.nextLine()));
                        }
                    }
                    System.out.println("Do you want to update anything else? (Y/N)");
                    if (sc.nextLine().equalsIgnoreCase("Y")) rep = true;
                }
            } catch (NumberFormatException | ParseException e) {
                System.out.println("*** Error, bad value or format. Try Again ***");
                rep = true;
            }

        } while (rep);


        //TODO Poner cosas para Update
        em.merge(player);
        em.getTransaction().commit();
        em.close();
    }

    public void deletePlayer(Integer playerId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Player player = em.find(Player.class, playerId);
        em.remove(player);
        em.getTransaction().commit();
        em.close();
    }

    public void deletePlayer() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        System.out.println("What element do you want to DELETE?");
        listPlayers();
        System.out.print("Selected: ");
        try {
            Player player = em.find(Player.class, Integer.parseInt(sc.nextLine()));
            em.remove(player);
            em.getTransaction().commit();
            System.out.println(" -- " + player.toString());
            System.out.println(" -- Player Deleted -- ");
        } catch (NumberFormatException e) {
            System.out.println("*** Error, bad value or format.***");
            System.out.println(" ** Exiting **");
        }
        em.close();
    }

    public void clearPlayers() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        List<Player> result = em.createQuery("from Player", Player.class)
                .getResultList();
        for (Player player : result) {
            deletePlayer(player.getPlayerId());
        }
        em.getTransaction().commit();
        em.close();
    }

}
