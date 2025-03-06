package TrackHours.API.DTO.Project;

import TrackHours.API.DTO.User.UserNoProjectsResponseDTO;
import TrackHours.API.entities.User;
import TrackHours.API.enumTypes.projects.PriorityProject;
import TrackHours.API.enumTypes.projects.StatusProject;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record ProjectResponseDTO (Long id,
                                  String name,
                                  @JsonFormat(pattern = "dd/MM/yyyy")
                                  LocalDate startDate,
                                  @JsonFormat(pattern = "dd/MM/yyyy")
                                  LocalDate endDate,
                                  StatusProject status,
                                  PriorityProject priority,
                                  UserNoProjectsResponseDTO responsibleUser) {
}
