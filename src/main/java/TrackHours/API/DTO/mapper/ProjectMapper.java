package TrackHours.API.DTO.mapper;

import TrackHours.API.DTO.Project.ProjectResponseDTO;
import TrackHours.API.entities.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectMapper {

    @Autowired
    private UserMapper map;

    public ProjectResponseDTO projectDtoToProject(Project dto) {
        return new ProjectResponseDTO(
                dto.getId(),
                dto.getName(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getStatus(),
                dto.getPriority(),
                map.userDtoToUser(dto.getResponsibleUser())
        );
    }
}
