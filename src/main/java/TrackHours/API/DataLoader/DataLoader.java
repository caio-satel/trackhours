package TrackHours.API.DataLoader;

import TrackHours.API.entities.Project;
import TrackHours.API.entities.Task;
import TrackHours.API.entities.User;
import TrackHours.API.enumTypes.projects.PriorityProject;
import TrackHours.API.enumTypes.projects.StatusProject;
import TrackHours.API.enumTypes.roles.UserRole;
import TrackHours.API.repositories.ProjectRepository;
import TrackHours.API.repositories.TaskRepository;
import TrackHours.API.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String encryptedAdminPassword = passwordEncoder.encode("admin");
        String encryptedUserPassword = passwordEncoder.encode("user");
        String encryptedRodrigoPassword = passwordEncoder.encode("rodrigo");

        User admin = userRepository.save(new User("admin", "admin@admin.com", encryptedAdminPassword, UserRole.ADMIN));
        User user = userRepository.save(new User("user", "user@user.com", encryptedUserPassword, UserRole.USER));
        User rodrigo = userRepository.save(new User("rodrigo", "rodrigo@rodrigo.com", encryptedRodrigoPassword, UserRole.ADMIN));

        projectRepository.save(new Project(
                "Trainee Wise",
                LocalDate.now(),
                LocalDate.now().plusMonths(3),
                admin,
                StatusProject.PLANNED,
                PriorityProject.HIGH
        ));

        projectRepository.save(new Project(
                "Portifólio Pessoal",
                LocalDate.now().minusDays(15),
                LocalDate.now().plusDays(45),
                user,
                StatusProject.PLANNED,
                PriorityProject.MEDIUM
        ));

        projectRepository.save(new Project(
                "Java",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                rodrigo,
                StatusProject.PLANNED,
                PriorityProject.LOW
        ));

        System.out.println("Dados inseridos no banco de dados!");
    }
}
