package its.fullstack.biblioteca_proj.exception;

public class EmailAlreadyUsedException extends RuntimeException {

    public EmailAlreadyUsedException() {
        super("Email già in uso");
    }

    public EmailAlreadyUsedException(String message) {
        super(message);
    }

    public EmailAlreadyUsedException(String email, boolean isEmail) {
        super("L'email " + email + " è già in uso");
    }
}