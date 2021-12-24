package nl.spaan.personeels_app.payload.response;

import java.time.LocalTime;
import java.util.List;

public class StandardRosterResponse {


    private Long id;
    private String day;
    private LocalTime startTime;
    private LocalTime endTime;
    private int startTimeHour;
    private int startTimeMinute;
    private int endTimeHour;
    private int endTimeMinute;
    private boolean endTimeNeeded;
    private long totalTimeInMinutes;
    private int multiply;
    private List<StandardRosterResponse> standardRosterResponses;

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

    public int getStartTimeHour() {
        return startTimeHour;
    }

    public void setStartTimeHour(int startTimeHour) {
        this.startTimeHour = startTimeHour;
    }

    public int getStartTimeMinute() {
        return startTimeMinute;
    }

    public void setStartTimeMinute(int startTimeMinute) {
        this.startTimeMinute = startTimeMinute;
    }

    public int getEndTimeHour() {
        return endTimeHour;
    }

    public void setEndTimeHour(int endTimeHour) {
        this.endTimeHour = endTimeHour;
    }

    public int getEndTimeMinute() {
        return endTimeMinute;
    }

    public void setEndTimeMinute(int endTimeMinute) {
        this.endTimeMinute = endTimeMinute;
    }

    public boolean isEndTimeNeeded() {
        return endTimeNeeded;
    }

    public void setEndTimeNeeded(boolean endTimeNeeded) {
        this.endTimeNeeded = endTimeNeeded;
    }

    public long getTotalTimeInMinutes() {
        return totalTimeInMinutes;
    }

    public void setTotalTimeInMinutes(long totalTimeInMinutes) {
        this.totalTimeInMinutes = totalTimeInMinutes;
    }

    public int getMultiply() {
        return multiply;
    }

    public void setMultiply(int multiply) {
        this.multiply = multiply;
    }

    public List<StandardRosterResponse> getStandardRosterResponses() {
        return standardRosterResponses;
    }

    public void setStandardRosterResponses(List<StandardRosterResponse> standardRosterResponses) {
        this.standardRosterResponses = standardRosterResponses;
    }
}
