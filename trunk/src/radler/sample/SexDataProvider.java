package radler.sample;

import radler.gui.MetaModel;
import radler.persistence.DataProvider;
import radler.persistence.H2Util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This ...
 *
 * @author mlieshoff
 */
public class SexDataProvider implements DataProvider<String, Sex> {

    private Map<Class<?>, MetaModel> _resolvers = new HashMap<Class<?>, MetaModel>();
    private Map<Class<?>, DataProvider> _providers = new HashMap<Class<?>, DataProvider>();
    private Connection _connection;

    public SexDataProvider(Map<Class<?>, MetaModel> resolvers, Map<Class<?>, DataProvider> providers, Connection connection) {
        _resolvers = resolvers;
        _providers = providers;
        _connection = connection;
    }

    @Override
    public Sex create(Class<?> clazz) {
        return new Sex();
    }

    @Override
    public Sex write(Sex sex) {
        try {
            if (get(null, sex.getTitle()) == null) {
                H2Util.update(_connection, "insert into sex (title) values('%s')", sex.getTitle());
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return sex;
    }

    @Override
    public Sex get(Class<?> clazz, String id) {
        Sex sex = null;
        try {
            ResultSet resultSet = H2Util.select(_connection, "select title from sex where title = '%s'", id);
            while(resultSet.next()) {
                sex = new Sex();
                sex.setTitle(resultSet.getString(1));
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return sex;
    }

    @Override
    public List<Sex> read(Class<?> clazz) {
        List<Sex> list = new ArrayList<Sex>();
        try {
            ResultSet resultSet = H2Util.select(_connection, "select title from sex");
            while(resultSet.next()) {
                Sex sex = new Sex();
                sex.setTitle(resultSet.getString(1));
                list.add(sex);
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return list;
    }

    @Override
    public String getKey(MetaModel metaModel, Object object) {
        return ((Sex) object).getTitle();
    }
}
