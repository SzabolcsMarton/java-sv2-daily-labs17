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


        Flyway flyway = Flyway.configure().locations("db/migration/movies").dataSource(dataSource).load();
        //flyway.clean();
        flyway.migrate();

        ActorsRepository actorsRepository = new ActorsRepository(dataSource);
        MoviesRepository moviesRepository = new MoviesRepository(dataSource);
        ActorsMoviesRepository actorsMoviesRepository = new ActorsMoviesRepository(dataSource);
        RatingsRepository ratingsRepository = new RatingsRepository(dataSource);

        ActorsMoviesService actorsMoviesService = new ActorsMoviesService(actorsRepository, moviesRepository, actorsMoviesRepository);
        MoviesRatingService moviesRatingService = new MoviesRatingService(moviesRepository,ratingsRepository);






  //      actorsMoviesService.insertMovieWithActors("Star Wars", LocalDate.of(1977,05,25),List.of("Mark Hamill","Harrison Ford","Carrie Fisher"));
//        actorsMoviesService.insertMovieWithActors("Great Gatsby", LocalDate.of(2012,12,10), List.of("Leonardo Dicaprio","Toby"));
//
       // moviesRatingService.addRatings("Titanic",5, 3, 2, 2,5,5,5,5,5,5,5,5);
       // moviesRatingService.addRatings("Great Gatsby", 5,2,4,3,4);
        moviesRatingService.addRatings("Star Wars", 1);


//        moviesRepository.saveMovie("Filmecske3", LocalDate.of(2022,01,05));
//        List<Movie> movies = moviesRepository.findAllMovies();
//        movies.stream().forEach(movie -> System.out.println(movie.toString()));
        //actorsRepository.saveActor("Jack Doe");
        // System.out.println(actorsRepository.findActorsWithPrefix("o"));


    }
}
