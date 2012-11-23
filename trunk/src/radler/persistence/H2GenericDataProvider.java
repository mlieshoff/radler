package radler.persistence;

import radler.gui.MetaField;
import radler.gui.MetaModel;
import radler.persistence.db.*;
import radler.sample.model.Role;
import radler.sample.model.Sex;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This ...
 *
 * @author mlieshoff
 */
public class H2GenericDataProvider extends JdbcTemplate implements DataProvider<Object, Object> {
    private Map<Class<?>, MetaModel> _resolvers = new HashMap<Class<?>, MetaModel>();
    private Map<Class<?>, DataProvider> _providers = new HashMap<Class<?>, DataProvider>();
    private DbMode dbMode;

    public H2GenericDataProvider(Map<Class<?>, MetaModel> resolvers) {
        _resolvers = resolvers;

        try {
            connection = DriverManager.getConnection("jdbc:h2:~/test");
            dbMode = DbMode.fromConnectionUrl(connection.getMetaData().getURL());
            new DbUpdater("TEST", "PUBLIC", connection, resolvers.values()).update();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
//        _providers.put(Sex.class, new SexDataProvider(_resolvers, _providers, connection));
        write(createSex("M"));
        write(createSex("F"));

        write(createRole("admin"));
        write(createRole("user"));
        write(createRole("guest"));

    }

    private Sex createSex(String title) {
        Sex sex = new Sex();
        sex.setTitle(title);
        return sex;
    }

    private Role createRole(String title) {
        Role role = new Role();
        role.setTitle(title);
        return role;
    }


    @Override
    public Object create(Class<?> clazz) {
        if (_providers.containsKey(clazz)) {
            return _providers.get(clazz).create(clazz);
        } else {
            try {
                return clazz.newInstance();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }

    @Override
    public Object write(Object object) {
        Class<?> clazz = object.getClass();
        if (_providers.containsKey(clazz)) {
            return _providers.get(clazz).write(object);
        } else {
            MetaModel metaModel = _resolvers.get(clazz);
            try {
                if (get(clazz, getKey(metaModel, object)) == null) {
                    updateInJdbc(SqlUtil.createInsertSql(dbMode, metaModel, object));
                }
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
            return object;
        }
    }

    @Override
    public Object getKey(MetaModel metaModel, Object object) {
        Class<?> clazz = object.getClass();
        if (_providers.containsKey(clazz)) {
            return _providers.get(clazz).getKey(metaModel, object);
        } else {
            return metaModel.getKeys(object);
        }
    }

    @Override
    public Object get(final Class<?> clazz, Object id) {
        if (_providers.containsKey(clazz)) {
            return _providers.get(clazz).get(clazz, id);
        } else {
            final MetaModel metaModel = _resolvers.get(clazz);
            try {
                List<Object> result = queryInJdbc(SqlUtil.createSelectSql(dbMode, metaModel, id), new RowTransformer<Object>() {
                    @Override
                    public Object transform(ResultSet resultSet) {
                        try {
                            Object object = clazz.newInstance();
                            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                            for (int i = 1, n = resultSetMetaData.getColumnCount(); i < n; i ++) {
                                String columnName = resultSetMetaData.getColumnName(i);
                                MetaField metaField = metaModel.getMetaField(columnName);
                                metaField.setValue(object, resultSet.getObject(i));
                            }
                            return object;
                        } catch (Exception e) {
                            throw new IllegalStateException(e);
                        }
                    }
                });
                if (result.size() > 0) {
                    return result.get(0);
                }
                return null;
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }

    @Override
    public List<Object> read(final Class<?> clazz) {
        if (_providers.containsKey(clazz)) {
            return _providers.get(clazz).read(clazz);
        } else {
            final MetaModel metaModel = _resolvers.get(clazz);
            try {
                return queryInJdbc(SqlUtil.createSelectSql(dbMode, metaModel), new RowTransformer<Object>() {
                    @Override
                    public Object transform(ResultSet resultSet) {
                        try {
                            Object object = clazz.newInstance();
                            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                            for (int i = 1, n = resultSetMetaData.getColumnCount(); i < n; i ++) {
                                String columnName = resultSetMetaData.getColumnName(i);
                                MetaField metaField = metaModel.getMetaField(columnName);
                                metaField.setValue(object, resultSet.getObject(i));
                            }
                            return object;
                        } catch (Exception e) {
                            throw new IllegalStateException(e);
                        }
                    }
                });
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }

}
