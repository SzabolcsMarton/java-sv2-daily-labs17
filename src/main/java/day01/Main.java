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
            dataSource.setUrl("jdbc:mariadb://localhost:3306/movies-actors-test?useUnicode=true");
            dataSource.setUser("root");
            dataSource.setPassword("root");
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot reach DataBase!", sqle);
        }


        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        //flyway.clean();
        flyway.migrate();

        ActorsRepository actorsRepository = new ActorsRepository(dataSource);
        MoviesRepository moviesRepository = new MoviesRepository(dataSource);
        ActorsMoviesRepository actorsMoviesRepository = new ActorsMoviesRepository(dataSource);
        RatingsRepository ratingsRepository = new RatingsRepository(dataSource);

        ActorsMoviesService actorsMoviesService = new ActorsMoviesService(actorsRepository, moviesRepository, actorsMoviesRepository);
        MoviesRatingService moviesRatingService = new MoviesRatingService(moviesRepository,ratingsRepository);


//        actorsMoviesService.insertMovieWithActors("Titanic", LocalDate.of(2000,10,11),List.of("Leonardo Dicaprio", "Kate Winslet"));
//        actorsMoviesService.insertMovieWithActors("Great Gatsby", LocalDate.of(2012,12,10), List.of("Leonardo Dicaprio","Toby"));

        //moviesRatingService.addRatings("Titanic",5, 3, 2);
        moviesRatingService.addRatings("Great Gatsby", 5,2,4);


//        moviesRepository.saveMovie("Filmecske3", LocalDate.of(2022,01,05));

//        List<Movie> movies = moviesRepository.findAllMovies();
//        movies.stream().forEach(movie -> System.out.println(movie.toString()));
//
        //actorsRepository.saveActor("Jack Doe");

        // System.out.println(actorsRepository.findActorsWithPrefix("o"));


    }
}
