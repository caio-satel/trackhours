package TrackHours.API.services;

import TrackHours.API.DTO.Project.CreateProjectDTO;
import TrackHours.API.DTO.Project.ProjectResponseDTO;
import TrackHours.API.DTO.Project.UpdateProjectDTO;
import TrackHours.API.DTO.User.UpdateUserDTO;
import TrackHours.API.entities.Project;
import TrackHours.API.entities.User;
import TrackHours.API.enumTypes.projects.StatusProject;
import TrackHours.API.repositories.ProjectRepository;
import TrackHours.API.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public Long createProject(CreateProjectDTO createProjectDTO) {
        User responsibleUser = userRepository.findById(createProjectDTO.responsibleUser())
                .orElseThrow(() -> new RuntimeException("Usuário com esse ID não encontrado: " + createProjectDTO.responsibleUser()));

        var projectCreated = new Project(
                createProjectDTO.name(),
                createProjectDTO.startDate(),
                createProjectDTO.endDate(),
                responsibleUser,
                StatusProject.PLANNED,
                createProjectDTO.priority());
        var projectSaved = projectRepository.save(projectCreated);

        return projectSaved.getId();
    }

    // Find All User
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    // Find By ID
    public Optional<Project> findProjectById(Long id) {
        return projectRepository.findById(id);
    }

    // Update User By ID
    public boolean updateProjectById(Long id, UpdateProjectDTO updateProjectDTO) {
        var projectEntity = projectRepository.findById(id);

        if(projectEntity.isPresent()) {
            var project = projectEntity.get();

            var userEntity = userRepository.findById(updateProjectDTO.responsibleUser());
            if (userEntity.isEmpty()) {
                return false; // Usuário não foi encontrado
            }

            if (updateProjectDTO.name() != null
                    && updateProjectDTO.startDate() != null
                    && updateProjectDTO.endDate() != null
                    && updateProjectDTO.priority() != null) {
                project.setName(updateProjectDTO.name());
                project.setStartDate(updateProjectDTO.startDate());
                project.setEndDate(updateProjectDTO.endDate());
                project.setResponsibleUser(userEntity.get());
                project.setPriority(updateProjectDTO.priority());
                project.setStatus(updateProjectDTO.status());

                projectRepository.save(project);

                return true;
            }
        }
        return false;
    }

    // Delete By ID
    public boolean deleteById(Long id) {
        var projectExists = projectRepository.existsById(id);

        if(projectExists) {
            projectRepository.deleteById(id);
        }

        return projectExists;
    }
}
