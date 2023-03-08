package newController;

import database.OpenCSV;
import newModel.Season;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.*;


/**
 * Class that have all the methods to control a Season Object from the Database.
 */
public class SeasonController {
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
     * Field List from the Seasons Object
     */
    private final List<Field> fields;
    private final TeamController teamController;

    /**
     * Constructor for SeasonController
     *
     * @param connection connection to the database
     */
    public SeasonController(Connection connection) {
        this.connection = connection;
        sc = new Scanner(System.in);
        fields = Arrays.stream(Season.class.getDeclaredFields()).toList();
        teamController = new TeamController(connection);
    }

    /**
     * Constructor for SeasonController
     *
     * @param connection           connection to the database
     * @param entityManagerFactory entity Manager for Model Objects
     */

    public SeasonController(Connection connection, EntityManagerFactory entityManagerFactory) {
        this.connection = connection;
        this.entityManagerFactory = entityManagerFactory;
        sc = new Scanner(System.in);
        fields = Arrays.stream(Season.class.getDeclaredFields()).toList();
        teamController = new TeamController(connection, entityManagerFactory);
    }

    /**
     * Shows all the Seasons and let you introduce 1 id.
     *
     * @return Season ID
     * @throws NumberFormatException when no number character is introduced.
     */
    private int selectSeason() throws NumberFormatException {
        System.out.println("What element do you want to select?");
        listSeasons();
        System.out.print("Selected id: ");
        return Integer.parseInt(sc.nextLine());
    }

    /**
     * Form to introduce new Season
     */
    public void newSeason() {
        Season season = new Season();
        for (int i = 1; i < fields.size(); i++) {
            try {
                System.out.print(fields.get(i).getName() + ": ");
                setter4Fields(season, i);
            } catch (NumberFormatException e) {
                System.out.println("*** Error, bad value or format. Try Again ***");
                i--;
            }
        }
        addSeason(season);
    }


    /**
     * Update Season information and let you decide if you want to filtes some values
     */
    public void updateSeason() {
        boolean rep;
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        do {
            try {
                rep = false;
                System.out.println(" -- Do you want to filter (Y/N) ? --");
                String value = sc.nextLine();
                if (value.equalsIgnoreCase("Y")) {
                    List<Season> seasons = getCriteriaList(em);
                    if (seasons != null && seasons.size() != 0) {
                        for (Season s : seasons) {
                            System.out.println(" Updating -- " + s.toString());
                            updateFieldForm(s);
                            em.merge(s);
                            System.out.println(" -- Season Updated -- ");
                        }
                    } else {
                        System.out.println(" -- No value founded... --");
                    }
                } else if (value.equalsIgnoreCase("N")) {
                    Season season = em.find(Season.class, selectSeason());
                    if (season != null) {
                        System.out.println(" Updating -- " + season);
                        updateFieldForm(season);
                        em.merge(season);
                        System.out.println(" -- Season Updated -- ");
                    } else {
                        System.out.println(" -- No value founded... --");
                    }
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                System.out.println("*** Error, bad value or format. Try Again ***");
                rep = true;
            }
        } while (rep);

        em.getTransaction().commit();
        em.close();
    }

    /**
     * Delete Season from Database
     */
    public void deleteSeason() {
        EntityManager em = entityManagerFactory.createEntityManager();

        String value;
        em.getTransaction().begin();
        System.out.println(" *** DELETE ***");
        try {
            System.out.println(" -- Do you want to filter (Y/N) ? --");
            value = sc.nextLine();
            if (value.equalsIgnoreCase("Y")) {
                List<Season> seasons = getCriteriaList(em);
                if (seasons != null && seasons.size() != 0) {
                    for (Season s : seasons) {
                        em.remove(s);
                        System.out.println(" -- " + s.toString());
                        System.out.println(" -- Season Deleted -- ");
                    }
                } else {
                    System.out.println(" -- No value founded... --");
                }
            } else if (value.equalsIgnoreCase("N")) {

                Season season = em.find(Season.class, selectSeason());
                if (season != null) {
                    em.remove(season);
                    System.out.println(" -- " + season);
                    System.out.println(" -- Season Deleted -- ");
                } else {
                    System.out.println(" -- No value founded... --");
                }
            } else {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            System.out.println("*** Error, bad value or format.***");
            System.out.println(" ** Exiting **");
        }
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Shows you all the Fields of the Class and let you decide what values you want to change.
     *
     * @param season Season to update
     */
    public void updateFieldForm(Season season) {
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
                    setter4Fields(season, opt);
                    System.out.println("Do you want to update anything else? (Y/N)");
                    if (sc.nextLine().equalsIgnoreCase("Y")) rep = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("*** Error, bad value or format. Try Again ***");
                rep = true;
            }
        } while (rep);
    }

    /**
     * Method to get the List of Seasons that agree with the Restrictions you define.
     *
     * @param em Entity Manager to make the Criteria
     * @return List of Seasons that agree with the criteria
     */
    private List<Season> getCriteriaList(@NotNull EntityManager em) {
        boolean rep;
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Season> q = cb.createQuery(Season.class);
        Root<Season> c = q.from(Season.class);
        q.select(c);

        String option;

        listSeasons();

        System.out.println(" -- What attribute do you want to filter for? --");
        for (int i = 1; i < fields.size(); i++) {
            System.out.println((i) + ": " + fields.get(i).getName());
        }
        System.out.print("Selected: ");
        Field filter = fields.get(Integer.parseInt(sc.nextLine()));
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
                switch (filter.getName()) {
                    case "year", "league", "MVP", "ROTY", "PPG_Leader", "RGP_Leader", "APG_Leader", "WS_Leader" -> {
                        if (option.equalsIgnoreCase("=")) {
                            q.where(
                                    cb.like(c.get(filter.getName()), sc.nextLine())
                            );
                        } else {
                            q.where(
                                    cb.notLike(c.get(filter.getName()), sc.nextLine())
                            );
                        }
                    }
                    default -> {
                        System.out.println("\n -- We can't filter for that field... --");
                        return null;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("*** Error, bad value or format. Try Again ***");
                rep = true;
            }
        } while (rep);
        return em.createQuery(q).getResultList();
    }

    /**
     * Switch that filter for what setter to apply depending on the position of Class Fields List
     *
     * @param season Season Object to set new Values
     * @param pos    postion of field in Fields List
     */
    private void setter4Fields(Season season, int pos) {
        switch (fields.get(pos).getName()) {
            case "year" -> season.setYear(sc.nextLine());
            case "league" -> season.setLeague(sc.nextLine());
            case "champion" -> season.setChampion(teamController.getTeamNull());
            case "MVP" -> season.setMVP(sc.nextLine());
            case "ROTY" -> season.setROTY(sc.nextLine());
            case "PPG_Leader" -> season.setPPG_Leader(sc.nextLine());
            case "RGP_Leader" -> season.setRGP_Leader(sc.nextLine());
            case "APG_Leader" -> season.setAPG_Leader(sc.nextLine());
            case "WS_Leader" -> season.setWS_Leader(sc.nextLine());
        }
    }

    /**
     * Method transform the data from a CSV into Objects
     *
     * @param path path of the csv
     * @return List of Seasons imported from the csv
     */
    public List<Season> readSeasonFile(String path) {
        List<Season> seasonList = new ArrayList<>();
        boolean first = false;
        for (String[] data : OpenCSV.readCSV(path)) {
            try {
                if (!first) first = true;
                else
                    seasonList.add(new Season(data[0], data[1], teamController.getTeam(Integer.parseInt(data[2])), data[3], data[4], data[5], data[6], data[7], data[8]));
            } catch (NumberFormatException e) {
                System.out.println("*** Error, bad value or format.***");
                throw new RuntimeException(e);
            }
        }
        return seasonList;
    }

    /**
     * Method to add a Season into the Database
     *
     * @param Season Season to add
     */
    public void addSeason(@NotNull Season Season) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Season SeasonExists = em.find(Season.class, Season.getSeasonId());
        if (SeasonExists == null) {
            em.persist(Season);
            System.out.println("*** Season Inserted ***");
        }
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Lists all the Seasons that are in the Database
     */
    public void listSeasons() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        List<Season> result = em.createQuery("from Season", Season.class)
                .getResultList().stream().sorted(Comparator.comparingInt(Season::getSeasonId)).toList();
        for (Season season : result) {
            System.out.println(season.toString());
        }
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Delete a Season from the ID.
     *
     * @param seasonId Season id
     */
    public void deleteSeason(Integer seasonId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Season season = em.find(Season.class, seasonId);
        em.remove(season);
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Drops all the Seasons from the Database.
     */
    public void clearSeasons() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        List<Season> result = em.createQuery("from Season", Season.class)
                .getResultList();
        for (Season season : result) {
            deleteSeason(season.getSeasonId());
        }
        em.getTransaction().commit();
        em.close();
    }

}
