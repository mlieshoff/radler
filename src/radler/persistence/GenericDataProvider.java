package radler.persistence;

import radler.gui.UiClassResolver;
import radler.sample.Product;
import radler.sample.Sex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This ...
 *
 * @author mlieshoff
 */
public class GenericDataProvider implements DataProvider<Object, Object> {
    private Map<Class<?>, UiClassResolver> _resolvers = new HashMap<Class<?>, UiClassResolver>();
    private Map<Class<?>, Map<Object, Object>> _model = new HashMap<Class<?>, Map<Object, Object>>();

    public GenericDataProvider(Map<Class<?>, UiClassResolver> resolvers) {
        _resolvers = resolvers;

        write(createSex("M"));
        write(createSex("F"));

    }

    private Sex createSex(String title) {
        Sex sex = new Sex();
        sex.setTitle(title);
        return sex;
    }

    @Override
    public Object create(Class<?> clazz) {
        return _resolvers.get(clazz).createObject();
    }

    @Override
    public Object write(Object object) {
        UiClassResolver uiClassResolver = _resolvers.get(object.getClass());
        Map<Object, Object> map = _model.get(uiClassResolver.getObjectClass());
        if (map == null) {
            map = new HashMap<Object, Object>();
            _model.put(uiClassResolver.getObjectClass(), map);
        }
        map.put(uiClassResolver.getKey(object), object);
        return object;
    }

    @Override
    public Object get(Class<?> clazz, Object id) {
        UiClassResolver uiClassResolver = _resolvers.get(clazz);
        Map<Object, Object> map = _model.get(uiClassResolver.getObjectClass());
        return map != null ? map.get(id) : null;
    }

    @Override
    public List<Object> read(Class<?> clazz) {
        UiClassResolver uiClassResolver = _resolvers.get(clazz);
        Map<Object, Object> map = _model.get(uiClassResolver.getObjectClass());
        return map != null ? new ArrayList<Object>(map.values()) : null;
    }
}
