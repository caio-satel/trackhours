package TrackHours.API.controllers;

import TrackHours.API.DTO.Project.CreateProjectDTO;
import TrackHours.API.DTO.Project.ProjectResponseDTO;
import TrackHours.API.DTO.Project.UpdateProjectDTO;
import TrackHours.API.DTO.mapper.ProjectMapper;
import TrackHours.API.Exceptions.UsersExceptions.UserNotFoundException;
import TrackHours.API.entities.Project;
import TrackHours.API.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectMapper map;

    @Autowired
    private ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponseDTO> createNewProject(@RequestBody CreateProjectDTO createProjectDTO) {
        var projectCreated = projectService.createProject(createProjectDTO);
        var response = map.projectDtoToProject(projectCreated);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {
        List<Project> listProjects = projectService.findAll();

        List<ProjectResponseDTO> projectsResponse = listProjects
                .stream()
                .map(allProjects -> map.projectDtoToProject(allProjects))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(projectsResponse);
    }

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

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> updateProjectById(@PathVariable Long id, @RequestBody UpdateProjectDTO updateProjectDTO) {
        Project updatedProject = projectService.updateProjectById(id, updateProjectDTO);

        ProjectResponseDTO projectResponseDTO = map.projectDtoToProject(updatedProject);

        return ResponseEntity.ok(projectResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        boolean projectExists = projectService.deleteById(id);

        if (!projectExists) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
