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

import java.net.URI;
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

    @Operation(summary = "Criar novo projeto (Apenas Administradores)")
    @PostMapping
    public ResponseEntity<ProjectResponseDTO> createNewProject(@RequestBody CreateProjectDTO createProjectDTO) {
        var projectCreated = projectService.createProject(createProjectDTO);
        var response = map.projectDtoToProject(projectCreated);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Obter lista projetos (Apenas Administradores)")
    @GetMapping
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {
        List<Project> listProjects = projectService.findAll();

        List<ProjectResponseDTO> projectsResponse = listProjects
                .stream()
                .map(allProjects -> map.projectDtoToProject(allProjects))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(projectsResponse);
    }

    @Operation(summary = "Obter projeto por ID (Apenas Administradores)")
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> getById(@PathVariable Long id) {
        try {
            var project = projectService.findProjectById(id);
            var response = map.projectDtoToProject(project);

            return ResponseEntity.ok(response);
        } catch (UserNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Atualizar projeto por ID (Apenas Administradores)")
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

    @Operation(summary = "Deletar projeto por ID (Apenas Administradores)")
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
}
