package nl.spaan.personeels_app.model;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Long id;

    private String companyName;
    private String phoneNumber;
    private String email;
    private String website;

    @OneToMany (
            mappedBy = "company",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<User> user;

    @OneToMany(mappedBy = "company",
        cascade = CascadeType.ALL,
        orphanRemoval = true)
    private List<StandardRosterDay> standardRosterDays;

    @OneToMany (
            mappedBy = "company",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<WeekRoster> weekRosters;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

    public List<StandardRosterDay> getStandardRosterDays() {
        return standardRosterDays;
    }

    public void setStandardRosterDays(List<StandardRosterDay> standardRosterDays) {
        this.standardRosterDays = standardRosterDays;
    }

    public List<WeekRoster> getWeekRosters() {
        return weekRosters;
    }

    public void setWeekRosters(List<WeekRoster> weekRosters) {
        this.weekRosters = weekRosters;
    }
}
