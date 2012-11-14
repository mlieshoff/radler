package radler.gui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This ...
 *
 * @author mlieshoff
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Selectables {
    String[] columns();
}
