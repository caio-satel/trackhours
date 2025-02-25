package TrackHours.API.DTO.User;

import TrackHours.API.enumTypes.roles.UserRole;

import java.util.List;

public record UserNoProjectsResponseDTO(Long id, String name, String email, UserRole role, List<String> projects) {
}
