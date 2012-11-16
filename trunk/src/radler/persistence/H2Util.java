package radler.persistence;

import radler.gui.MetaField;
import radler.gui.MetaModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

/**
 * This ...
 *
 * @author mlieshoff
 */
public class H2Util {

    public static ResultSet select(Connection connection, String sql, Object... params) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(String.format(sql, params));
    }

    public static int update(Connection connection, String sql, Object... params) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeUpdate(String.format(sql, params));
    }

    public static String createSchema(Connection connection, Collection<MetaModel> models) {
        StringBuilder s = new StringBuilder();
        for (MetaModel metaModel : models) {
            String scheme = createTableSchema(connection, metaModel);
            if (scheme.length() > 0) {
                s.append(scheme);
            }
        }
        return s.toString();
    }

    private static String createTableSchema(Connection connection, MetaModel metaModel) {
        String pattern = "create table if not exists '%s' (%s);";
        StringBuilder attributes = new StringBuilder();
        for (MetaField metaField : metaModel.getMetaFields()) {
            attributes.append(metaField.getName());
            attributes.append(" ");
            attributes.append(getSqlType(metaField));
        }
        return String.format(pattern, metaModel.getTitle(), attributes);
    }

    private static String getSqlType(MetaField metaField) {
        Class<?> clazz = metaField.getWrappedType();
        if (clazz == Integer.class) {
            return "int";
        } else if (clazz == Boolean.class) {
            return "";
        } else if (clazz == String.class) {
            return "varchar";
        }
        return "int";
    }
}
