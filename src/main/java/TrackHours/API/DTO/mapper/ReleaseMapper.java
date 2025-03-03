package TrackHours.API.DTO.mapper;

import TrackHours.API.DTO.Release.ReleaseDTO;
import TrackHours.API.DTO.Task.TaskSimpleDTO;
import TrackHours.API.DTO.User.UserDTO;
import TrackHours.API.entities.Release;
import org.springframework.stereotype.Service;

@Service
public class ReleaseMapper {

    public ReleaseDTO releaseToReleaseDTO(Release release) {
        TaskSimpleDTO taskSimpleDTO = new TaskSimpleDTO(
                release.getTask().getId(),
                release.getTask().getName()
        );

        UserDTO userDTO = new UserDTO(
                release.getUser().getId(),
                release.getUser().getName()
        );

        return new ReleaseDTO(
                release.getId(),
                release.getDescription(),
                release.getDateRelease(),
                release.getStartTime(),
                release.getEndTime(),
                release.getCreatedAt(),
                taskSimpleDTO,
                userDTO
        );
    }
}
