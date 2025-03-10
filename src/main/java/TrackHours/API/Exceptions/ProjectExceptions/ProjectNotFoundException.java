package TrackHours.API.Exceptions.ProjectExceptions;

public class ProjectNotFoundException extends RuntimeException {
    public ProjectNotFoundException (String message) {
        super(message);
    }
}
