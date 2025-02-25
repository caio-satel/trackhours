package TrackHours.API.DTO.Project;

import TrackHours.API.entities.User;
import TrackHours.API.enumTypes.projects.PriorityProject;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record UpdateProjectDTO(String name,
                               @JsonFormat(pattern = "dd/MM/yyyy")
                               LocalDate startDate,
                               @JsonFormat(pattern = "dd/MM/yyyy")
                               LocalDate endDate,
                               Long responsibleUser,
                               PriorityProject priority) {
}
