package TrackHours.API.DTO.Release;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateReleaseDTO(String description,
                               @JsonFormat(pattern = "dd/MM/yyyy")
                               LocalDate dateRelease,
                               @JsonFormat(pattern = "HH:mm")
                               LocalTime startTime,
                               @JsonFormat(pattern = "HH:mm")
                               LocalTime endTime,
                               Long taskId) {
}
