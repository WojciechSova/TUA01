package pl.lodz.p.it.ssbd2021.ssbd02.exceptions;

import javax.ejb.ApplicationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Klasa abstrakcyjna rozszerzająca {@link WebApplicationException}.
 *
 * @author Wojciech Sowa
 */
@ApplicationException(rollback = true)
public abstract class GeneralException extends WebApplicationException {

    protected GeneralException(Response.Status status, String key) {
        super(Response.status(status).entity(key).build());
    }
}
