package TrackHours.API.controllers;

import TrackHours.API.DTO.Release.CreateReleaseDTO;
import TrackHours.API.DTO.Release.ReleaseDTO;
import TrackHours.API.DTO.Release.UpdateReleaseDTO;
import TrackHours.API.DTO.Task.TaskDTO;
import TrackHours.API.DTO.Task.UpdateTaskDTO;
import TrackHours.API.DTO.mapper.ReleaseMapper;
import TrackHours.API.entities.Release;
import TrackHours.API.services.ReleaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Operation(summary = "Criar lançamento de horas")
    @PostMapping
    private ResponseEntity<ReleaseDTO> newRelease(@RequestBody CreateReleaseDTO createReleaseDTO, Authentication userAuthenticated) {
        Release release = releaseService.createRelease(createReleaseDTO, userAuthenticated);
        ReleaseDTO releaseDTO = map.releaseToReleaseDTO(release);
        return ResponseEntity.created(URI.create("/tasks/" + release.getId())).body(releaseDTO);
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
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Obter lançamento de horas por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ReleaseDTO> releaseById(@PathVariable Long id) {
        Release release = releaseService.getReleaseById(id);
        ReleaseDTO releaseDTO = map.releaseToReleaseDTO(release);
        return ResponseEntity.ok(releaseDTO);
    }

    @Operation(summary = "Atualizar lançamento de horas por ID")
    @PutMapping("/{id}")
    public ResponseEntity<ReleaseDTO> updateReleaseById(@PathVariable Long id, @RequestBody UpdateReleaseDTO updateReleaseDTO, Authentication userAuthenticated) {
        Release updateRelease = releaseService.updateReleaseByID(id, updateReleaseDTO, userAuthenticated);

        ReleaseDTO releaseDtoUpdated = map.releaseToReleaseDTO(updateRelease);

        return ResponseEntity.ok().body(releaseDtoUpdated);
    }

    @Operation(summary = "Deletar lançamento de horas por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReleaseById(@PathVariable Long id, Authentication userAuthenticated) {
        boolean isDeleted = releaseService.deleteById(id, userAuthenticated);

        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
