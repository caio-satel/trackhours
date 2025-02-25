package TrackHours.API.DTO.Project;

import TrackHours.API.DTO.User.UserNoProjectsResponseDTO;
import TrackHours.API.entities.User;
import TrackHours.API.enumTypes.projects.PriorityProject;
import TrackHours.API.enumTypes.projects.StatusProject;

import java.time.LocalDate;

public record ProjectResponseDTO (Long id,
                                  String name,
                                  LocalDate startDate,
                                  LocalDate endDate,
                                  StatusProject status,
                                  PriorityProject priority,
                                  UserNoProjectsResponseDTO responsibleUser) {
}
