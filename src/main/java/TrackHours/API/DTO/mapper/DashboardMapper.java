package TrackHours.API.DTO.mapper;

import TrackHours.API.DTO.Dashboard.DashboardProjectDTO;
import TrackHours.API.DTO.Dashboard.DashboardTaskDTO;
import TrackHours.API.DTO.User.UserDTO;
import TrackHours.API.entities.Project;
import TrackHours.API.entities.Task;
import TrackHours.API.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardMapper {

    public DashboardProjectDTO projectToDashboardProjectDTO(Project project, List<Task> tasks) {
        return new DashboardProjectDTO(
                project.getId(),
                project.getName(),
                project.getStartDate(),
                project.getEndDate(),
                project.getStatus(),
                project.getPriority(),
                new UserDTO(project.getResponsibleUser().getId(), project.getResponsibleUser().getName()),
                tasks.stream()
                        .map(this::taskToDashboardTaskDTO)
                        .collect(Collectors.toList())
        );
    }

    public DashboardTaskDTO taskToDashboardTaskDTO(Task task) {
        return new DashboardTaskDTO(
                task.getId(),
                task.getName(),
                task.getStartDate(),
                task.getEndDate(),
                task.getStatus()
        );
    }
}
