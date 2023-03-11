package controller;

import database.OpenCSV;
import model.PlayerSeason;
import model.PlayerSeasonId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Class that have all the methods to control a PlayerSeason Object from the Database.
 */
public class PlayerSeasonController {
    /**
     * Connection Object that has all the Database connection.
     */
    private final Connection connection;
    /**
     * Entity Manager Factory
     */
    private EntityManagerFactory entityManagerFactory;
    /**
     * Scanner Object to get Terminal Input
     */
    private final Scanner sc;
    /**
     * Field List from the Teams Object
     */
    private final List<Field> fields;
    private PlayerController playerController;
    private TeamController teamController;
    private SeasonController seasonController;

    /**
     * Constructor for PlayerSeasonController
     *
     * @param connection connection to the database
     */
    public PlayerSeasonController(Connection connection) {
        this.connection = connection;
        sc = new Scanner(System.in);
        fields = Arrays.stream(PlayerSeason.class.getDeclaredFields()).toList();
        playerController = new PlayerController(connection, entityManagerFactory);
        teamController = new TeamController(connection, entityManagerFactory);
        seasonController = new SeasonController(connection, entityManagerFactory);
    }


    /**
     * Constructor for PlayerSeasonController
     *
     * @param connection           connection to the database
     * @param entityManagerFactory entity Manager for Model Objects
     */
    public PlayerSeasonController(Connection connection, EntityManagerFactory entityManagerFactory) {
        this.connection = connection;
        this.entityManagerFactory = entityManagerFactory;
        sc = new Scanner(System.in);
        fields = Arrays.stream(PlayerSeason.class.getDeclaredFields()).toList();
        playerController = new PlayerController(connection, entityManagerFactory);
        teamController = new TeamController(connection, entityManagerFactory);
        seasonController = new SeasonController(connection, entityManagerFactory);
    }

    /**
     * Shows all the PlayerSeasons and let you introduce 1 id.
     *
     * @return PlayerSeason ID
     * @throws NumberFormatException when no number character is introduced.
     */
    private int selectPlayerSeason() throws NumberFormatException {
        System.out.println("What element do you want to select?");
        listPlayerSeasons();
        System.out.print("Selected id: ");
        return Integer.parseInt(sc.nextLine());
    }

    /**
     * Form to introduce new PlayerSeason
     */
    public void newPlayerSeason() {
        PlayerSeason playerSeason = new PlayerSeason();
        for (int i = 0; i < fields.size(); i++) {
            try {
                System.out.print(fields.get(i).getName() + ": ");
                setter4Fields(playerSeason, i);
            } catch (Exception e) {
                System.out.println("*** Error, bad value or format. Try Again ***");
                i--;
            }
        }
        addPlayerSeason(playerSeason);
    }

    /**
     * Update PlayerSeason information and let you decide if you want to filtes some values
     */
    public void updatePlayerSeason() {
        boolean rep;
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        do {
            try {
                rep = false;
                List<PlayerSeason> playerSeasons = getCriteriaList(em);
                if (playerSeasons != null && playerSeasons.size() != 0) {
                    for (PlayerSeason p : playerSeasons) {
                        System.out.println(" Updating -- " + p.toString());
                        updateFieldForm(p);
                        em.merge(p);
                        System.out.println(" -- PlayerSeason Updated -- ");
                    }
                } else {
                    System.out.println(" -- No value founded... --");
                }

            } catch (Exception e) {
                System.out.println("*** Error, bad value or format. Try Again ***");
                rep = true;
            }
        } while (rep);

        em.getTransaction().commit();
        em.close();
    }

    /**
     * Delete PlayerSeason from Database
     */
    public void deletePlayerSeason() {
        EntityManager em = entityManagerFactory.createEntityManager();

        em.getTransaction().begin();
        System.out.println(" *** DELETE ***");
        try {
            System.out.println(" -- Apply Filter --");
            List<PlayerSeason> playerSeasons = getCriteriaList(em);
            if (playerSeasons != null && playerSeasons.size() != 0) {
                for (PlayerSeason p : playerSeasons) {
                    em.remove(p);
                    System.out.println(" -- " + p.toString());
                    System.out.println(" -- PlayerSeason Deleted -- ");
                }
            } else {
                System.out.println(" -- No value founded... --");
            }

        } catch (Exception e) {
            System.out.println("*** Error, bad value or format.***");
            System.out.println(" ** Exiting **");
        }
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Shows you all the Fields of the Class and let you decide what values you want to change.
     *
     * @param playerSeason PlayerSeason to update
     */
    public void updateFieldForm(PlayerSeason playerSeason) {
        boolean rep;
        do {
            rep = false;
            System.out.println("What attribute do you want to update?");
            for (int i = 0; i < fields.size(); i++) {
                if (i == 0) {
                    System.out.println("0. idPlayer / idSeason / idTeam");
                } else System.out.println((i) + ": " + fields.get(i).getName());
            }
            System.out.println("-1: Exit");
            System.out.print("Selected: ");
            try {
                int opt = Integer.parseInt(sc.nextLine());

                if (opt < -1 || opt > fields.size() - 1) throw new NumberFormatException();
                else if (opt != -1) {
                    System.out.print("New " + fields.get(opt).getName() + " value: ");
                    setter4Fields(playerSeason, opt);
                    System.out.println("Do you want to update anything else? (Y/N)");
                    if (sc.nextLine().equalsIgnoreCase("Y")) rep = true;
                }
            } catch (Exception e) {
                System.out.println("*** Error, bad value or format. Try Again ***");
                rep = true;
            }
        } while (rep);
    }


    /**
     * Method to get the List of PlayerSeasons that agree with the Restrictions you define.
     *
     * @param em Entity Manager to make the Criteria
     * @return List of PlayerSeasons that agree with the criteria
     */
    private @Nullable List<PlayerSeason> getCriteriaList(@NotNull EntityManager em) {
        boolean rep;
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PlayerSeason> q = cb.createQuery(PlayerSeason.class);
        Root<PlayerSeason> c = q.from(PlayerSeason.class);
        q.select(c);

        String option;
        int opt = 0, id = 0;

        listPlayerSeasons();

        System.out.println(" -- What attribute do you want to filter for? --");
        for (int i = 0; i < fields.size(); i++) {
            if (i == 0) {
                System.out.println("0. idPlayer / idSeason / idTeam");
            } else System.out.println((i) + ": " + fields.get(i).getName());
        }
        System.out.print("Selected: ");

        Field filter = fields.get(Integer.parseInt(sc.nextLine()));
        if (filter.getName().equalsIgnoreCase("playerSeasonId")) {
            do {
                System.out.println("1. Filter Player");
                System.out.println("2. Filter Season");
                System.out.println("3. Filter Team");
                opt = Integer.parseInt(sc.nextLine());
                switch (opt) {
                    case 1 -> id = playerController.getPlayer().getPlayerId();
                    case 2 -> id = seasonController.getSeason().getSeasonId();
                    case 3 -> id = teamController.getTeam().getTeamId();
                    default -> System.out.println(" -- Error, try another value. --");
                }
            } while (opt != 1 && opt != 2 && opt != 3);
        }
        do {
            rep = false;
            System.out.println("*** What type of criteria would you use (<, >, = , !=) ? ***");
            System.out.println("*** Be consequence of the type of data are you trying to filter. ***");
            option = sc.nextLine();
            if (!option.equalsIgnoreCase(">") && !option.equalsIgnoreCase("<")
                    && !option.equalsIgnoreCase("=") && !option.equalsIgnoreCase("!="))
                rep = true;
        } while (rep);

        System.out.print("Define the value to compare in " + filter.getName() + " : ");
        do {
            rep = false;
            try {
                if (filter.getGenericType().toString().equals("class java.lang.String")) {
                    if (option.equalsIgnoreCase("=")) {
                        q.where(
                                cb.equal(c.get(filter.getName()), sc.nextLine())
                        );
                    } else {
                        q.where(
                                cb.notEqual(c.get(filter.getName()), sc.nextLine())
                        );
                    }
                } else if (filter.getGenericType().toString().equals("int") || filter.getGenericType().toString().equals("float")) {
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
                } else if (filter.getGenericType().toString().equals("class model.PlayerSeasonId")) {
                    if (option.equalsIgnoreCase("=")) {
                        if (opt == 1) {
                            q.where(
                                    cb.equal(c.get(filter.getName()).get("idPlayer"), id)
                            );
                        } else if (opt == 2) {
                            q.where(
                                    cb.equal(c.get(filter.getName()).get("idSeason"), id)
                            );
                        } else if (opt == 3) {
                            q.where(
                                    cb.equal(c.get(filter.getName()).get("idTeam"), id)
                            );
                        }
                    }
                } else {
                    System.out.println(" -- We can't filter for that field... --");
                    return null;
                }
            } catch (Exception e) {
                System.out.println("*** Error, bad value or format. Try Again ***");
                rep = true;
            }
        } while (rep);
        return em.createQuery(q).getResultList();
    }


    /**
     * Switch that filter for what setter to apply depending on the position of Class Fields List
     *
     * @param playerSeason PlayerSeason Object to set new Values
     * @param pos          postion of field in Fields List
     */
    private void setter4Fields(PlayerSeason playerSeason, int pos) {
        System.out.println(fields.get(pos).getName());
        switch (fields.get(pos).getName()) {
            case "playerSeasonId" ->
                    playerSeason.setPlayerSeasonId(new PlayerSeasonId(playerController.getPlayer(), seasonController.getSeason(), teamController.getTeam()));
            case "age" -> playerSeason.setAge(Integer.parseInt(sc.nextLine()));
            case "league" -> playerSeason.setLeague(sc.nextLine());
            case "position" -> playerSeason.setPosition(sc.nextLine());
            case "games" -> playerSeason.setGames(Integer.parseInt(sc.nextLine()));
            case "gamesStarter" -> playerSeason.setGamesStarter(Integer.parseInt(sc.nextLine()));
            case "minutesPlayed" -> playerSeason.setMinutesPlayed(Integer.parseInt(sc.nextLine()));
            case "fieldGoals" -> playerSeason.setFieldGoals(Integer.parseInt(sc.nextLine()));
            case "fgAttempts" -> playerSeason.setFgAttempts(Integer.parseInt(sc.nextLine()));
            case "fgPerc" -> playerSeason.setFgPerc(Integer.parseInt(sc.nextLine()));
            case "fgPts3" -> playerSeason.setFgPts3(Integer.parseInt(sc.nextLine()));
            case "pfgAttempts3" -> playerSeason.setPfgAttempts3(Integer.parseInt(sc.nextLine()));
            case "pfgPerc3" -> playerSeason.setPfgPerc3(Float.parseFloat(sc.nextLine()));
            case "fgPts2" -> playerSeason.setFgPts2(Integer.parseInt(sc.nextLine()));
            case "pfgAttempts2" -> playerSeason.setPfgAttempts2(Integer.parseInt(sc.nextLine()));
            case "pfgPerc2" -> playerSeason.setPfgPerc2(Float.parseFloat(sc.nextLine()));
            case "effGoalPerc" -> playerSeason.setEffGoalPerc(Float.parseFloat(sc.nextLine()));
            case "freeThrows" -> playerSeason.setFreeThrows(Integer.parseInt(sc.nextLine()));
            case "freetAttempts" -> playerSeason.setFreetAttempts(Integer.parseInt(sc.nextLine()));
            case "ftPerc" -> playerSeason.setFtPerc(Float.parseFloat(sc.nextLine()));
            case "offRbds" -> playerSeason.setOffRbds(Integer.parseInt(sc.nextLine()));
            case "defRbds" -> playerSeason.setDefRbds(Integer.parseInt(sc.nextLine()));
            case "totalRbds" -> playerSeason.setTotalRbds(Integer.parseInt(sc.nextLine()));
            case "assists" -> playerSeason.setAssists(Integer.parseInt(sc.nextLine()));
            case "steals" -> playerSeason.setSteals(Integer.parseInt(sc.nextLine()));
            case "blocks" -> playerSeason.setBlocks(Integer.parseInt(sc.nextLine()));
            case "turnovers" -> playerSeason.setTurnovers(Integer.parseInt(sc.nextLine()));
            case "fouls" -> playerSeason.setFouls(Integer.parseInt(sc.nextLine()));
            case "points" -> playerSeason.setPoints(Integer.parseInt(sc.nextLine()));
            case "tripleDouble" -> playerSeason.setTripleDouble(Integer.parseInt(sc.nextLine()));
        }
    }

    /**
     * Method transform the data from a CSV into Objects
     *
     * @param path path of the csv
     * @return List of PlayerSeasons imported from the csv
     */
    public List<PlayerSeason> readPlayerSeasonFile(String path) {
        List<PlayerSeason> PlayerSeasonList = new ArrayList<>();
        boolean first = false;
        for (String[] data : OpenCSV.readCSV(path)) {
            try {
                if (!first) first = true;
                else
                    PlayerSeasonList.add(new PlayerSeason(new PlayerSeasonId(playerController.getPlayer(Integer.parseInt(data[0])), seasonController.getSeason(Integer.parseInt(data[1])), teamController.getTeam(Integer.parseInt(data[2]))),
                            Integer.parseInt(data[3]), data[4], data[5], Integer.parseInt(data[6]), Integer.parseInt(data[7]), Integer.parseInt(data[8]),
                            Integer.parseInt(data[9]), Integer.parseInt(data[10]), Float.parseFloat(data[11]), Integer.parseInt(data[12]), Integer.parseInt(data[13]),
                            Float.parseFloat(data[14]), Integer.parseInt(data[15]), Integer.parseInt(data[16]), Float.parseFloat(data[17]), Float.parseFloat(data[18]),
                            Integer.parseInt(data[19]), Integer.parseInt(data[20]), Float.parseFloat(data[21]), Integer.parseInt(data[22]), Integer.parseInt(data[23]),
                            Integer.parseInt(data[24]), Integer.parseInt(data[25]), Integer.parseInt(data[26]), Integer.parseInt(data[27]), Integer.parseInt(data[28]),
                            Integer.parseInt(data[29]), Integer.parseInt(data[30]), Integer.parseInt(data[31])));
            } catch (Exception e) {
                System.out.println("*** Error, bad value or format.***");
            }
        }
        return PlayerSeasonList;
    }


    /**
     * Method to add a PlayerSeason into the Database
     *
     * @param playerSeason PlayerSeason to add
     */
    public void addPlayerSeason(@NotNull PlayerSeason playerSeason) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        PlayerSeason PlayerSeasonExists = em.find(PlayerSeason.class, playerSeason.getPlayerSeasonId());
        if (PlayerSeasonExists == null) {
            em.persist(playerSeason);
            System.out.println("*** PlayerSeason Inserted ***");
        }
        em.getTransaction().commit();
        em.close();
    }


    /**
     * Lists all the PlayerSeasons that are in the Database
     */
    public void listPlayerSeasons() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        List<PlayerSeason> result = em.createQuery("from PlayerSeason", PlayerSeason.class)
                .getResultList().stream().sorted(Comparator.comparingInt(o -> o.getPlayerSeasonId().getIdPlayer().getPlayerId())).toList();
        for (PlayerSeason PlayerSeason : result) {
            System.out.println(PlayerSeason.toString());
        }
        em.getTransaction().commit();
        em.close();
    }


    /**
     * Delete a PlayerSeason from the ID.
     *
     * @param playerSeasonId PlayerSeason id
     */
    public void deletePlayerSeason(PlayerSeasonId playerSeasonId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        PlayerSeason PlayerSeason = em.find(PlayerSeason.class, playerSeasonId);
        em.remove(PlayerSeason);
        em.getTransaction().commit();
        em.close();
    }


    /**
     * Drops all the PlayerSeasons from the Database.
     */
    public void clearPlayerSeasons() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        List<PlayerSeason> result = em.createQuery("from PlayerSeason", PlayerSeason.class)
                .getResultList();
        for (PlayerSeason PlayerSeason : result) {
            deletePlayerSeason(PlayerSeason.getPlayerSeasonId());
        }
        em.getTransaction().commit();
        em.close();
    }

}
