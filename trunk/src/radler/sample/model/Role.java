package radler.sample.model;

import radler.gui.annotation.Editables;
import radler.gui.annotation.Selectables;
import radler.persistence.annotation.Id;
import radler.persistence.annotation.ManyToMany;

import java.util.List;

/**
 * This ...
 *
 * @author mlieshoff
 */
@Selectables(columns = {"title"})
@Editables(columns = {"title"})
public class Role {
    @Id
    private String title;

    @ManyToMany(to = Speaker.class)
    private List<Speaker> speaker;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Speaker> getSpeaker() {
        return speaker;
    }

    public void setSpeaker(List<Speaker> speaker) {
        this.speaker = speaker;
    }
}
