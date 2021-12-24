package nl.spaan.personeels_app.service;


import nl.spaan.personeels_app.model.*;
import nl.spaan.personeels_app.payload.response.*;
import nl.spaan.personeels_app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

@Service
public class WeekRosterService {

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

    private WeekRosterRepository weekRosterRepository;

    @Autowired
    public void setWeekRosterRepository(WeekRosterRepository weekRosterRepository) {
        this.weekRosterRepository = weekRosterRepository;
    }

    private WeekDayRepository weekDayRepository;

    @Autowired
    public void setWeekDayRepository(WeekDayRepository weekDayRepository) {
        this.weekDayRepository = weekDayRepository;
    }

    private ShiftRepository shiftRepository;

    @Autowired
    public void setShiftRepository(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }

    public ResponseEntity<?> getWeekRoster(String authorization, String weekNumber, String year) {

        Company company = userService.getUserFromToken(authorization).getCompany();
        if(!standardRosterDayRepository.existsById(company.getId()))
        {
            return ResponseEntity.ok("Er is nog geen standard rooster gemaakt");
        }
        int weekNumberInInt = parseInt(weekNumber);
        int yearInInt = parseInt(year);
        WeekRoster weekRoster = weekRosterRepository.findByCompanyAndWeekAndYear(company,weekNumberInInt,yearInInt);
        if (weekRoster == null){
            createNewWeekRoster(company,weekNumberInInt,yearInInt);
        }
        weekRoster = weekRosterRepository.findByCompanyAndWeekAndYear(company,weekNumberInInt,yearInInt);
        checkIfShiftExists(weekRoster);
        return ResponseEntity.ok(createStandardRosterResponse(weekRoster.getCompany()));
    }

    private void createNewWeekRoster(Company company, int weekNumber, int year){

        WeekRoster weekRoster = new WeekRoster();
        weekRoster.setCompany(company);
        weekRoster.setWeek(weekNumber);
        weekRoster.setYear(year);
        weekRosterRepository.save(weekRoster);
        createNewShift(weekRoster);
    }

    private void createNewShift(WeekRoster weekRoster){

        List<StandardRosterDay> standardRosterDayList = standardRosterDayRepository.findAllByCompany(weekRoster.getCompany());
            for (StandardRosterDay standardRosterDay : standardRosterDayList) {
                WeekDay weekDay = new WeekDay();
                weekDay.setDay(standardRosterDay.getDay());
                weekDay.setWeekRoster(weekRoster);
                weekDayRepository.save(weekDay);
                List<StandardRosterTime> standardRosterTimes = standardRosterTimeRepository.findByStandardRosterDayId(standardRosterDay.getId());
                for (StandardRosterTime standardRosterTime : standardRosterTimes) {
                    for (int i = 0; i < standardRosterTime.getMultiply(); i++) {
                        createNewShift(weekDay, standardRosterTime);
                    }
                }
            }
    }

    private void checkIfShiftExists(WeekRoster weekRoster){
        
        List<WeekDay> weekDayList = weekDayRepository.findByWeekRosterId(weekRoster.getId());
        for (WeekDay weekDay: weekDayList){
            StandardRosterDay standardRosterDay = standardRosterDayRepository.findByCompanyAndDay(weekRoster.getCompany(),weekDay.getDay());
            List<StandardRosterTime> standardRosterTimeList = standardRosterTimeRepository.findByStandardRosterDayId(standardRosterDay.getId());
            for(StandardRosterTime standardRosterTime: standardRosterTimeList){
                List<Shift> shiftList = shiftRepository.findByStandardRosterTimeId(standardRosterTime.getId());
                if(shiftList.size() < standardRosterTime.getMultiply()-1){
                    for(int i = 0; i < standardRosterTime.getMultiply()- shiftList.size(); i++){
                        createNewShift(weekDay, standardRosterTime);
                    }
                }
                if(shiftList.size() > standardRosterTime.getMultiply()-1){
                    for(int i = 0; i < shiftList.size() - standardRosterTime.getMultiply() ; i++){
                        if(shiftList.get(i).getUser() == null){
                            shiftRepository.delete(shiftList.get(i));
                        }
                    }
                }
            }
        }
    }

    private void createNewShift(WeekDay weekDay, StandardRosterTime standardRosterTime) {
        Shift newShift = new Shift();
        newShift.setWeekDay(weekDay);
        newShift.setStartTime(standardRosterTime.getStartTime());
        newShift.setEndTime(standardRosterTime.getEndTime());
        newShift.setMultiply(standardRosterTime.getMultiply());
        newShift.setStandardRosterTime(standardRosterTime);
        shiftRepository.save(newShift);
    }

    private WeekRosterResponse createWeekRosterResponse(WeekRoster weekRoster){

        WeekRosterResponse weekRosterResponse = new WeekRosterResponse();
        weekRosterResponse.setId(weekRoster.getId());
        weekRosterResponse.setYear(weekRoster.getYear());
        weekRosterResponse.setWeek(weekRoster.getWeek());
        List<WeekDay> weekDays = weekDayRepository.findByWeekRosterId(weekRoster.getId());
        List<WeekDayResponse> weekDayResponses = new ArrayList<>();
        for (WeekDay weekDay : weekDays) {
            WeekDayResponse weekDayResponse = new WeekDayResponse();
            weekDayResponse.setDay(weekDay.getDay());
            List<Shift> shifts = shiftRepository.findByWeekDayId(weekDay.getId());
            List<ShiftResponse> shiftResponses = new ArrayList<>();
            for(Shift shift: shifts){
                ShiftResponse shiftResponse = new ShiftResponse();
                shiftResponse.setStartShift(shift.getStartTime());
                shiftResponse.setEndShift(shift.getEndTime());
                shiftResponses.add(shiftResponse);
            }
            weekDayResponse.setShiftResponseList(shiftResponses);
            weekDayResponses.add(weekDayResponse);
        }
        weekRosterResponse.setWeekDayResponses(weekDayResponses);
        return weekRosterResponse;
    }

    public ResponseEntity<?> createWeekRoster(String authorization, WeekRoster weekRoster) {

        weekRoster.setCompany(userService.getUserFromToken(authorization).getCompany());
        if(!checkIfWeekRosterExist(weekRoster)) {
            createNewWeekRoster(weekRoster.getCompany(), weekRoster.getWeek(), weekRoster.getYear());
        }
        weekRoster.setId(weekRosterRepository.findByCompanyAndWeekAndYear(weekRoster.getCompany(),weekRoster.getWeek(),weekRoster.getYear()).getId());
        checkIfShiftExists(weekRoster);
        return ResponseEntity.ok(createStandardRosterResponse(weekRoster.getCompany()));
    }

    private boolean checkIfWeekRosterExist(WeekRoster weekRoster){


        return weekRosterRepository.existsByCompanyAndWeekAndYear(weekRoster.getCompany(),weekRoster.getWeek(),weekRoster.getYear());
    }

    public List<DayResponse> createStandardRosterResponse(Company company){

        List<DayResponse> dayResponses = new ArrayList<>();
        List<StandardRosterDay> standardRosterDays = standardRosterDayRepository.findAllByCompany(company);
        for (StandardRosterDay standardRosterDay: standardRosterDays){
            DayResponse dayResponse = new DayResponse();
            dayResponse.setDay(standardRosterDay.getDay());
            List<StandardRosterTime> standardRosterTimes = standardRosterTimeRepository.findByStandardRosterDayId(standardRosterDay.getId());
            List<StandardRosterResponseSmall> standardRosterResponseList = new ArrayList<>();
            for(StandardRosterTime standardRosterTime: standardRosterTimes){
                StandardRosterResponseSmall rosterResponse = new StandardRosterResponseSmall();
                rosterResponse.setId(standardRosterTime.getId());
                rosterResponse.setStartTime(standardRosterTime.getStartTime());
                rosterResponse.setEndTime(standardRosterTime.getEndTime());
                rosterResponse.setMultiply(standardRosterTime.getMultiply());
                standardRosterResponseList.add(rosterResponse);
            }
            dayResponse.setTimes(standardRosterResponseList);
            dayResponses.add(dayResponse);
        }

        return dayResponses;
    }

}
