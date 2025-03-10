package TrackHours.API.Exceptions.UsersExceptions;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException (String message) {
        super(message);
    }

}
