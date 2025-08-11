package api.exceptions;

/**
 * EntityNotFoundException.
 */
public class EntityNotFoundException extends RuntimeException {
    /**
     * EntityNotFoundException.
     *
     * @param message Error message
     */
    public EntityNotFoundException(String message) {
        super(message);
    }
}
