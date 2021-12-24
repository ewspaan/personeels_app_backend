package nl.spaan.personeels_app.model;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "week_rosters")
public class WeekRoster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int week;
    private int year;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToMany (
            mappedBy = "weekRoster",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<WeekDay> weekDays;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<WeekDay> getWeekDays() {
        return weekDays;
    }

    public void setWeekDays(List<WeekDay> weekDays) {
        this.weekDays = weekDays;
    }
}
