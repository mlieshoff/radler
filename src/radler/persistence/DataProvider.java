package radler.persistence;

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
}
