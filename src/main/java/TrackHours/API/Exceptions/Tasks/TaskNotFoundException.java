package TrackHours.API.Exceptions.Tasks;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException (String message) {
        super(message);
    }
}
