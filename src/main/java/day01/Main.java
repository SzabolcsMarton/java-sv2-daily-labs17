package day01;

import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

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

//        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
//        flyway.migrate();

        MovieRepository movieRepository = new MovieRepository(dataSource);
        //movieRepository.saveMovie("Filmecske", LocalDate.of(2022,01,05));
        movieRepository.saveMovie("Filmecske2", LocalDate.of(2022,01,05));
        movieRepository.saveMovie("Filmecske3", LocalDate.of(2022,01,05));

        List<Movie> movies = movieRepository.findAllMovies();
        movies.stream().forEach(movie -> System.out.println(movie.toString()));

//        ActorsRepository actorsRepository = new ActorsRepository(dataSource);
//        actorsRepository.saveActor("Jack Doe");

       // System.out.println(actorsRepository.findActorsWithPrefix("o"));


    }
}
