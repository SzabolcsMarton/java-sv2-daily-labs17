package day01;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MoviesRepository {

    private DataSource dataSource;

    public MoviesRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Long saveMovie(String title, LocalDate releaseDate) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("insert into movies(title, release_date) values(?,?)", Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, title);
            stmt.setDate(2, Date.valueOf(releaseDate));
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
                throw new IllegalStateException("Insert failed to movies");
            }

        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot connect", sqle);
        }
    }

    public List<Movie> findAllMovies() {
        List<Movie> movies = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {
            parseMoviesFromResult(stmt, movies);
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot query!", sqle);
        }

        return movies;
    }

    public Optional<Movie> findMovieByTitle(String title) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmp = conn.prepareStatement("SELECT * from movies where title=?")) {
            stmp.setString(1, title);
            try (ResultSet rs = stmp.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Movie(rs.getLong("id"), rs.getString("title"), rs.getDate("release_date").toLocalDate()));
                }
                return Optional.empty();
            }

        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot connect to movies", sqle);
        }
    }

    public double getMovieAvgRating(String title) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "select avg(rating) as calculated_avg from ratings join movies on movies.id=ratings.movie_id where movies.title=?"
             )) {
            stmt.setString(1,title);
            try (ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                   return rs.getDouble("calculated_avg");
                }
                throw new IllegalArgumentException("Cannot find movie");
            }
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot query", sqle);
        }
    }

    public void updateMovieAvgRating(String title,double avg){
        try (Connection conn = dataSource.getConnection();
              PreparedStatement stmt = conn.prepareStatement("Update movies set avg_rating=? where title=?")){
                stmt.setDouble(1, avg);
                stmt.setString(2,title);
                stmt.executeUpdate();
                }catch (SQLException sqle){
                throw new IllegalStateException("Cannot update", sqle);

        }
    }


    private void parseMoviesFromResult(Statement stmt, List<Movie> movies) {
        try (ResultSet rs = stmt.executeQuery("SELECT * FROM movies")) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                LocalDate release = rs.getDate("release_date").toLocalDate();
                movies.add(new Movie((long) id, title, release));
            }
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot parse!", sqle);
        }
    }

}
