package TrackHours.API.controllers;

import TrackHours.API.DTO.Project.ProjectResponseDTO;
import TrackHours.API.DTO.Task.CreateTaskDTO;
import TrackHours.API.DTO.Task.TaskDTO;
import TrackHours.API.DTO.Task.UpdateTaskDTO;
import TrackHours.API.DTO.User.UpdateRoleUserDTO;
import TrackHours.API.DTO.mapper.TaskMapper;
import TrackHours.API.entities.Project;
import TrackHours.API.entities.Task;
import TrackHours.API.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskMapper map;

    @PostMapping
    public ResponseEntity<TaskDTO> newTask(@RequestBody CreateTaskDTO createTaskDTO) {
        Task task = taskService.createTask(createTaskDTO);
        TaskDTO taskDTO = map.taskToTaskDTO(task);
        return ResponseEntity.created(URI.create("/tasks/" + task.getId())).body(taskDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long id) {
        TaskDTO taskDTO = taskService.getTaskDTOById(id);
        return ResponseEntity.ok(taskDTO);
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTask () {
        List<Task> listTasks = taskService.getAllTasks();

        List<TaskDTO> taskResponse = listTasks
                .stream()
                .map(allTasks -> map.taskToTaskDTO(allTasks))
                .toList();

        return ResponseEntity.ok().body(taskResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTaskById(@PathVariable Long id, @RequestBody UpdateTaskDTO updateTaskDTO) {
        boolean taskUpdated = taskService.updateTaskById(id, updateTaskDTO);

        if (!taskUpdated) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaskById(@PathVariable Long id) {
        boolean taskExists = taskService.deleteById(id);

        if (!taskExists) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

}
