package TrackHours.API.DTO.User;

import TrackHours.API.enumTypes.roles.UserRole;

public record RegisterDTO(String name, String email, String password, UserRole role) {
}
