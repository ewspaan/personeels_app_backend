package nl.spaan.personeels_app.payload.response;

import java.time.LocalTime;

public class StandardRosterResponse {


    private String day;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean endTimeNeeded;

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

    public boolean isEndTimeNeeded() {
        return endTimeNeeded;
    }

    public void setEndTimeNeeded(boolean endTimeNeeded) {
        this.endTimeNeeded = endTimeNeeded;
    }
}
