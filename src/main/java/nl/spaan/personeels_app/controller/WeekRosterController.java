package nl.spaan.personeels_app.controller;


import nl.spaan.personeels_app.model.WeekRoster;
import nl.spaan.personeels_app.service.WeekRosterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/week-roster")
public class WeekRosterController {

    private WeekRosterService weekRosterService;

    @Autowired
    public void setWeekRosterService(WeekRosterService weekRosterService) {
        this.weekRosterService = weekRosterService;
    }

    @GetMapping("/{week_number}/{year}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> getWeekRoster(@RequestHeader Map<String, String> headers,
                                           @PathVariable("week_number") String weekNumber,
                                           @PathVariable("year") String year){
        return weekRosterService.getWeekRoster(headers.get("authorization"), weekNumber, year);
    }

    @PostMapping("/create-week-roster")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> createWeekRoster(@RequestHeader Map<String, String> headers, WeekRoster weekRoster){
        return weekRosterService.createWeekRoster(headers.get("authorization"),weekRoster);
    }


}
