package TrackHours.API.services;

import TrackHours.API.DTO.Project.CreateProjectDTO;
import TrackHours.API.DTO.Project.ProjectResponseDTO;
import TrackHours.API.DTO.Project.UpdateProjectDTO;
import TrackHours.API.DTO.User.UpdateUserDTO;
import TrackHours.API.Exceptions.ProjectExceptions.ProjectNotFoundException;
import TrackHours.API.Exceptions.UsersExceptions.UserNotFoundException;
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

    public Project createProject(CreateProjectDTO createProjectDTO) {
        User responsibleUser = userRepository.findById(createProjectDTO.responsibleUser())
                .orElseThrow(() -> new UserNotFoundException("Usuário com esse ID não encontrado: " + createProjectDTO.responsibleUser()));

        var projectCreated = new Project(
                createProjectDTO.name(),
                createProjectDTO.startDate(),
                createProjectDTO.endDate(),
                responsibleUser,
                StatusProject.PLANNED,
                createProjectDTO.priority());

        return projectRepository.save(projectCreated);
    }

    // Find All User
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    // Find By ID
    public Project findProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Projeto não encontrado - ID: " + id));
    }

    // Update User By ID
    public Project updateProjectById(Long id, UpdateProjectDTO updateProjectDTO) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Projeto não encontrado"));

        User responsibleUser = userRepository.findById(updateProjectDTO.responsibleUser())
                .orElseThrow(() -> new UserNotFoundException("Usuário responsável não encontrado"));

        project.setName(updateProjectDTO.name());
        project.setStartDate(updateProjectDTO.startDate());
        project.setEndDate(updateProjectDTO.endDate());
        project.setResponsibleUser(responsibleUser);
        project.setPriority(updateProjectDTO.priority());
        project.setStatus(updateProjectDTO.status());

        return projectRepository.save(project);
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
