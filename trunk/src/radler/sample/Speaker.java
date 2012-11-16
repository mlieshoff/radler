package radler.sample;

import radler.gui.annotation.Editables;
import radler.gui.annotation.Selectables;
import radler.persistence.annotation.Id;
import radler.persistence.annotation.ManyToMany;
import radler.persistence.annotation.OneToMany;

import java.util.List;

/**
 * This ...
 *
 * @author mlieshoff
 */
@Selectables(columns = {"firstname", "lastname", "street", "streetNumber", "phone", "email", "sex", "active"})
@Editables(columns = {"firstname", "lastname", "street", "streetNumber", "phone", "email", "sex", "active", "meeting", "role"})
public class Speaker {

    @Id
    private int id;

    private String firstname;
    private String lastname;
    private String street;
    private String streetNumber;
    private String phone;
    private String email;

    private boolean active;

    @OneToMany(displayPattern = "%s", displayFields={"title"})
    private Sex sex;

    @ManyToMany(to = Meeting.class)
    private List<Meeting> meeting;

    @ManyToMany(to = Role.class)
    private List<Role> role;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public List<Meeting> getMeeting() {
        return meeting;
    }

    public void setMeeting(List<Meeting> meeting) {
        this.meeting = meeting;
    }

    public List<Role> getRole() {
        return role;
    }

    public void setRole(List<Role> role) {
        this.role = role;
    }
}
