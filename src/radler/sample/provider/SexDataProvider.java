package radler.sample.provider;

import radler.gui.MetaModel;
import radler.persistence.DataProvider;
import radler.persistence.db.JdbcTemplate;
import radler.persistence.db.RowTransformer;
import radler.sample.model.Sex;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This ...
 *
 * @author mlieshoff
 */
public class SexDataProvider extends JdbcTemplate implements DataProvider<String, Sex> {

    private Map<Class<?>, MetaModel> _resolvers = new HashMap<Class<?>, MetaModel>();
    private Map<Class<?>, DataProvider> _providers = new HashMap<Class<?>, DataProvider>();

    public SexDataProvider(Map<Class<?>, MetaModel> resolvers, Map<Class<?>, DataProvider> providers, Connection connection) {
        _resolvers = resolvers;
        _providers = providers;
        this.connection = connection;
    }

    @Override
    public Sex create(Class<?> clazz) {
        return new Sex();
    }

    @Override
    public Sex write(Sex sex) {
        try {
            if (get(null, sex.getTitle()) == null) {
                updateInJdbc("insert into sex (title) values('%s')", sex.getTitle());
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return sex;
    }

    @Override
    public Sex get(Class<?> clazz, String id) {
        try {
            List<Sex> list = queryInJdbc("select title from sex where title = '%s'", new RowTransformer<Sex>() {
                @Override
                public Sex transform(ResultSet resultSet) throws SQLException {
                    Sex sex = new Sex();
                    sex.setTitle(resultSet.getString(1));
                    return sex;
                }
            }, id);
            if (list.size() > 0) {
                return list.get(0);
            }
            return null;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<Sex> read(Class<?> clazz) {
        try {
            return queryInJdbc("select title from sex", new RowTransformer<Sex>() {
                @Override
                public Sex transform(ResultSet resultSet) throws SQLException {
                    Sex sex = new Sex();
                    sex.setTitle(resultSet.getString(1));
                    return sex;
                }
            });
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String getKey(MetaModel metaModel, Object object) {
        return ((Sex) object).getTitle();
    }
}
