package nl.spaan.personeels_app.payload.response;

import java.util.List;

public class WeekDayResponse {

    private Long id;
    private String day;
    private String dateOfDay;
    private List<ShiftResponse> shiftResponseList;

    public void setId(Long id) {
        this.id = id;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setDateOfDay(String dateOfDay) {
        this.dateOfDay = dateOfDay;
    }

    public void setShiftResponseList(List<ShiftResponse> shiftResponseList) {
        this.shiftResponseList = shiftResponseList;
    }

    public Long getId() {
        return id;
    }

    public String getDay() {
        return day;
    }

    public String getDateOfDay() {
        return dateOfDay;
    }

    public List<ShiftResponse> getShiftResponseList() {
        return shiftResponseList;
    }
}
