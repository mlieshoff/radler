package radler.persistence;

import radler.gui.MetaModel;

import java.util.List;

/**
 * This ...
 *
 * @author mlieshoff
 */
public interface DataProvider<K, T> {

    T create(Class<?> clazz);
    T write(T object);
    T get(Class<?> clazz, K id);
    List<T> read(Class<?> clazz);

    K getKey(MetaModel metaModel, Object object);
}
