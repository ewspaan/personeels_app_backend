package nl.spaan.personeels_app.controller;


import nl.spaan.personeels_app.payload.request.RosterRequest;
import nl.spaan.personeels_app.service.RosterService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/roster")
public class RosterController {

    private RosterService rosterService;

    @Autowired
    public void setRosterService(RosterService rosterService) {
        this.rosterService = rosterService;
    }

    @PostMapping(value = "/standard")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> setStandardRoster(@RequestBody RosterRequest rosterRequest,
                                               @RequestHeader Map<String, String> headers) {
        return rosterService.setStandard(headers.get("authorization"),rosterRequest);
    }

    @GetMapping("/roster")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> getStandardRoster(@RequestHeader Map<String, String> headers){
        return rosterService.getStandardRoster(headers.get("authorization"));
    }

}

//,consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}