package radler.persistence.db;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This ...
 *
 * @author mlieshoff
 */
public interface RowTransformer<T> {

    T transform(ResultSet resultSet) throws SQLException;

}
