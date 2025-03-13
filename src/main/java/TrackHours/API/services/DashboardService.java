package TrackHours.API.services;

import TrackHours.API.DTO.Dashboard.DashboardProjectDTO;
import TrackHours.API.DTO.Dashboard.LateTasksCountDTO;
import TrackHours.API.DTO.mapper.DashboardMapper;
import TrackHours.API.Exceptions.UsersExceptions.UserNotFoundException;
import TrackHours.API.entities.Project;
import TrackHours.API.entities.Release;
import TrackHours.API.entities.Task;
import TrackHours.API.repositories.DashboardRepository;
import TrackHours.API.repositories.ReleaseRepository;
import TrackHours.API.repositories.TaskRepository;
import TrackHours.API.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private DashboardRepository dashboardRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ReleaseRepository releaseRepository;

    @Autowired
    private DashboardMapper dashboardMapper;

    @Autowired
    private UserRepository userRepository;

    // Find Projects and Tasks with User Logged is Collaborator
    public List<DashboardProjectDTO> getDashboardData(Authentication authentication) {
        String email = authentication.getName();

        userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        // Busca os projetos em que o usuário possui tarefas relacionadas
        List<Project> projects = dashboardRepository.findProjectsAndTasksByUser(email);

        // Para cada projeto, busca as tarefas em que o usuário é um colaborador
        return projects.stream()
                .map(project -> {
                    List<Task> tasks = taskRepository.findTasksByProjectIdAndUserLogged(project.getId(), email);
                    return dashboardMapper.projectToDashboardProjectDTO(project, tasks);
                })
                .collect(Collectors.toList());
    }

    // Get total houras worked by user logged
    public String getTotalHoursWorked(Authentication authentication) {
        String email = authentication.getName();

        userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        List<Release> releasesByUserLogged = releaseRepository.findReleasesByUserLogged(email);

        // Calcula o total de horas trabalhadas
        long totalSeconds = releasesByUserLogged.stream()
                .mapToLong(release -> Duration.between(release.getStartTime(), release.getEndTime()).getSeconds())
                .sum();

        return formatTotalHours(totalSeconds);
    }

    // Format total hours in string
    private String formatTotalHours(long totalSeconds) {
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        return String.format("%02d:%02d", hours, minutes);
    }

    // Find count late tasks by user logged
    public LateTasksCountDTO getLateTasksCount(Authentication authentication) {
        String email = authentication.getName();

        userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        Long lateTasksCount = dashboardRepository.countLateTasksByUser(email);
        return new LateTasksCountDTO(lateTasksCount);
    }
}
