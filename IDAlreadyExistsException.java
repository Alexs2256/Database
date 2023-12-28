import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class IDAlreadyExistsException extends SQLIntegrityConstraintViolationException {

    public IDAlreadyExistsException(String message) {

        super(message);
    }
}
