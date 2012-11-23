package radler.sample;

import radler.Application;
import radler.sample.model.Meeting;
import radler.sample.model.Role;
import radler.sample.model.Sex;
import radler.sample.model.Speaker;

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
