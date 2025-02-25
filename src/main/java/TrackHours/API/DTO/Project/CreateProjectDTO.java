package TrackHours.API.DTO.Project;

import TrackHours.API.enumTypes.projects.PriorityProject;
import TrackHours.API.enumTypes.projects.StatusProject;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record CreateProjectDTO(String name,
                               @JsonFormat(pattern = "dd/MM/yyyy")
                               LocalDate startDate,
                               @JsonFormat(pattern = "dd/MM/yyyy")
                               LocalDate endDate,
                               Long responsibleUser,
                               StatusProject status,
                               PriorityProject priority) {
}
