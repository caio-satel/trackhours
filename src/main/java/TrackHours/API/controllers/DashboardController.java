package TrackHours.API.controllers;

import TrackHours.API.DTO.Dashboard.DashboardProjectDTO;
import TrackHours.API.DTO.Dashboard.HoursWorkedDTO;
import TrackHours.API.DTO.Dashboard.LateTasksCountDTO;
import TrackHours.API.services.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

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

    @GetMapping("/lateTasks")
    public ResponseEntity<LateTasksCountDTO> getLateTasksCount(Authentication authentication) {
        LateTasksCountDTO lateTasksCount = dashboardService.getLateTasksCount(authentication);
        return ResponseEntity.ok(lateTasksCount);
    }

}
