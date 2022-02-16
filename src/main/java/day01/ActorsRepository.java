package day01;



import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActorsRepository {

    private DataSource dataSource;

    public ActorsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void saveActor(String name){
        try(Connection connection = dataSource.getConnection();
            PreparedStatement stmt = connection.prepareStatement("insert into actors(actor_name) values(?)")) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot update: "+ name, sqle);
        }
    }


    public List<String> findActorsWithPrefix(String prefix){
        List<String> result = new ArrayList<>();
        try(Connection connection = dataSource.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT actor_name FROM actors WHERE actor_name LIKE ?")){
            stmt.setString(1, "%"+prefix+"%");
            parseActorFromResult(stmt,result);
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot query!",sqle);
        }
        return result;
    }

    private void parseActorFromResult(PreparedStatement stmt,List<String> actorNames){
        try(ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String actorName = rs.getString("actor_name");
                actorNames.add(actorName);
            }
        }catch (SQLException sqle) {
            throw new IllegalStateException("Cannot parse!",sqle);
        }
    }


}
