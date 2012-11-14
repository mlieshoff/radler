package radler.sample;

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
@Selectables(columns = {"year", "month"})
@Editables(columns = {"year", "month"})
public class Meeting {
    @Id
    private int year;
    @Id
    private int month;

    @ManyToMany
    private List<Speaker> speaker;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public List<Speaker> getSpeaker() {
        return speaker;
    }

    public void setSpeaker(List<Speaker> speaker) {
        this.speaker = speaker;
    }
}
