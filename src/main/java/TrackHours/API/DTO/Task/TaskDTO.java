package TrackHours.API.DTO.Task;

import TrackHours.API.DTO.Project.ProjectDTO;
import TrackHours.API.DTO.User.UserDTO;
import TrackHours.API.enumTypes.tasks.StatusTask;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

public record TaskDTO(Long id,
                      String name,
                      @JsonFormat(pattern = "dd/MM/yyyy")
                      LocalDate startDate,
                      @JsonFormat(pattern = "dd/MM/yyyy")
                      LocalDate endDate,
                      StatusTask status,
                      ProjectDTO project,
                      List<UserDTO> collaborators) {
}
