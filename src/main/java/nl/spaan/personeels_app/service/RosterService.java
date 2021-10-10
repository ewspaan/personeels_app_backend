package nl.spaan.personeels_app.service;

import nl.spaan.personeels_app.model.*;
import nl.spaan.personeels_app.payload.request.RosterRequest;
import nl.spaan.personeels_app.payload.response.StandardRosterResponse;
import nl.spaan.personeels_app.repository.StandardRosterDayRepository;
import nl.spaan.personeels_app.repository.StandardRosterTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class RosterService {

    private UserService userService;

    private StandardRosterDayRepository standardRosterDayRepository;

    private StandardRosterTimeRepository standardRosterTimeRepository;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setStandardRosterDayRepository(StandardRosterDayRepository standardRosterDayRepository) {
        this.standardRosterDayRepository = standardRosterDayRepository;
    }

    @Autowired
    public void setStandardRosterTimeRepository(StandardRosterTimeRepository standardRosterTimeRepository) {
        this.standardRosterTimeRepository = standardRosterTimeRepository;
    }

    public ResponseEntity<?> setStandard(String authorization, RosterRequest rosterRequest) {

        if(!validatedRosterRequest(rosterRequest)){
            return ResponseEntity.ok("Time incorrect");
        }
        Company company = userService.getUserFromToken(authorization).getCompany();
        List <StandardRosterDay> standardRosterDays = new ArrayList<>(standardRosterDayRepository.findAllByCompany(company));
        if (standardRosterDays.size() == 0){
            createStandardRosterDays(company);
        }
        standardRosterDays = standardRosterDayRepository.findAllByCompany(company);
        for(StandardRosterDay standardRosterDay : standardRosterDays) {
            if(Objects.equals(standardRosterDay.getDay(), rosterRequest.getDay().toUpperCase())){
                System.out.println("dag--> " + standardRosterDay.getDay() + "  " + standardRosterDay.getId());
                    List<StandardRosterTime> standardRosterTimes = standardRosterTimeRepository.findByStandardRosterDayId(standardRosterDay.getId());
                    if (standardRosterTimes.size() == 0){
                        saveNewStandardRosterTime(standardRosterDay,rosterRequest);
                    }
                    for (StandardRosterTime rosterTime : standardRosterTimes) {
                        if (Objects.equals(rosterRequest.getStartTimeHour(), rosterTime.getStartTimeHour())
                                && Objects.equals(rosterRequest.getStartTimeMinute(), rosterTime.getStartTimeMinute())
                                && Objects.equals(rosterRequest.getEndTimeHour(), rosterTime.getEndTimeHour())
                                && Objects.equals(rosterRequest.getEndTimeMinute(), rosterTime.getEndTimeMinute())
                                && Objects.equals(standardRosterDay.getId(), rosterTime.getId())) {
                            System.out.println("Gelijk--> " + rosterRequest.getEndTimeMinute());
                            System.out.println("Gelijk--> " + standardRosterDay.getDay());
                            rosterTime.setMultiply(rosterTime.getMultiply()+rosterRequest.getMultiply());
                            System.out.println("Gelijk--> " + standardRosterDay.getDay());
                            standardRosterTimeRepository.save(rosterTime);
                        }
                        else {
                            saveNewStandardRosterTime(standardRosterDay,rosterRequest);
                        }
                    }
                }
        }
        return null;
    }

    public ResponseEntity<?> getStandardRoster(String token) {

        User user = userService.getUserFromToken(token);

        return ResponseEntity.ok(createStandardRosterResponse(user.getCompany()));
    }

    private LocalTime convertStringToLocaltime(String hour, String minute) {

        int hourTemp = Integer.parseInt(hour);
        int minuteTemp = Integer.parseInt(minute);
        LocalTime time = LocalTime.of(hourTemp, minuteTemp, 0, 0);
        System.out.println("Time--> " + time);
        return time;
    }

    private boolean validatedRosterRequest(RosterRequest rosterRequest){


            if(!rosterRequest.getStartTimeHour().matches("^[0-9]*$") || rosterRequest.getStartTimeHour().length() > 2){
                System.out.println("1  ");
                return false;
            }
            if(!rosterRequest.getStartTimeMinute().matches("^[0-9]*$") || rosterRequest.getStartTimeMinute().length() > 2){
                System.out.println("2  ");
                return false;
            }
            if(!rosterRequest.getEndTimeHour().matches("^[0-9]*$") || rosterRequest.getEndTimeHour().length() > 2){
                System.out.println("3  ");
                return false;
            }
            if(!rosterRequest.getEndTimeMinute().matches("^[0-9]*$") || rosterRequest.getEndTimeMinute().length() > 2){
                System.out.println("4  ");
                return false;
            }
            if(enumContainsValue(rosterRequest.getDay())){
                System.out.println("5  ");
                return false;
            }
            if(validateTime(rosterRequest.getStartTimeHour(), rosterRequest.getStartTimeMinute())){
                System.out.println("6  ");
                return false;
            }
            if(validateTime(rosterRequest.getEndTimeHour(), rosterRequest.getEndTimeMinute())){
                System.out.println("7  ");
                return false;
            }
        return true;
    }

    private boolean enumContainsValue(String value)
    {
        if (value != null ) {
            for (EWeekDay eWeekDay : EWeekDay.values()) {
                if (!eWeekDay.name().equals(value.toUpperCase())) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean validateTime(String hour, String minute) {

        int hourTemp = Integer.parseInt(hour);
        int minuteTemp = Integer.parseInt(minute);
        if (hourTemp > 0 && hourTemp < 24) {
            return minuteTemp < 0 || minuteTemp >= 60;
        }
        return true;
    }

    private List<StandardRosterResponse> createStandardRosterResponse(Company company){

        List<StandardRosterDay> standardRosterDays = standardRosterDayRepository.findAllByCompany(company);
        List<StandardRosterResponse> standardRosterResponses = new ArrayList<>();
        for (StandardRosterDay standardRosterDay : standardRosterDays){
            List<StandardRosterTime> standardRosterTimes = standardRosterDay.getStandardRosterTimes();
            for (StandardRosterTime standardRosterTime : standardRosterTimes){
                for(int i = standardRosterTime.getMultiply(); i > 0; i--){
                    StandardRosterResponse standardRosterResponse1 = new StandardRosterResponse();
                    standardRosterResponse1.setDay(standardRosterDay.getDay());
                    standardRosterResponse1.setStartTime(standardRosterTime.getStartTime());
                    standardRosterResponse1.setEndTime(standardRosterTime.getEndTime());
                    standardRosterResponse1.setEndTimeNeeded(standardRosterTime.isEndTimeNeeded());
                    standardRosterResponses.add(standardRosterResponse1);
                }
            }
        }
        return standardRosterResponses;
    }

    private void saveNewStandardRosterDay(Company company, RosterRequest rosterRequest){
        StandardRosterDay standardRosterDay = new StandardRosterDay();
        standardRosterDay.setDay(rosterRequest.getDay());
        standardRosterDay.setCompany(company);
        standardRosterDayRepository.save(standardRosterDay);
        saveNewStandardRosterTime(standardRosterDay,rosterRequest);
    }

    private void saveNewStandardRosterTime(StandardRosterDay standardRosterDay, RosterRequest rosterRequest){
        StandardRosterTime standardRosterTime = new StandardRosterTime();
        standardRosterTime.setStartTime(convertStringToLocaltime(rosterRequest.getStartTimeHour(), rosterRequest.getStartTimeMinute()));
        standardRosterTime.setEndTime(convertStringToLocaltime(rosterRequest.getEndTimeHour(), rosterRequest.getEndTimeMinute()));
        standardRosterTime.setStartTimeHour(rosterRequest.getStartTimeHour());
        standardRosterTime.setStartTimeMinute(rosterRequest.getStartTimeMinute());
        standardRosterTime.setEndTimeHour(rosterRequest.getEndTimeHour());
        standardRosterTime.setEndTimeMinute(rosterRequest.getEndTimeMinute());
        standardRosterTime.setMultiply(rosterRequest.getMultiply());
        standardRosterTime.setEndTimeNeeded(rosterRequest.isEndTime());
        standardRosterTime.setStandardRosterDay(standardRosterDay);
        standardRosterTimeRepository.save(standardRosterTime);
    }

    private void createStandardRosterDays(Company company){

        for (EWeekDay eWeekDay : EWeekDay.values()) {
            RosterRequest rosterRequest = new RosterRequest();
            rosterRequest.setDay(eWeekDay.toString());
            StandardRosterDay standardRosterDay = new StandardRosterDay();
            standardRosterDay.setDay(rosterRequest.getDay());
            standardRosterDay.setCompany(company);
            standardRosterDayRepository.save(standardRosterDay);
        }
    }

}
