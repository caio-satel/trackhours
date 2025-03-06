package TrackHours.API.DTO.Dashboard;

import TrackHours.API.enumTypes.tasks.StatusTask;

import java.time.LocalDate;

public record DashboardTaskDTO(Long id,
                               String name,
                               LocalDate startDate,
                               LocalDate endDate,
                               StatusTask status) {
}
