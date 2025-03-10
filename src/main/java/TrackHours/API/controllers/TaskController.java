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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
@Tag(name = "Tarefas", description = "Endpoints de tarefas")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskMapper map;

    @Operation(summary = "Criar nova tarefa (Apenas administradores)")
    @PostMapping
    public ResponseEntity<TaskDTO> newTask(@RequestBody CreateTaskDTO createTaskDTO) {
        Task task = taskService.createTask(createTaskDTO);
        TaskDTO taskDTO = map.taskToTaskDTO(task);
        return ResponseEntity.created(URI.create("/tasks/" + task.getId())).body(taskDTO);
    }

    @Operation(summary = "Obter tarefa por ID (Apenas administradores)")
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long id) {
        TaskDTO taskDTO = taskService.getTaskDTOById(id);
        return ResponseEntity.ok(taskDTO);
    }

    @Operation(summary = "Obter lista de tarefas (Apenas administradores)")
    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTask () {
        List<Task> listTasks = taskService.getAllTasks();

        List<TaskDTO> taskResponse = listTasks
                .stream()
                .map(allTasks -> map.taskToTaskDTO(allTasks))
                .toList();

        return ResponseEntity.ok().body(taskResponse);
    }

    @Operation(summary = "Obter lista de tarefas do usuário logado")
    @GetMapping("/byUser")
    public ResponseEntity<List<TaskDTO>> getTasksForCurrentUser(Authentication authentication) {
        List<Task> tasks = taskService.getTasksForCurrentUser(authentication);

        List<TaskDTO> taskDTOs = tasks.stream()
                .map(map::taskToTaskDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(taskDTOs);
    }

    @Operation(summary = "Atualizar tarefa por ID (Apenas administradores)")
    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTaskById(@PathVariable Long id, @RequestBody UpdateTaskDTO updateTaskDTO) {
        Task updatedTask = taskService.updateTaskById(id, updateTaskDTO);

        if (updatedTask == null) {
            return ResponseEntity.notFound().build();
        }

        TaskDTO taskDTO = map.taskToTaskDTO(updatedTask);
        return ResponseEntity.ok(taskDTO);
    }

    @Operation(summary = "Deletar tarefa por ID (Apenas administradores)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaskById(@PathVariable Long id) {
        boolean taskExists = taskService.deleteById(id);

        if (!taskExists) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

}
