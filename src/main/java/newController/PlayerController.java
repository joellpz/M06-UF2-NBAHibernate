package newController;

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


}
