package nl.spaan.personeels_app.model;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "standard_roster_days")
public class StandardRosterDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String day;

    @OneToOne
    @JoinColumn(name = "function_id", referencedColumnName = "id")
    private Function function;

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company company;

    @OneToMany (
            mappedBy = "standardRosterDay",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<StandardRosterTime> standardRosterTimes;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<StandardRosterTime> getStandardRosterTimes() {
        return standardRosterTimes;
    }

    public void setStandardRosterTimes(List<StandardRosterTime> standardRosterTimes) {
        this.standardRosterTimes = standardRosterTimes;
    }
}
