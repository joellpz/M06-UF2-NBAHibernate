package controller;

import database.OpenCSV;
import model.Team;
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
 * Class that have all the methods to control a Team Object from the Database.
 */
public class TeamController {
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

    /**
     * Constructor for TeamController
     *
     * @param connection connection to the database
     */
    public TeamController(Connection connection) {
        this.connection = connection;
        sc = new Scanner(System.in);
        fields = Arrays.stream(Team.class.getDeclaredFields()).toList();
    }

    /**
     * Constructor for TeamController
     *
     * @param connection           connection to the database
     * @param entityManagerFactory entity Manager for Model Objects
     */

    public TeamController(Connection connection, EntityManagerFactory entityManagerFactory) {
        this.connection = connection;
        this.entityManagerFactory = entityManagerFactory;
        sc = new Scanner(System.in);
        fields = Arrays.stream(Team.class.getDeclaredFields()).toList();
    }


    /**
     * Method to get the specified Team Object
     *
     * @return Team.
     */
    public Team getTeamNull() {
        EntityManager em = entityManagerFactory.createEntityManager();
        return em.find(Team.class, selectTeam(true));
    }

    /**
     * Method that return the object that had been specified in teamId.
     *
     * @return The Team Object
     */
    public Team getTeam() {
        EntityManager em = entityManagerFactory.createEntityManager();
        return em.find(Team.class, selectTeam(false));
    }

    /**
     * Method that return the object that had been specified in teamId.
     *
     * @param teamId id form the Team
     * @return The Team Object
     */
    public Team getTeam(int teamId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        return em.find(Team.class, teamId);
    }

    /**
     * Shows all the Teams and let you introduce 1 id.
     *
     * @return Team ID
     * @throws NumberFormatException when no number character is introduced.
     */
    private int selectTeam(boolean foreignPetition) throws NumberFormatException {
        System.out.println("What element do you want to select?");
        listTeams();
        if (foreignPetition) System.out.println(" -- Introuce 0 to leave it Blank -- ");
        System.out.print("Selected id: ");
        return Integer.parseInt(sc.nextLine());
    }

    /**
     * Form to introduce new Team
     */
    public void newTeam() {
        Team team = new Team();
        for (int i = 1; i < fields.size(); i++) {
            try {
                System.out.print(fields.get(i).getName() + ": ");
                setter4Fields(team, i);
            } catch (Exception e) {
                System.out.println("*** Error, bad value or format. Try Again ***");
                i--;
            }
        }
        addTeam(team);
    }


    /**
     * Update Team information and let you decide if you want to filtes some values
     */
    public void updateTeam() {
        boolean rep;
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        do {
            try {
                rep = false;
                System.out.println(" -- Do you want to filter (Y/N) ? --");
                String value = sc.nextLine();
                if (value.equalsIgnoreCase("Y")) {
                    List<Team> teams = getCriteriaList(em);
                    if (teams != null && teams.size() != 0) {
                        for (Team t : teams) {
                            System.out.println(" Updating -- " + t.toString());
                            updateFieldForm(t);
                            em.merge(t);
                            System.out.println(" -- Team Updated -- ");
                        }
                    } else {
                        System.out.println(" -- No value founded... --");
                    }
                } else if (value.equalsIgnoreCase("N")) {
                    Team team = em.find(Team.class, selectTeam(false));
                    if (team != null) {
                        System.out.println(" Updating -- " + team);
                        updateFieldForm(team);
                        em.merge(team);
                        System.out.println(" -- Team Updated -- ");
                    } else {
                        System.out.println(" -- No value founded... --");
                    }
                } else {
                    throw new NumberFormatException();
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
     * Delete Team from Database
     */
    public void deleteTeam() {
        EntityManager em = entityManagerFactory.createEntityManager();

        String value;
        em.getTransaction().begin();
        System.out.println(" *** DELETE ***");
        try {
            System.out.println(" -- Do you want to filter (Y/N) ? --");
            value = sc.nextLine();
            if (value.equalsIgnoreCase("Y")) {
                List<Team> teams = getCriteriaList(em);
                if (teams != null && teams.size() != 0) {
                    for (Team t : teams) {
                        em.remove(t);
                        System.out.println(" -- " + t.toString());
                        System.out.println(" -- Team Deleted -- ");
                    }
                } else {
                    System.out.println(" -- No value founded... --");
                }
            } else if (value.equalsIgnoreCase("N")) {
                Team team = em.find(Team.class, selectTeam(false));
                if (team != null) {
                    em.remove(team);
                    System.out.println(" -- " + team);
                    System.out.println(" -- Team Deleted -- ");
                } else {
                    System.out.println(" -- No value founded... --");
                }
            } else {
                throw new NumberFormatException();
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
     * @param team Team to update
     */
    public void updateFieldForm(Team team) {
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
                    setter4Fields(team, opt);
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
     * Method to get the List of Teams that agree with the Restrictions you define.
     *
     * @param em Entity Manager to make the Criteria
     * @return List of Teams that agree with the criteria
     */
    private List<Team> getCriteriaList(@NotNull EntityManager em) {
        boolean rep;
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Team> q = cb.createQuery(Team.class);
        Root<Team> c = q.from(Team.class);
        q.select(c);

        String option;

        listTeams();

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
                    case "name", "location", "conference" -> {
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
                    case "totalGames", "wins", "loses", "playoffAppearances", "conferenceChampions", "championships" -> {

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
                    default -> {
                        System.out.println(" -- We can't filter for that field... --");
                        return null;
                    }
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
     * @param team Team Object to set new Values
     * @param pos  postion of field in Fields List
     */
    private void setter4Fields(Team team, int pos) {
        switch (fields.get(pos).getName()) {
            case "name" -> team.setName(sc.nextLine());
            case "location" -> team.setLocation(sc.nextLine());
            case "totalGames" -> team.setTotalGames(Integer.parseInt(sc.nextLine()));
            case "wins" -> team.setWins(Integer.parseInt(sc.nextLine()));
            case "loses" -> team.setLoses(Integer.parseInt(sc.nextLine()));
            case "playoffAppearances" -> team.setPlayoffAppearances(Integer.parseInt(sc.nextLine()));
            case "conferenceChampions" -> team.setConferenceChampions(Integer.parseInt(sc.nextLine()));
            case "championships" -> team.setChampionships(Integer.parseInt(sc.nextLine()));
            case "conference" -> team.setConference(sc.nextLine());
        }
    }

    /**
     * Method transform the data from a CSV into Objects
     *
     * @param path path of the csv
     * @return List of Teams imported from the csv
     */
    public List<Team> readTeamFile(String path) {
        List<Team> TeamList = new ArrayList<>();
        boolean first = false;
        for (String[] data : OpenCSV.readCSV(path)) {
            try {
                if (!first) first = true;
                else
                    TeamList.add(new Team(data[0], data[1], Integer.parseInt(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4]), Integer.parseInt(data[5]), Integer.parseInt(data[6]), Integer.parseInt(data[7]), data[8]));
            } catch (Exception e) {
                System.out.println("*** Error, bad value or format.***");
                throw new RuntimeException(e);
            }
        }
        return TeamList;
    }

    /**
     * Method to add a Team into the Database
     *
     * @param team Team to add
     */
    public void addTeam(@NotNull Team team) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Team teamExists = em.find(Team.class, team.getTeamId());
        if (teamExists == null) {
            em.persist(team);
            System.out.println("*** Team Inserted ***");
        }
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Lists all the Teams that are in the Database
     */
    public void listTeams() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        List<Team> result = em.createQuery("from Team", Team.class)
                .getResultList().stream().sorted(Comparator.comparingInt(Team::getTeamId)).toList();
        for (Team Team : result) {
            System.out.println(Team.toString());
        }
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Delete a Team from the ID.
     *
     * @param teamId Team id
     */
    public void deleteTeam(Integer teamId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Team Team = em.find(Team.class, teamId);
        em.remove(Team);
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Drops all the Teams from the Database.
     */
    public void clearTeams() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        List<Team> result = em.createQuery("from Team", Team.class)
                .getResultList();
        for (Team Team : result) {
            deleteTeam(Team.getTeamId());
        }
        em.getTransaction().commit();
        em.close();
    }

}
