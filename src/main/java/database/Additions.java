package database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class Additions {

    public static void resetDB(Connection c, String path) {
        try {
            System.out.println("*** Reiniciando BBDD ***");
            Statement statement = c.createStatement();
            try (BufferedReader br = new BufferedReader(new FileReader(path))) {
                statement.execute(br.lines().collect(Collectors.joining(" \n")));
            }
            statement.close();
            System.out.println("*** Done! ***");
        } catch (
                SQLException | IOException e) {
            System.out.println("¡¡ ERROR -> " + e);
        }
    }
}
