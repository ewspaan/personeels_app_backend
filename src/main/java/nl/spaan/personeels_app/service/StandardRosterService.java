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
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class StandardRosterService {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private StandardRosterDayRepository standardRosterDayRepository;

    @Autowired
    public void setStandardRosterDayRepository(StandardRosterDayRepository standardRosterDayRepository) {
        this.standardRosterDayRepository = standardRosterDayRepository;
    }

    private StandardRosterTimeRepository standardRosterTimeRepository;

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
        StandardRosterTime standardRosterTime = checkStandardRosterTimeExists(company,rosterRequest);
        StandardRosterDay standardRosterDay = standardRosterDayRepository.findByCompanyAndDay(company,rosterRequest.getDay().toUpperCase());
        if(standardRosterTime == null){
            saveNewStandardRosterTime(standardRosterDay,rosterRequest);
        }
        if(standardRosterTime !=null){
            standardRosterTime.setMultiply(standardRosterTime.getMultiply() + 1);
            standardRosterTimeRepository.save(standardRosterTime);
        }
        return null;
    }

    public ResponseEntity<?> getStandardRoster(String token) {

        User user = userService.getUserFromToken(token);

        return ResponseEntity.ok(createStandardRosterResponse(user.getCompany()));
    }

    public ResponseEntity<?> deleteStandardRoster(String token, RosterRequest rosterRequest) {

        if(!validatedRosterRequest(rosterRequest)){
            return ResponseEntity.ok("Time incorrect");
        }
        Company company = userService.getUserFromToken(token).getCompany();
        StandardRosterTime standardRosterTime = checkStandardRosterTimeExists(company,rosterRequest);
        if(standardRosterTime == null){
            return null;
        }
        standardRosterTime.setMultiply(standardRosterTime.getMultiply() - 1);
        if(standardRosterTime.getMultiply() == 0){
            standardRosterTimeRepository.delete(standardRosterTime);
            return null;
        }
        standardRosterTimeRepository.save(standardRosterTime);
        return null;
    }

    private LocalTime convertStringToLocaltime(String hour, String minute) {

        int hourTemp = Integer.parseInt(hour);
        int minuteTemp = Integer.parseInt(minute);
        return LocalTime.of(hourTemp, minuteTemp, 0, 0);
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

        int hourTemp = 0;
        int minuteTemp =0;
        if(!Objects.equals(hour, "")) {
             hourTemp = Integer.parseInt(hour);
        }
        if(!Objects.equals(minute, "")) {
            minuteTemp = Integer.parseInt(minute);
        }
        if (hourTemp >= 0 && hourTemp < 24) {
            return minuteTemp < 0 || minuteTemp >= 60;
        }
        return true;
    }

    private List<StandardRosterResponse> createStandardRosterResponse(Company company){

        List<StandardRosterDay> standardRosterDays = standardRosterDayRepository.findAllByCompany(company);
        List<StandardRosterResponse> standardRosterResponses = new ArrayList<>();
        for (StandardRosterDay standardRosterDay : standardRosterDays){
            List<StandardRosterTime> standardRosterTimes = standardRosterDay.getStandardRosterTimes();
            standardRosterTimes.sort(Comparator.comparing(StandardRosterTime::getStartTime));
            for (StandardRosterTime standardRosterTime : standardRosterTimes){
                for(int i = standardRosterTime.getMultiply(); i > 0; i--){
                    StandardRosterResponse standardRosterResponse1 = new StandardRosterResponse();
                    standardRosterResponse1.setDay(standardRosterDay.getDay());
                    standardRosterResponse1.setStartTime(standardRosterTime.getStartTime());
                    standardRosterResponse1.setEndTime(standardRosterTime.getEndTime());
                    standardRosterResponse1.setEndTimeNeeded(standardRosterTime.isEndTimeNeeded());
                    standardRosterResponse1.setTotalTimeInMinutes(standardRosterTime.getTotalTime());
                    standardRosterResponse1.setStartTimeHour(Integer.parseInt(standardRosterTime.getStartTimeHour()));
                    standardRosterResponse1.setStartTimeMinute(Integer.parseInt(standardRosterTime.getStartTimeMinute()));
                    standardRosterResponse1.setEndTimeHour(Integer.parseInt(standardRosterTime.getEndTimeHour()));
                    standardRosterResponse1.setEndTimeMinute(Integer.parseInt(standardRosterTime.getEndTimeMinute()));
                    standardRosterResponse1.setMultiply(standardRosterTime.getMultiply());
                    standardRosterResponses.add(standardRosterResponse1);
                }
            }
        }
        return standardRosterResponses;
    }


    private void saveNewStandardRosterTime(StandardRosterDay standardRosterDay, RosterRequest rosterRequest){

        StandardRosterTime standardRosterTime = new StandardRosterTime();
        standardRosterTime.setStartTime(convertStringToLocaltime(rosterRequest.getStartTimeHour(), rosterRequest.getStartTimeMinute()));
        standardRosterTime.setEndTime(convertStringToLocaltime(rosterRequest.getEndTimeHour(), rosterRequest.getEndTimeMinute()));
        standardRosterTime.setStartTimeHour(rosterRequest.getStartTimeHour());
        standardRosterTime.setStartTimeMinute(rosterRequest.getStartTimeMinute());
        standardRosterTime.setEndTimeHour(rosterRequest.getEndTimeHour());
        standardRosterTime.setEndTimeMinute(rosterRequest.getEndTimeMinute());
        standardRosterTime.setMultiply(1);
        standardRosterTime.setEndTimeNeeded(rosterRequest.isEndTime());
        standardRosterTime.setStandardRosterDay(standardRosterDay);
        standardRosterTime.setTotalTime(calculateTimeBetweenStartTimeAndEndTime(standardRosterTime.getStartTime(),standardRosterTime.getEndTime()));
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

    private long calculateTimeBetweenStartTimeAndEndTime(LocalTime startTime, LocalTime endTime ){

        return ChronoUnit.MINUTES.between(startTime, endTime);
    }

    private StandardRosterTime checkStandardRosterTimeExists(Company company, RosterRequest rosterRequest){

        StandardRosterDay standardRosterDay = standardRosterDayRepository.findByCompanyAndDay(company, rosterRequest.getDay().toUpperCase());
        if(standardRosterDay == null){
            return null;
        }
        List<StandardRosterTime> standardRosterTimes = standardRosterTimeRepository.findByStandardRosterDayId(standardRosterDay.getId());
        for(StandardRosterTime standardRosterTime : standardRosterTimes){
            if(Objects.equals(standardRosterTime.getStartTimeHour(), rosterRequest.getStartTimeHour())){
                if(Objects.equals(standardRosterTime.getStartTimeMinute(), rosterRequest.getStartTimeMinute())){
                    if(Objects.equals(standardRosterTime.getEndTimeHour(), rosterRequest.getEndTimeHour())){
                        if(Objects.equals(standardRosterTime.getEndTimeMinute(), rosterRequest.getEndTimeMinute())){
                            return standardRosterTime;
                        }
                    }
                }
            }
        }
        return null;
    }

}


