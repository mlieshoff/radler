package radler.sample;

import radler.gui.annotation.Display;
import radler.gui.annotation.Editables;
import radler.gui.annotation.Selectables;
import radler.persistence.annotation.Id;
import radler.persistence.annotation.OneToMany;

import java.util.List;

/**
 * This ...
 *
 * @author mlieshoff
 */
@Selectables(columns = {"title"})
@Editables(columns = {"title"})
@Display(format = "%s", columns = {"title"})
public class Sex {

    @Id
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
