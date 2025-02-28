package TrackHours.API.DTO.mapper;

import TrackHours.API.DTO.Project.ProjectDTO;
import TrackHours.API.DTO.Task.TaskDTO;
import TrackHours.API.DTO.User.UserDTO;
import TrackHours.API.entities.Task;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskMapper {

    public TaskDTO taskToTaskDTO(Task task) {
        // Converte o projeto para DTO
        ProjectDTO projectDTO = new ProjectDTO(
                task.getProject().getId(),
                task.getProject().getName()
        );

        // Converte os usuários (colaboradores) para DTO
        List<UserDTO> collaboratorsDTO = task.getIntegrantes().stream()
                .map(user -> new UserDTO(user.getId(), user.getName()))
                .toList();

        // Retorna o TaskDTO com os dados mapeados
        return new TaskDTO(
                task.getId(),
                task.getName(),
                task.getStartDate(),
                task.getEndDate(),
                task.getStatus(),
                projectDTO,
                collaboratorsDTO
        );
    }
}


