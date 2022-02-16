package day01;


import org.mariadb.jdbc.Statement;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.Connection;
import java.sql.SQLException;


public class Main {


    public static void main(String[] args) {
        MariaDbDataSource dataSource = new MariaDbDataSource();
        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/movies-actors?useUnicode=true");
            dataSource.setUser("root");
            dataSource.setPassword("root");
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot reach DataBase!",sqle);
        }

        ActorsRepository actorsRepository = new ActorsRepository(dataSource);
        // actorsRepository.saveActor("Jack Doe");

        System.out.println(actorsRepository.findActorsWithPrefix("o"));
    }
}
