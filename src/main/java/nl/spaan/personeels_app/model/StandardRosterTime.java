package nl.spaan.personeels_app.model;


import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "standard_roster_times")
public class StandardRosterTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime startTime;
    private LocalTime endTime;
    private String startTimeHour;
    private String startTimeMinute;
    private String endTimeHour;
    private String endTimeMinute;
    private boolean endTimeNeeded;
    private int multiply;
    private long totalTime;

    @ManyToOne
    @JoinColumn(name = "standard_roster_day_id", nullable = false)
    private StandardRosterDay standardRosterDay;

    @OneToMany (
            mappedBy = "standardRosterTime",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Shift> shifts;


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

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public boolean isEndTimeNeeded() {
        return endTimeNeeded;
    }

    public void setEndTimeNeeded(boolean endTimeNeeded) {
        this.endTimeNeeded = endTimeNeeded;
    }

    public int getMultiply() {
        return multiply;
    }

    public void setMultiply(int multiply) {
        this.multiply = multiply;
    }

    public StandardRosterDay getStandardRosterDay() {
        return standardRosterDay;
    }

    public void setStandardRosterDay(StandardRosterDay standardRosterDay) {
        this.standardRosterDay = standardRosterDay;
    }

    public String getStartTimeHour() {
        return startTimeHour;
    }

    public void setStartTimeHour(String startTimeHour) {
        this.startTimeHour = startTimeHour;
    }

    public String getStartTimeMinute() {
        return startTimeMinute;
    }

    public void setStartTimeMinute(String startTimeMinute) {
        this.startTimeMinute = startTimeMinute;
    }

    public String getEndTimeHour() {
        return endTimeHour;
    }

    public void setEndTimeHour(String endTimeHour) {
        this.endTimeHour = endTimeHour;
    }

    public String getEndTimeMinute() {
        return endTimeMinute;
    }

    public void setEndTimeMinute(String endTimeMinute) {
        this.endTimeMinute = endTimeMinute;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public List<Shift> getShifts() {
        return shifts;
    }

    public void setShifts(List<Shift> shifts) {
        this.shifts = shifts;
    }
}
