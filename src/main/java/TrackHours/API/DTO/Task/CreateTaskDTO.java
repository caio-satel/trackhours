package TrackHours.API.DTO.Task;

import TrackHours.API.entities.Project;
import TrackHours.API.entities.User;
import TrackHours.API.enumTypes.tasks.StatusTask;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

public record CreateTaskDTO(String name,
                            @JsonFormat(pattern = "dd/MM/yyyy")
                            LocalDate startDate,
                            @JsonFormat(pattern = "dd/MM/yyyy")
                            LocalDate endDate,
                            Long projectId,
                            List<Long> collaborators) {
}
