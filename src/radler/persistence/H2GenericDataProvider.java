package radler.persistence;

import radler.gui.MetaModel;
import radler.sample.Role;
import radler.sample.Sex;
import radler.sample.SexDataProvider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This ...
 *
 * @author mlieshoff
 */
public class H2GenericDataProvider implements DataProvider<Object, Object> {
    private Map<Class<?>, MetaModel> _resolvers = new HashMap<Class<?>, MetaModel>();
    private Map<Class<?>, DataProvider> _providers = new HashMap<Class<?>, DataProvider>();
    private Connection _connection;

    public H2GenericDataProvider(Map<Class<?>, MetaModel> resolvers) {
        _resolvers = resolvers;

        try {
            _connection = DriverManager.getConnection("jdbc:h2:~/test");

//            H2Util.update(_connection, "create table if not exists sex (title varchar(50) primary key)");

            String schema = H2Util.createSchema(_connection, resolvers.values());
            H2Util.update(_connection, schema);

        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

        _providers.put(Sex.class, new SexDataProvider(_resolvers, _providers, _connection));

        write(createSex("M"));
        write(createSex("F"));

//        write(createRole("admin"));
//        write(createRole("user"));
//        write(createRole("guest"));

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
        return _providers.get(clazz).create(clazz);
    }

    @Override
    public Object write(Object object) {
        return _providers.get(object.getClass()).write(object);
    }

    @Override
    public Object getKey(MetaModel metaModel, Object object) {
        return _providers.get(object.getClass()).getKey(metaModel, object);
    }

    @Override
    public Object get(Class<?> clazz, Object id) {
        return _providers.get(clazz).get(clazz, id);
    }

    @Override
    public List<Object> read(Class<?> clazz) {
        return _providers.get(clazz).read(clazz);
    }

}
