package api.exceptions;

/**
 * EntityNotFoundException.
 */
public class EntityNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * EntityNotFoundException.
     *
     * @param message Error message
     */
    public EntityNotFoundException(String message) {
        super(message);
    }
}
