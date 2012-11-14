package radler.sample;

import radler.Application;

/**
 * This ...
 *
 * @author mlieshoff
 */
public class Main {

    public static void main(String[] args) {
        new Application(new Class<?>[]{Speaker.class, Sex.class, Role.class, Meeting.class});
    }

}
