package TrackHours.API.Exceptions.Release;

public class ReleaseNotFoundException extends RuntimeException {
    public ReleaseNotFoundException (String message) {
        super(message);
    }
}
