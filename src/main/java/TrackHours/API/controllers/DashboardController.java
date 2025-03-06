package TrackHours.API.controllers;

import TrackHours.API.DTO.Dashboard.DashboardProjectDTO;
import TrackHours.API.DTO.Dashboard.HoursWorkedDTO;
import TrackHours.API.DTO.mapper.DashboardMapper;
import TrackHours.API.entities.Project;
import TrackHours.API.services.DashboardService;
import TrackHours.API.services.ReleaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private DashboardMapper dashboardMapper;

    @GetMapping("/byUser")
    public ResponseEntity<List<DashboardProjectDTO>> getDashboardData(Authentication authentication) {
        List<DashboardProjectDTO> dashboardData = dashboardService.getDashboardData(authentication);
        return ResponseEntity.ok(dashboardData);
    }

    @GetMapping("/hoursWorked")
    public ResponseEntity<HoursWorkedDTO> getTotalHoursWorked(Authentication authentication) {
        String totalHours = dashboardService.getTotalHoursWorked(authentication);
        HoursWorkedDTO response = new HoursWorkedDTO(totalHours);
        return ResponseEntity.ok(response);
    }
}
