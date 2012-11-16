package radler.gui.actions;

import java.util.List;

/**
 * This ...
 *
 * @author mlieshoff
 */
public interface ReadAction {

    List<Object> read(Class<?> clazz);
}
