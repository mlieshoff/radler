package radler.persistence.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * This ...
 *
 * @author mlieshoff
 */
public class JdbcTemplate {

    protected Connection connection;

    protected int updateInJdbc(String sql, Object... objects) throws SQLException {
        Statement statement = connection.createStatement();
        try {
            System.out.println("update: " + sql);
            return statement.executeUpdate(String.format(sql, objects));
        } catch (SQLException e) {
            throw e;
        } finally {
            statement.close();
        }
    }

    protected ResultSet queryInJdbc(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        try {
            System.out.println("query: " + sql);
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            throw e;
        } finally {
            statement.close();
        }
    }

    protected <T> List<T> queryInJdbc(String sql, RowTransformer<T> rowTransformer, Object... objects) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = null;
        List<T> result = new ArrayList<T>();
        try {
            System.out.println("query: " + sql);
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                result.add(rowTransformer.transform(resultSet));
            }
            return result;
        } catch (SQLException e) {
            throw e;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            statement.close();
        }
    }

}
