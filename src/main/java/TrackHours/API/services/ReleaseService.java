package TrackHours.API.services;

import TrackHours.API.DTO.Release.CreateReleaseDTO;
import TrackHours.API.DTO.Release.UpdateReleaseDTO;
import TrackHours.API.Exceptions.Release.ReleaseNotFoundException;
import TrackHours.API.Exceptions.Tasks.TaskNotFoundException;
import TrackHours.API.Exceptions.UsersExceptions.UserNotCollaboratorTaskException;
import TrackHours.API.Exceptions.UsersExceptions.UserNotFoundException;
import TrackHours.API.entities.Release;
import TrackHours.API.entities.Task;
import TrackHours.API.entities.User;
import TrackHours.API.repositories.ReleaseRepository;
import TrackHours.API.repositories.TaskRepository;
import TrackHours.API.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReleaseService {

    @Autowired
    private ReleaseRepository releaseRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    // Create release by user logged
    public Release createRelease(CreateReleaseDTO createReleaseDTO, Authentication userAuthenticated) {
        Task task = taskRepository.findById(createReleaseDTO.taskId())
                .orElseThrow(() -> new TaskNotFoundException("Tarefa não encontrada"));

        String email = userAuthenticated.getName();

        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        if (!task.getIntegrantes().contains(user)) {
            throw new UserNotCollaboratorTaskException("Usuário não faz parte da tarefa. Não é possível criar a lançamento!");
        }

        var releaseCreated = new Release(
                createReleaseDTO.description(),
                createReleaseDTO.dateRelease(),
                createReleaseDTO.startTime(),
                createReleaseDTO.endTime(),
                task,
                user
        );

        return releaseRepository.save(releaseCreated);
    }

    // Find all releases
    public List<Release> listAllReleases () {
        return releaseRepository.findAllNotDeleted();
    }

    // Find all releases by user logged
    public List<Release> getReleasesByUserLogged(Authentication authentication) {
        String email = authentication.getName();

        userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        return releaseRepository.findReleasesByUserLogged(email);
    }

    // Find Release by ID
    public Release getReleaseById(Long id) {
        return releaseRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ReleaseNotFoundException("Lançamento não encontrado"));
    }

    // Update release by ID com usuário logado
    public Release updateReleaseByID(Long id, UpdateReleaseDTO updateReleaseDTO, Authentication userAuthenticated) {
        // Busca a release pelo ID
        var releaseEntity = releaseRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ReleaseNotFoundException("Release não encontrada ou deletada com id: " + id));

        // Obtém o usuário autenticado
        String email = userAuthenticated.getName();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        // Verifica se o usuário faz parte da tarefa associada à release
        if (!releaseEntity.getTask().getIntegrantes().contains(user)) {
            throw new UserNotCollaboratorTaskException("Usuário não faz parte da tarefa. Não é possível atualizar o lançamento!");
        }

        // Atualiza os campos da release, se forem fornecidos no DTO
        if (updateReleaseDTO.description() != null && !updateReleaseDTO.description().equals(releaseEntity.getDescription())) {
            releaseEntity.setDescription(updateReleaseDTO.description());
        }
        if (updateReleaseDTO.dateRelease() != null && !updateReleaseDTO.dateRelease().equals(releaseEntity.getDateRelease())) {
            releaseEntity.setDateRelease(updateReleaseDTO.dateRelease());
        }
        if (updateReleaseDTO.startTime() != null && !updateReleaseDTO.startTime().equals(releaseEntity.getStartTime())) {
            releaseEntity.setStartTime(updateReleaseDTO.startTime());
        }
        if (updateReleaseDTO.endTime() != null && !updateReleaseDTO.endTime().equals(releaseEntity.getEndTime())) {
            releaseEntity.setEndTime(updateReleaseDTO.endTime());
        }

        // Atualiza a tarefa associada
        if (updateReleaseDTO.taskId() != null) {
            var taskEntity = taskRepository.findById(updateReleaseDTO.taskId())
                    .orElseThrow(() -> new TaskNotFoundException("Tarefa relacionada não encontrada: " + updateReleaseDTO.taskId()));

            // Verifica se o usuário faz parte da nova tarefa
            if (!taskEntity.getIntegrantes().contains(user)) {
                throw new UserNotCollaboratorTaskException("Usuário não faz parte da nova tarefa. Não é possível atualizar o lançamento!");
            }

            releaseEntity.setTask(taskEntity);
        }

        return releaseRepository.save(releaseEntity);
    }

    // Delete By ID com validação do usuário autenticado
    public boolean deleteById(Long id, Authentication authentication) {
        // Busca a release pelo ID
        var release = releaseRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ReleaseNotFoundException("Release não encontrada com id: " + id));

        // Obtém o usuário autenticado
        String email = authentication.getName();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        // Verifica se o usuário faz parte da tarefa associada à release
        if (!release.getTask().getIntegrantes().contains(user)) {
            throw new UserNotCollaboratorTaskException("Usuário não faz parte da tarefa. Não é possível deletar o lançamento!");
        }

        releaseRepository.deleteById(id);

        return true;
    }

    // Find Releases and Total Hours Launched by User
    public List<Object[]> getLaunchesAndTotalHoursByUser() {
        return releaseRepository.findLaunchesAndTotalHoursByUser();
    }
}
