package TrackHours.API.DTO.Release;

import TrackHours.API.DTO.Task.TaskSimpleDTO;
import TrackHours.API.DTO.User.UserDTO;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record ReleaseDTO(Long id,
                         String description,
                         @JsonFormat(pattern = "dd/MM/yyyy")
                         LocalDate dateRelease,
                         @JsonFormat(pattern = "HH:mm")
                         LocalTime startTime,
                         @JsonFormat(pattern = "HH:mm")
                         LocalTime endTime,
                         @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
                         LocalDateTime createdAt,
                         TaskSimpleDTO task,
                         UserDTO user) {
}
