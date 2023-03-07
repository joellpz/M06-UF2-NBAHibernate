package newController;

import database.OpenCSV;
import newModel.Team;

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

public class TeamController {
    private final Connection connection;
    private EntityManagerFactory entityManagerFactory;
    private final Scanner sc;
    private List<Field> fields;

    public TeamController(Connection connection) {
        this.connection = connection;
        sc = new Scanner(System.in);
        fields = Arrays.stream(Team.class.getDeclaredFields()).toList();
    }

    public TeamController(Connection connection, EntityManagerFactory entityManagerFactory) {
        this.connection = connection;
        this.entityManagerFactory = entityManagerFactory;
        sc = new Scanner(System.in);
        fields = Arrays.stream(Team.class.getDeclaredFields()).toList();
    }

    private int selectTeam() throws NumberFormatException {
        System.out.println("What element do you want to select?");
        listTeams();
        System.out.print("Selected id: ");
        return Integer.parseInt(sc.nextLine());
    }

    public void newTeam() {
        Team Team = new Team();
        for (int i = 1; i < fields.size(); i++) {
            try {
                System.out.print(fields.get(i).getName() + ": ");
                switch (fields.get(i).getName()) {
                    case "name" -> Team.setName(sc.nextLine());
                    case "age" -> Team.setAge(Integer.parseInt(sc.nextLine()));
                    case "position" -> Team.setPosition(sc.nextLine());
                    case "college" -> Team.setCollege(sc.nextLine());
                    case "draftTeam" -> Team.setDraftTeam(sc.nextLine());
                    case "draftPos" -> Team.setDraftPos(Integer.parseInt(sc.nextLine()));
                    case "draftYear" -> Team.setDraftYear(Integer.parseInt(sc.nextLine()));
                    case "born" -> {
                        System.out.println(" -- Format: yyyy-MM-dd -- ");
                        Team.setBorn(new SimpleDateFormat("yyyy-MM-dd").parse(sc.nextLine()));
                    }
                    case "expCareer" -> Team.setExpCareer(Integer.parseInt(sc.nextLine()));
                }
            } catch (NumberFormatException | ParseException e) {
                System.out.println("*** Error, bad value or format. Try Again ***");
                i--;
            }
        }
        addTeam(Team);
    }

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
                    for (Team p : getCriteriaList(em)) {
                        System.out.println(" Updating -- " + p.toString());
                        updateFieldForm(p);
                        em.merge(p);
                        System.out.println(" -- Team Updated -- ");
                    }
                } else if (value.equalsIgnoreCase("N")) {
                    Team Team = em.find(Team.class, selectTeam());
                    System.out.println(" Updating -- " + Team.toString());
                    updateFieldForm(Team);
                    em.merge(Team);
                    System.out.println(" -- Team Updated -- ");
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

    public void deleteTeam() {
        EntityManager em = entityManagerFactory.createEntityManager();

        String value;
        em.getTransaction().begin();
        System.out.println(" *** DELETE ***");
        try {
            System.out.println(" -- Do you want to filter (Y/N) ? --");
            value = sc.nextLine();
            if (value.equalsIgnoreCase("Y")) {
                for (Team p : getCriteriaList(em)) {
                    em.remove(p);
                    System.out.println(" -- " + p.toString());
                    System.out.println(" -- Team Deleted -- ");
                }
            } else if (value.equalsIgnoreCase("N")) {
                Team Team = em.find(Team.class, selectTeam());
                em.remove(Team);
                System.out.println(" -- " + Team.toString());
                System.out.println(" -- Team Deleted -- ");
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

    public void updateFieldForm(Team Team){
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
                        case "name" -> Team.setName(sc.nextLine());
                        case "age" -> Team.setAge(Integer.parseInt(sc.nextLine()));
                        case "position" -> Team.setPosition(sc.nextLine());
                        case "college" -> Team.setCollege(sc.nextLine());
                        case "draftTeam" -> Team.setDraftTeam(sc.nextLine());
                        case "draftPos" -> Team.setDraftPos(Integer.parseInt(sc.nextLine()));
                        case "draftYear" -> Team.setDraftYear(Integer.parseInt(sc.nextLine()));
                        case "born" -> {
                            System.out.println(" -- Format: yyyy-MM-dd -- ");
                            Team.setBorn(new SimpleDateFormat("yyyy-MM-dd").parse(sc.nextLine()));
                        }
                        case "expCareer" -> Team.setExpCareer(Integer.parseInt(sc.nextLine()));
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

    private List<Team> getCriteriaList(EntityManager em) throws ParseException {
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

    public List<Team> readTeamFile(String path) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<Team> TeamList = new ArrayList();
        boolean first = false;
        for (String[] data : OpenCSV.readCSV(path)) {
            try {
                if (!first) first = true;
                else
                    TeamList.add(new Team(data[0], data[2], data[3], data[4], Integer.parseInt(data[5]), dateFormat.parse(data[7]), Integer.parseInt(data[1]), Integer.parseInt(data[6]), Integer.parseInt(data[8])));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return TeamList;
    }

    public void addTeam(Team Team) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Team TeamExists = em.find(Team.class, Team.getTeamId());
        if (TeamExists == null) {
            em.persist(Team);
            System.out.println("*** Team Inserted ***");
        }
        em.getTransaction().commit();
        em.close();
    }

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

    public void deleteTeam(Integer TeamId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Team Team = em.find(Team.class, TeamId);
        em.remove(Team);
        em.getTransaction().commit();
        em.close();
    }

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
