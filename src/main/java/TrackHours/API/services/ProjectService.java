package TrackHours.API.services;

import TrackHours.API.DTO.Project.CreateProjectDTO;
import TrackHours.API.DTO.Project.UpdateProjectDTO;
import TrackHours.API.Exceptions.ProjectExceptions.ProjectNotFoundException;
import TrackHours.API.Exceptions.UsersExceptions.UserNotFoundException;
import TrackHours.API.entities.Project;
import TrackHours.API.entities.Task;
import TrackHours.API.entities.User;
import TrackHours.API.enumTypes.projects.StatusProject;
import TrackHours.API.enumTypes.tasks.StatusTask;
import TrackHours.API.repositories.ProjectRepository;
import TrackHours.API.repositories.TaskRepository;
import TrackHours.API.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    // Create project
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
        return projectRepository.findAllNotDeleted();
    }

    // Find By Project by ID
    public Project findProjectById(Long id) {
        return projectRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ProjectNotFoundException("Projeto não encontrado - ID: " + id));
    }

    // Update Project By ID
    public Project updateProjectById(Long id, UpdateProjectDTO updateProjectDTO) {
        Project project = projectRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ProjectNotFoundException("Projeto não encontrado"));

        User responsibleUser = userRepository.findById(updateProjectDTO.responsibleUser())
                .orElseThrow(() -> new UserNotFoundException("Usuário responsável não encontrado"));

        // Verifica se o status está sendo alterado para DONE
        if (updateProjectDTO.status() == StatusProject.DONE) {
            // Busca todas as tarefas relacionadas ao projeto (não deletadas)
            List<Task> tasks = taskRepository.findByProjectIdAndNotDeleted(project.getId());

            // Verifica se todas as tarefas estão com status DONE
            boolean allTasksDone = tasks.stream()
                    .allMatch(task -> task.getStatus() == StatusTask.DONE);

            if (!allTasksDone) {
                throw new IllegalStateException("Não é possível concluir o projeto. Todas as tarefas relacionadas devem estar concluídas.");
            }
        }

        project.setName(updateProjectDTO.name());
        project.setStartDate(updateProjectDTO.startDate());
        project.setEndDate(updateProjectDTO.endDate());
        project.setResponsibleUser(responsibleUser);
        project.setPriority(updateProjectDTO.priority());
        project.setStatus(updateProjectDTO.status());

        return projectRepository.save(project);
    }

    // Delete By Project ID
    public void deleteById(Long id, Authentication authentication) {
        Project project = projectRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ProjectNotFoundException("Projeto não encontrado ou foi excluído"));

        String email = authentication.getName();

        // Buscar o usuário logado
        User loggedUser = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuário logado não encontrado"));

        // Verifica se o usuário logado é o responsibleUser do projeto
        if (!project.getResponsibleUser().getId().equals(loggedUser.getId())) {
            throw new IllegalStateException("Não é possível excluir o projeto. Apenas o responsável pelo projeto pode excluí-lo.");
        }

        // Verificar se o status do projeto é DONE
        if (project.getStatus() != StatusProject.DONE) {
            throw new IllegalStateException("Não é possível excluir o projeto. O projeto deve estar concluído.");
        }

        // Buscar todas as tarefas relacionadas ao projeto
        List<Task> tasks = taskRepository.findByProjectIdAndNotDeleted(project.getId());

        // Verifica se todas as tarefas estão com status DONE
        boolean allTasksDone = tasks.stream()
                .allMatch(task -> task.getStatus() == StatusTask.DONE);

        if (!allTasksDone) {
            throw new IllegalStateException("Não é possível excluir o projeto. Todas as tarefas relacionadas devem estar concluídas.");
        }

        projectRepository.delete(project);
    }

    // Get total hours launched by projects in last 30 days
    public List<Object[]> getTotalHoursByProjectLast30Days() {
        LocalDateTime dataInicio = LocalDateTime.now().minusDays(30); // Calcula a data de início (últimos 30 dias)
        return projectRepository.getTotalHoursByProjectLast30Days(dataInicio);
    }

    // Get ongoing tasks by projects
    public List<Object[]> getOngoingTasksByProject() {
        return projectRepository.getOngoingTasksByProject();
    }
}
