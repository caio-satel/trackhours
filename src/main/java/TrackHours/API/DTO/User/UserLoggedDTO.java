package TrackHours.API.DTO.User;

import TrackHours.API.enumTypes.roles.UserRole;

public record UserLoggedDTO(String name, UserRole role) {
}
