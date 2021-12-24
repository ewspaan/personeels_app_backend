package nl.spaan.personeels_app.model;


import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "shifts")
public class Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private String function;
    private int multiply;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "week_day_id", nullable = false)
    private WeekDay weekDay;

    @ManyToOne
    @JoinColumn(name="standard_roster_time_id")
    private StandardRosterTime standardRosterTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endShift) {
        this.endTime = endShift;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public int getMultiply() {
        return multiply;
    }

    public void setMultiply(int multiply) {
        this.multiply = multiply;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public WeekDay getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(WeekDay weekDay) {
        this.weekDay = weekDay;
    }

    public StandardRosterTime getStandardRosterTime() {
        return standardRosterTime;
    }

    public void setStandardRosterTime(StandardRosterTime standardRosterTime) {
        this.standardRosterTime = standardRosterTime;
    }
}
