package TrackHours.API.Exceptions.UsersExceptions;

public class UserNotCollaboratorTaskException extends RuntimeException {
    public UserNotCollaboratorTaskException (String message) {
        super(message);
    }
}
