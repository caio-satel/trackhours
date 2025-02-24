package TrackHours.API.DTO.User;

import TrackHours.API.entities.Project;

import java.util.List;

public record UserResponseDTO (Long id, String name, String email, String role, String createdAt, List<Project> projects){
}
