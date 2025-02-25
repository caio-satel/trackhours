package TrackHours.API.DTO.mapper;

import TrackHours.API.DTO.Project.ProjectResponseDTO;
import TrackHours.API.DTO.User.UserNoProjectsResponseDTO;
import TrackHours.API.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMapper {

    public UserNoProjectsResponseDTO userDtoToUser(User dto) {

        List<String> projectNames = dto.getProjects().stream()
                .map(project -> project.getName()) // Pegando apenas o nome do projeto
                .collect(Collectors.toList());

        return new UserNoProjectsResponseDTO(
                dto.getId(),
                dto.getName(),
                dto.getEmail(),
                dto.getRole(),
                projectNames
        );
    }
}
