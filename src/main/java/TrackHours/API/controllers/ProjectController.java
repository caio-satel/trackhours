package TrackHours.API.controllers;

import TrackHours.API.DTO.Project.CreateProjectDTO;
import TrackHours.API.DTO.Project.ProjectResponseDTO;
import TrackHours.API.DTO.Project.UpdateProjectDTO;
import TrackHours.API.DTO.User.UpdateUserDTO;
import TrackHours.API.DTO.mapper.ProjectMapper;
import TrackHours.API.entities.Project;
import TrackHours.API.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Project> createNewProject(@RequestBody CreateProjectDTO createProjectDTO) {
        var projectId = projectService.createProject(createProjectDTO);
        return ResponseEntity.created(URI.create("/projects/" + projectId.toString())).build();
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
    public ResponseEntity<List<ProjectResponseDTO>> getById(@PathVariable Long id) {
        var projectById = projectService.findProjectById(id);

        List<ProjectResponseDTO> projectByIdResponse = projectById
                .stream()
                .map(projectId -> map.projectDtoToProject(projectId))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(projectByIdResponse);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProjectById(@PathVariable Long id, @RequestBody UpdateProjectDTO updateProjectDTO) {
        boolean projectUpdated = projectService.updateProjectById(id, updateProjectDTO);

        if (!projectUpdated) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
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
