package TrackHours.API.services;

import TrackHours.API.DTO.Release.CreateReleaseDTO;
import TrackHours.API.DTO.Release.ReleaseDTO;
import TrackHours.API.DTO.Release.UpdateReleaseDTO;
import TrackHours.API.DTO.Task.CreateTaskDTO;
import TrackHours.API.DTO.Task.TaskDTO;
import TrackHours.API.entities.Project;
import TrackHours.API.entities.Release;
import TrackHours.API.entities.Task;
import TrackHours.API.entities.User;
import TrackHours.API.enumTypes.tasks.StatusTask;
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

    // Create release com usuário logado
    public Release createRelease(CreateReleaseDTO createReleaseDTO, Authentication userAuthenticated) {
        Task task = taskRepository.findById(createReleaseDTO.taskId())
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        String email = userAuthenticated.getName();

        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!task.getIntegrantes().contains(user)) {
            throw new RuntimeException("Usuário não faz parte da tarefa. Não é possível criar a lançamento!");
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
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return releaseRepository.findReleasesByUserLogged(email);
    }

    // Find Release by ID
    public Release getReleaseById(Long id) {
        Release release = releaseRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new RuntimeException("Lançamento não encontrado"));

        return release;
    }

    // Update release by ID com usuário logado
    public Release updateReleaseByID(Long id, UpdateReleaseDTO updateReleaseDTO, Authentication userAuthenticated) {
        // Busca a release pelo ID
        var releaseEntity = releaseRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new RuntimeException("Release não encontrada ou deletada com id: " + id));

        // Obtém o usuário autenticado
        String email = userAuthenticated.getName();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Verifica se o usuário faz parte da tarefa associada à release
        if (!releaseEntity.getTask().getIntegrantes().contains(user)) {
            throw new RuntimeException("Usuário não faz parte da tarefa. Não é possível atualizar o lançamento!");
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

        // Atualiza a tarefa associada, se fornecida no DTO
        if (updateReleaseDTO.taskId() != null) {
            var taskEntity = taskRepository.findById(updateReleaseDTO.taskId())
                    .orElseThrow(() -> new RuntimeException("Tarefa relacionada não encontrada: " + updateReleaseDTO.taskId()));

            // Verifica se o usuário faz parte da nova tarefa
            if (!taskEntity.getIntegrantes().contains(user)) {
                throw new RuntimeException("Usuário não faz parte da nova tarefa. Não é possível atualizar o lançamento!");
            }

            releaseEntity.setTask(taskEntity);
        }

        return releaseRepository.save(releaseEntity);
    }

    // Delete By ID com validação do usuário autenticado
    public boolean deleteById(Long id, Authentication authentication) {
        // Busca a release pelo ID
        var release = releaseRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new RuntimeException("Release não encontrada com id: " + id));

        // Obtém o usuário autenticado
        String email = authentication.getName();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Verifica se o usuário faz parte da tarefa associada à release
        if (!release.getTask().getIntegrantes().contains(user)) {
            throw new RuntimeException("Usuário não faz parte da tarefa. Não é possível deletar o lançamento!");
        }

        releaseRepository.deleteById(id);

        return true;
    }
}
