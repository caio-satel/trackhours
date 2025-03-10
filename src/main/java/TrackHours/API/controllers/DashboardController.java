package TrackHours.API.controllers;

import TrackHours.API.DTO.Dashboard.DashboardProjectDTO;
import TrackHours.API.DTO.Dashboard.HoursWorkedDTO;
import TrackHours.API.DTO.Dashboard.LateTasksCountDTO;
import TrackHours.API.services.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@Tag(name = "Dashboard", description = "Endpoints da dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @Operation(summary = "Obter projetos e tarefas apenas do usuário logado")
    @GetMapping("/byUser")
    public ResponseEntity<List<DashboardProjectDTO>> getDashboardData(Authentication authentication) {
        List<DashboardProjectDTO> dashboardData = dashboardService.getDashboardData(authentication);
        return ResponseEntity.ok(dashboardData);
    }

    @Operation(summary = "Obter horas trabalhadas do usuário logado")
    @GetMapping("/hoursWorked")
    public ResponseEntity<HoursWorkedDTO> getTotalHoursWorked(Authentication authentication) {
        String totalHours = dashboardService.getTotalHoursWorked(authentication);
        HoursWorkedDTO response = new HoursWorkedDTO(totalHours);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obter quantidade de tarefas atrasadas do usuário logado")
    @GetMapping("/lateTasks")
    public ResponseEntity<LateTasksCountDTO> getLateTasksCount(Authentication authentication) {
        LateTasksCountDTO lateTasksCount = dashboardService.getLateTasksCount(authentication);
        return ResponseEntity.ok(lateTasksCount);
    }

}
