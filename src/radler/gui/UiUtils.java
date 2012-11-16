package radler.gui;

import java.awt.*;

/**
 * This ...
 *
 * @author mlieshoff
 */
public class UiUtils {
    public static void center(Container frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }
}
