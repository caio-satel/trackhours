package TrackHours.API.Exceptions.UsersExceptions;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException (String message) {
        super(message);
    }
}
