package radler.gui.annotation;

/**
 * Created with IntelliJ IDEA.
 * User: micha
 * Date: 14.11.12
 * Time: 22:25
 * To change this template use File | Settings | File Templates.
 */
public @interface Display {
    String format();

    String[] columns();
}
