package day01;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MovieRepository {

    private DataSource dataSource;

    public MovieRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void saveMovie(String title, LocalDate releaseDate){
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("insert into movies(title, release_date) values(?,?)")){
            stmt.setString(1,title);
            stmt.setDate(2, Date.valueOf(releaseDate));
            stmt.executeUpdate();

        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot connect",sqle);
        }
    }

    public List<Movie> findAllMovies(){
        List<Movie> movies = new ArrayList<>();
        try(Connection connection = dataSource.getConnection();
            Statement stmt = connection.createStatement()){
            parseMoviesFromResult(stmt,movies);
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot query!",sqle);
        }

        return movies;
    }

    private void parseMoviesFromResult(Statement stmt,List<Movie> movies){
        try(ResultSet rs = stmt.executeQuery("SELECT * FROM movies")) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                LocalDate release = rs.getDate("release_date").toLocalDate();
                movies.add(new Movie((long) id,title,release));
            }
        }catch (SQLException sqle) {
            throw new IllegalStateException("Cannot parse!",sqle);
        }
    }

}
