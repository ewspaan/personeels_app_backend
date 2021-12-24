package nl.spaan.personeels_app.model;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "week_days")
public class WeekDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String day;
    LocalDate dateOfDay;

    @OneToMany (
            mappedBy = "weekDay",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Shift> shifts;

    @ManyToOne
    @JoinColumn(name = "week_roster_id", nullable = false)
    private WeekRoster weekRoster;

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

    public LocalDate getDateOfDay() {
        return dateOfDay;
    }

    public void setDateOfDay(LocalDate dateOfDay) {
        this.dateOfDay = dateOfDay;
    }

    public List<Shift> getShifts() {
        return shifts;
    }

    public void setShifts(List<Shift> shifts) {
        this.shifts = shifts;
    }

    public WeekRoster getWeekRoster() {
        return weekRoster;
    }

    public void setWeekRoster(WeekRoster weekRoster) {
        this.weekRoster = weekRoster;
    }
}
