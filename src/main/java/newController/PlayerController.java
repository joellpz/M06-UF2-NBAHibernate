package newController;

import newModel.Player;
import profesor.model.Author;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.sql.Connection;

public class PlayerController {
    private Connection connection;
    private EntityManagerFactory entityManagerFactory;

    public PlayerController(Connection connection) {
        this.connection = connection;
    }

    public PlayerController(Connection connection, EntityManagerFactory entityManagerFactory) {
        this.connection = connection;
        this.entityManagerFactory = entityManagerFactory;
    }

    public void addPlayer(Player player) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Player playerExists = (Player) em.find(Player.class, player.getPlayerId());
        if (playerExists == null) {
            em.persist(player);
            System.out.println("*** Player Inserted ***");
        }
        em.getTransaction().commit();
        em.close();
    }


}
