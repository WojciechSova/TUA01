package pl.lodz.p.it.ssbd2021.ssbd02.web.mop;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Klasa ziarna CDI o zasięgu żądania.
 * Zawiera metody obsługujące żądania związane z typami kajut.
 *
 * @author Kacper Świercz
 */
@RequestScoped
@Path("cabinType")
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
public class CabinTypeEndpoint {

    @GET
    @RolesAllowed({"CLIENT", "EMPLOYEE"})
    public Response getAllCabinTypes() {
        return null;
    }
}
