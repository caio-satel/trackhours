package TrackHours.API.services;

import TrackHours.API.DTO.Task.CreateTaskDTO;
import TrackHours.API.DTO.Task.TaskDTO;
import TrackHours.API.DTO.Task.UpdateTaskDTO;
import TrackHours.API.DTO.User.UpdateUserDTO;
import TrackHours.API.DTO.User.UserDTO;
import TrackHours.API.DTO.mapper.TaskMapper;
import TrackHours.API.Exceptions.ProjectExceptions.ProjectNotFoundException;
import TrackHours.API.Exceptions.Tasks.TaskNotFoundException;
import TrackHours.API.Exceptions.UsersExceptions.UserNotFoundException;
import TrackHours.API.entities.Project;
import TrackHours.API.entities.Task;
import TrackHours.API.entities.User;
import TrackHours.API.enumTypes.tasks.StatusTask;
import TrackHours.API.repositories.ProjectRepository;
import TrackHours.API.repositories.TaskRepository;
import TrackHours.API.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskMapper map;

    // Create Task
    public Task createTask(CreateTaskDTO createTaskDTO) {
        Project project = projectRepository.findById(createTaskDTO.projectId())
                .orElseThrow(() -> new ProjectNotFoundException("Projeto não encontrado"));

        // Valida as datas da tarefa em relação ao projeto
        if (createTaskDTO.startDate().isBefore(project.getStartDate())) {
            throw new IllegalArgumentException("A data de início da tarefa não pode ser anterior à data de início do projeto.");
        }
        if (createTaskDTO.endDate().isAfter(project.getEndDate())) {
            throw new IllegalArgumentException("A data de término da tarefa não pode ser posterior à data de término do projeto.");
        }

        List<User> collaborators = userRepository.findAllById(createTaskDTO.collaborators());

        var taskCreated = new Task(
                createTaskDTO.name(),
                createTaskDTO.startDate(),
                createTaskDTO.endDate(),
                project,
                StatusTask.OPEN);

        taskCreated.setIntegrantes(collaborators);

        return taskRepository.save(taskCreated);
    }

    // Find Task by ID
    public TaskDTO getTaskDTOById(Long id) {
        Task task = taskRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new TaskNotFoundException("Tarefa não encontrada ou foi desativada"));

        return map.taskToTaskDTO(task);
    }

    // Find All Tasks
    public List<Task> getAllTasks() {
        return taskRepository.findAllNotDeleted();
    }

    // Find All Tasks by User Logged
    public List<Task> getTasksForCurrentUser(Authentication authentication) {
        String email = authentication.getName();

        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        return taskRepository.findTasksByUser(user);
    }

    // Find All Late Tasks by User Logged
    public List<Task> getLateTasksByUser(Authentication authentication) {
        String email = authentication.getName();

        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        return taskRepository.findLateTasksByUser(email);
    }

    // Update Task by ID
    public Task updateTaskById(Long id, UpdateTaskDTO updateTaskDTO) {
        var task = taskRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new TaskNotFoundException("Tarefa não encontrada ou foi desativada"));

            // Atualiza apenas os campos que não são nulos no DTO
            if (updateTaskDTO.name() != null) {
                task.setName(updateTaskDTO.name());
            }
            if (updateTaskDTO.startDate() != null) {
                task.setStartDate(updateTaskDTO.startDate());
            }
            if (updateTaskDTO.endDate() != null) {
                task.setEndDate(updateTaskDTO.endDate());
            }
            if (updateTaskDTO.status() != null) {
                task.setStatus(updateTaskDTO.status());
            }

            // Atualiza o projeto, se fornecido
            if (updateTaskDTO.projectId() != null) {
                var project = projectRepository.findById(updateTaskDTO.projectId())
                        .orElseThrow(() -> new ProjectNotFoundException("Projeto não encontrado"));
                task.setProject(project);
            }

            // Atualiza a lista de colaboradores, se fornecida
            if (updateTaskDTO.collaborators() != null) {
                // Lista de IDs dos novos colaboradores
                List<Long> newCollaboratorIds = updateTaskDTO.collaborators();

                // Remove colaboradores que não estão mais na lista
                List<User> currentCollaborators = new ArrayList<>(task.getIntegrantes());
                for (User collaborator : currentCollaborators) {
                    if (!newCollaboratorIds.contains(collaborator.getId())) {
                        task.removeCollaborator(collaborator);
                    }
                }

                // Adiciona os novos colaboradores
                List<User> collaboratorsToAdd = userRepository.findAllById(newCollaboratorIds);
                for (User collaborator : collaboratorsToAdd) {
                    if (!task.getIntegrantes().contains(collaborator)) {
                        task.addCollaborator(collaborator);
                    }
                }
            }

            taskRepository.save(task);
            return task;
    }


    // Delete By ID
    public void deleteById(Long id) {
        var task = taskRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new TaskNotFoundException("Tarefa não encontrada ou foi desativada"));

        // Verifica se a tarefa está concluída
        if (task.getStatus() != StatusTask.DONE) {
            throw new IllegalStateException("A tarefa não pode ser excluída porque não está concluída.");
        }

        taskRepository.delete(task);
    }

}
