package radler.persistence;

import radler.gui.MetaField;
import radler.gui.MetaModel;
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
    private Map<Class<?>, MetaModel> _resolvers = new HashMap<Class<?>, MetaModel>();
    private Map<Class<?>, Map<Object, Object>> _model = new HashMap<Class<?>, Map<Object, Object>>();

    private static int INT_ID = 0;

    public GenericDataProvider(Map<Class<?>, MetaModel> resolvers) {
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
        MetaModel metaModel = _resolvers.get(clazz);
        Object object = metaModel.createObject();
        List<MetaField> keys = metaModel.getKeyFields();
        if (keys.size() == 1) {
            MetaField metaField = metaModel.getKeyFields().get(0);
            if (metaField.getWrappedType() == Integer.class) {
                metaField.setValue(object, ++ INT_ID);
            }
        }
        return object;
    }

    @Override
    public Object write(Object object) {
        MetaModel metaModel = _resolvers.get(object.getClass());
        Map<Object, Object> map = _model.get(metaModel.getObjectClass());
        if (map == null) {
            map = new HashMap<Object, Object>();
            _model.put(metaModel.getObjectClass(), map);
        }
        map.put(getKey(metaModel, object), object);
        return object;
    }

    @Override
    public Object getKey(MetaModel metaModel, Object object) {
        Map<MetaField, Object> map = metaModel.getKeys(object);
        StringBuilder s = new StringBuilder();
        for (MetaField key : metaModel.getKeyFields()) {
            s.append(map.get(key));
        }
        return s.toString();
    }

    @Override
    public Object get(Class<?> clazz, Object id) {
        MetaModel metaModel = _resolvers.get(clazz);
        Map<Object, Object> map = _model.get(metaModel.getObjectClass());
        Object value = null;
        if (map != null) {
            value = map.get(id);
        }
        return value;
    }

    @Override
    public List<Object> read(Class<?> clazz) {
        MetaModel metaModel = _resolvers.get(clazz);
        Map<Object, Object> map = _model.get(metaModel.getObjectClass());
        return map != null ? new ArrayList<Object>(map.values()) : null;
    }

}
