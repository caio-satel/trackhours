package TrackHours.API.controllers;

import TrackHours.API.DTO.Project.CreateProjectDTO;
import TrackHours.API.DTO.Project.ProjectResponseDTO;
import TrackHours.API.DTO.Project.UpdateProjectDTO;
import TrackHours.API.DTO.mapper.ProjectMapper;
import TrackHours.API.Exceptions.ProjectExceptions.ProjectNotFoundException;
import TrackHours.API.Exceptions.UsersExceptions.UserNotFoundException;
import TrackHours.API.entities.Project;
import TrackHours.API.services.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/projects")
@Tag(name = "Projetos", description = "Endpoints de projetos")
public class ProjectController {

    @Autowired
    private ProjectMapper map;

    @Autowired
    private ProjectService projectService;

    @Operation(summary = "Criar novo projeto",
            description = "Corpo da requisição esperado: " +
                    "{ \"name\": \"Exemplo\", " +
                    "\"startDate\": \"dd/MM/yyyy\", " +
                    "\"endDate\": \"dd/MM/yyyy\", " +
                    "\"responsibleUser\": 1, " +
                    "\"priority\": \"LOW\" ou \"MEDIUM\" ou \"HIGH\" }")
    @PostMapping
    public ResponseEntity<ProjectResponseDTO> createNewProject(@RequestBody CreateProjectDTO createProjectDTO) {
        try {
            var projectCreated = projectService.createProject(createProjectDTO);
            var response = map.projectDtoToProject(projectCreated);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Obter lista de todos projetos")
    @GetMapping
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {
        List<Project> listProjects = projectService.findAll();

        List<ProjectResponseDTO> projectsResponse = listProjects
                .stream()
                .map(allProjects -> map.projectDtoToProject(allProjects))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(projectsResponse);
    }

    @Operation(summary = "Obter projeto por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> getById(@PathVariable Long id) {
        try {
            var project = projectService.findProjectById(id);
            var response = map.projectDtoToProject(project);

            return ResponseEntity.ok(response);
        } catch (UserNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Atualizar projeto por ID",
                description = "Corpo da requisição esperado: " +
                        "{ \"name\": \"Exemplo\", " +
                        "\"startDate\": \"dd/MM/yyyy\", " +
                        "\"endDate\": \"dd/MM/yyyy\", " +
                        "\"responsibleUser\": 1, " +
                        "\"priority\": \"LOW\" ou \"MEDIUM\" ou \"HIGH\", " +
                        "\"status\": \"PLANNED\" ou \"PROGRESS\" ou \"PAUSED\" ou \"CANCELED\" ou \"DONE\" }")
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProjectById(@PathVariable Long id, @RequestBody UpdateProjectDTO updateProjectDTO) {
        try {
            Project updatedProject = projectService.updateProjectById(id, updateProjectDTO);
            ProjectResponseDTO projectResponseDTO = map.projectDtoToProject(updatedProject);

            return ResponseEntity.ok(projectResponseDTO);
        } catch (ProjectNotFoundException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Deletar projeto por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id, Authentication authentication) {
        try {
            projectService.deleteById(id, authentication);
            return ResponseEntity.noContent().build();
        } catch (ProjectNotFoundException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Obter o total de horas lançadas por projeto nos últimos 30 dias")
    @GetMapping("/total-hours-last-30-days")
    public ResponseEntity<List<Object[]>> getTotalHoursByProjectLast30Days() {
        List<Object[]> listTotalHoursByProject30Days = projectService.getTotalHoursByProjectLast30Days();
        return ResponseEntity.ok().body(listTotalHoursByProject30Days);
    }

    @Operation(summary = "Obter o total de atividades em andamento por projeto")
    @GetMapping("/ongoing-tasks")
    public ResponseEntity<List<Object[]>> getOngoingTasksByProject() {
        List<Object[]> ongoingTasks = projectService.getOngoingTasksByProject();
        return ResponseEntity.ok().body(ongoingTasks);
    }
}
