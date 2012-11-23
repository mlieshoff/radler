package radler.sample.model;

import radler.gui.annotation.Editables;
import radler.gui.annotation.Selectables;
import radler.persistence.annotation.Id;

/**
 * This ...
 *
 * @author mlieshoff
 */
@Selectables(columns = {"title"})
@Editables(columns = {"title"})
public class Sex {
    @Id
    private int id;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sex)) return false;

        Sex sex = (Sex) o;

        if (title != null ? !title.equals(sex.title) : sex.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return title != null ? title.hashCode() : 0;
    }
}
