package newController;

import com.opencsv.CSVReader;
import com.sun.istack.NotNull;
import database.OpenCSV;
import newModel.Player;
import org.hibernate.SessionFactory;
import profesor.model.Magazine;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PlayerController {
    private final Connection connection;
    private EntityManagerFactory entityManagerFactory;
    private final Scanner sc;
    private List<Field> fields;

    public PlayerController(Connection connection) {
        this.connection = connection;
        sc = new Scanner(System.in);
        fields = Arrays.stream(Player.class.getDeclaredFields()).toList();
    }

    public PlayerController(Connection connection, EntityManagerFactory entityManagerFactory) {
        this.connection = connection;
        this.entityManagerFactory = entityManagerFactory;
        sc = new Scanner(System.in);
        fields = Arrays.stream(Player.class.getDeclaredFields()).toList();
    }

    private int selectPlayer() throws NumberFormatException {
        System.out.println("What element do you want to select?");
        listPlayers();
        System.out.print("Selected id: ");
        return Integer.parseInt(sc.nextLine());
    }

    public void newPlayer() {
        Player player = new Player();
        for (int i = 1; i < fields.size(); i++) {
            try {
                System.out.print(fields.get(i).getName() + ": ");
                switch (fields.get(i).getName()) {
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
                    case "expCareer" -> player.setExpCareer(Integer.parseInt(sc.nextLine()));
                }
            } catch (NumberFormatException | ParseException e) {
                System.out.println("*** Error, bad value or format. Try Again ***");
                i--;
            }
        }
        addPlayer(player);
    }

    public void updatePlayer() {
        boolean rep;
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        do {
            try {
                rep = false;
                System.out.println(" -- Do you want to filter (Y/N) ? --");
                String value = sc.nextLine();
                if (value.equalsIgnoreCase("Y")) {
                    for (Player p : getCriteriaList(em)) {
                        System.out.println(" Updating -- " + p.toString());
                        updateFieldForm(p);
                        em.merge(p);
                        System.out.println(" -- Player Updated -- ");
                    }
                } else if (value.equalsIgnoreCase("N")) {
                    Player player = em.find(Player.class, selectPlayer());
                    System.out.println(" Updating -- " + player.toString());
                    updateFieldForm(player);
                    em.merge(player);
                    System.out.println(" -- Player Updated -- ");
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException | ParseException e) {
                System.out.println("*** Error, bad value or format. Try Again ***");
                rep = true;
            }
        } while (rep);

        em.getTransaction().commit();
        em.close();
    }

    public void deletePlayer() {
        EntityManager em = entityManagerFactory.createEntityManager();

        String value;
        em.getTransaction().begin();
        System.out.println(" *** DELETE ***");
        try {
            System.out.println(" -- Do you want to filter (Y/N) ? --");
            value = sc.nextLine();
            if (value.equalsIgnoreCase("Y")) {
                for (Player p : getCriteriaList(em)) {
                    em.remove(p);
                    System.out.println(" -- " + p.toString());
                    System.out.println(" -- Player Deleted -- ");
                }
            } else if (value.equalsIgnoreCase("N")) {
                Player player = em.find(Player.class, selectPlayer());
                em.remove(player);
                System.out.println(" -- " + player.toString());
                System.out.println(" -- Player Deleted -- ");
            } else {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException | ParseException e) {
            System.out.println("*** Error, bad value or format.***");
            System.out.println(" ** Exiting **");
        }
        em.getTransaction().commit();
        em.close();
    }

    public void updateFieldForm(Player player){
        boolean rep;
        do {
            rep = false;
            System.out.println("What attribute do you want to update?");
            for (int i = 1; i < fields.size(); i++) {
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
                        case "expCareer" -> player.setExpCareer(Integer.parseInt(sc.nextLine()));
                    }
                    System.out.println("Do you want to update anything else? (Y/N)");
                    if (sc.nextLine().equalsIgnoreCase("Y")) rep = true;
                }
            } catch (NumberFormatException | ParseException e) {
                System.out.println("*** Error, bad value or format. Try Again ***");
                rep = true;
            }
        } while (rep);
    }

    private List<Player> getCriteriaList(EntityManager em) throws ParseException {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Player> q = cb.createQuery(Player.class);
        Root<Player> c = q.from(Player.class);
        q.select(c);

        String option;

        listPlayers();

        System.out.println(" -- What attribute do you want to filter for? --");
        for (int i = 1; i < fields.size(); i++) {
            System.out.println((i) + ": " + fields.get(i).getName());
        }
        System.out.print("Selected: ");
        Field filter = fields.get(Integer.parseInt(sc.nextLine()));
        do {
            System.out.println("*** What type of criteria would you use (<, >, = , !=) ? ***");
            System.out.println("*** Be consequence of the type of data are you trying to filter. ***");
            option = sc.nextLine();
        } while (!option.equalsIgnoreCase(">") && !option.equalsIgnoreCase("<") && !option.equalsIgnoreCase("=") && !option.equalsIgnoreCase("!="));

        System.out.print("Define the value to compare " + filter.getName() + " : ");
        switch (filter.getName()) {
            case "name", "draftTeam", "college", "position" -> {
                if (option.equalsIgnoreCase("=")) {
                    q.where(
                            cb.equal(c.get(filter.getName()), sc.nextLine())
                    );
                } else {
                    q.where(
                            cb.notEqual(c.get(filter.getName()), sc.nextLine())
                    );
                }
            }
            case "age", "draftPos", "draftYear", "expCareer" -> {

                if (option.equalsIgnoreCase("=")) {
                    q.where(
                            cb.equal(c.get(filter.getName()), Integer.parseInt(sc.nextLine()))
                    );
                } else if (option.equalsIgnoreCase("!=")) {
                    q.where(
                            cb.notEqual(c.get(filter.getName()), sc.nextLine())
                    );
                } else if (option.equalsIgnoreCase(">")) {
                    q.where(
                            cb.greaterThan(c.get(filter.getName()), Integer.parseInt(sc.nextLine()))
                    );
                } else if (option.equalsIgnoreCase("<")) {
                    q.where(
                            cb.lessThan(c.get(filter.getName()), Integer.parseInt(sc.nextLine()))
                    );
                }
            }
            default -> System.out.println(" -- We can't filter for that field... --");
        }

        return em.createQuery(q).getResultList();
    }

    public List<Player> readPlayerFile(String path) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<Player> playerList = new ArrayList();
        boolean first = false;
        for (String[] data : OpenCSV.readCSV(path)) {
            try {
                if (!first) first = true;
                else
                    playerList.add(new Player(data[0], data[2], data[3], data[4], Integer.parseInt(data[5]), dateFormat.parse(data[7]), Integer.parseInt(data[1]), Integer.parseInt(data[6]), Integer.parseInt(data[8])));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return playerList;
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
                .getResultList().stream().sorted(Comparator.comparingInt(Player::getPlayerId)).toList();
        for (Player player : result) {
            System.out.println(player.toString());
        }
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
