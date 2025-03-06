package TrackHours.API.DTO.Dashboard;

import TrackHours.API.DTO.User.UserDTO;
import TrackHours.API.enumTypes.projects.PriorityProject;
import TrackHours.API.enumTypes.projects.StatusProject;

import java.time.LocalDate;
import java.util.List;

public record DashboardProjectDTO(Long id,
                                  String name,
                                  LocalDate startDate,
                                  LocalDate endDate,
                                  StatusProject status,
                                  PriorityProject priority,
                                  UserDTO responsibleUser,
                                  List<DashboardTaskDTO> tasks) {
}
