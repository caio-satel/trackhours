package TrackHours.API.controllers;

import TrackHours.API.DTO.Release.CreateReleaseDTO;
import TrackHours.API.DTO.Release.ReleaseDTO;
import TrackHours.API.DTO.Release.UpdateReleaseDTO;
import TrackHours.API.DTO.mapper.ReleaseMapper;
import TrackHours.API.Exceptions.Release.ReleaseNotFoundException;
import TrackHours.API.Exceptions.Tasks.TaskNotFoundException;
import TrackHours.API.Exceptions.UsersExceptions.UserNotCollaboratorTaskException;
import TrackHours.API.Exceptions.UsersExceptions.UserNotFoundException;
import TrackHours.API.entities.Release;
import TrackHours.API.services.ReleaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/releases")
@Tag(name = "Lançamento de Horas", description = "Endpoints de lançamento de horas")
public class ReleaseController {

    @Autowired
    private ReleaseService releaseService;

    @Autowired
    private ReleaseMapper map;

    @Operation(summary = "Criar lançamento de horas", description = "Corpo da requisição esperado: " +
            "{ \"description\": \"Exemplo\", " +
            "\"dateRelease\": \"dd/MM/yyyy\", " +
            "\"startTime\": \"HH:mm\", " +
            "\"endTime\": \"HH:mm\", " +
            "\"taskId\": 1 }")
    @PostMapping
    private ResponseEntity<ReleaseDTO> newRelease(@RequestBody CreateReleaseDTO createReleaseDTO, Authentication userAuthenticated) {
        try {
            Release release = releaseService.createRelease(createReleaseDTO, userAuthenticated);
            ReleaseDTO releaseDTO = map.releaseToReleaseDTO(release);
            return ResponseEntity.created(URI.create("/tasks/" + release.getId())).body(releaseDTO);
        } catch (UserNotFoundException | TaskNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (UserNotCollaboratorTaskException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Obter lista de todos lançamentos de horas")
    @GetMapping
    private ResponseEntity<List<ReleaseDTO>> getAllReleases() {
        List<Release> listReleases = releaseService.listAllReleases();

        List<ReleaseDTO> releaseResponse = listReleases
                .stream()
                .map(allReleases -> map.releaseToReleaseDTO(allReleases))
                .toList();

        return ResponseEntity.ok().body(releaseResponse);
    }

    @Operation(summary = "Obter lista de lançamento de horas do usuário logado")
    @GetMapping("/byUser")
    public ResponseEntity<List<ReleaseDTO>> getReleasesByUserLogged(Authentication authentication) {
        try {
            List<Release> releases = releaseService.getReleasesByUserLogged(authentication);

            List<ReleaseDTO> releaseResponse = releases
                    .stream()
                    .map(allReleases -> map.releaseToReleaseDTO(allReleases))
                    .toList();

            return ResponseEntity.ok(releaseResponse);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Obter lançamento de horas por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ReleaseDTO> releaseById(@PathVariable Long id) {
        try {
            Release release = releaseService.getReleaseById(id);
            ReleaseDTO releaseDTO = map.releaseToReleaseDTO(release);

            return ResponseEntity.ok(releaseDTO);
        } catch (ReleaseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Atualizar lançamento de horas por ID", description = "Corpo da requisição esperado: " +
            "{ \"description\": \"Exemplo\", " +
            "\"dateRelease\": \"dd/MM/yyyy\", " +
            "\"startTime\": \"HH:mm\", " +
            "\"endTime\": \"HH:mm\", " +
            "\"taskId\": 1 }")
    @PutMapping("/{id}")
    public ResponseEntity<ReleaseDTO> updateReleaseById(@PathVariable Long id, @RequestBody UpdateReleaseDTO updateReleaseDTO, Authentication userAuthenticated) {
        try {
            Release updateRelease = releaseService.updateReleaseByID(id, updateReleaseDTO, userAuthenticated);
            ReleaseDTO releaseDtoUpdated = map.releaseToReleaseDTO(updateRelease);

            return ResponseEntity.ok().body(releaseDtoUpdated);
        } catch (UserNotFoundException | TaskNotFoundException | ReleaseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (UserNotCollaboratorTaskException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Deletar lançamento de horas por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReleaseById(@PathVariable Long id, Authentication userAuthenticated) {
        try {
            boolean isDeleted = releaseService.deleteById(id, userAuthenticated);

            if (isDeleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (UserNotFoundException | ReleaseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (UserNotCollaboratorTaskException e) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Obter lista agrupada por usuários com quantidade de lançamentos e total de horas lançadas")
    @GetMapping("/user-launches")
    public ResponseEntity<List<Object[]>> getLaunchesAndTotalHoursByUser() {
        List<Object[]> launchesByUser = releaseService.getLaunchesAndTotalHoursByUser();

        return ResponseEntity.ok().body(launchesByUser);
    }
}