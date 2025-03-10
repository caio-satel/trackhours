package TrackHours.API.Exceptions.UsersExceptions;

public class UserDatasInvalid extends RuntimeException {
    public UserDatasInvalid (String message) {
        super(message);
    }
}
