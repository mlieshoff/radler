package radler;

import radler.gui.MetaModel;
import radler.persistence.DataProvider;
import radler.persistence.H2GenericDataProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * This ...
 *
 * @author mlieshoff
 */
public class ApplicationFactory {

    private final static ApplicationFactory INSTANCE = new ApplicationFactory();

    private Map<String, Class<?>> _classes = new HashMap<String, Class<?>>();
    private Map<Class<?>, MetaModel> _resolvers = new HashMap<Class<?>, MetaModel>();
    private DataProvider _dataProvider;

    public static ApplicationFactory getInstance() {
        return INSTANCE;
    }

    public static void init(Class<?>[] classes) {
        for (Class<?> clazz : classes) {
            getInstance().getClasses().put(clazz.getName(), clazz);
            getInstance().getResolvers().put(clazz, new MetaModel(clazz));
        }
//        getInstance()._dataProvider = new GenericDataProvider(getInstance().getResolvers());
        getInstance()._dataProvider = new H2GenericDataProvider(getInstance().getResolvers());
    }

    public Map<String, Class<?>> getClasses() {
        return _classes;
    }

    public Map<Class<?>, MetaModel> getResolvers() {
        return _resolvers;
    }

    public DataProvider getDataProvider() {
        return _dataProvider;
    }

    public String getResource(String id) {
        return id;
    }
}
