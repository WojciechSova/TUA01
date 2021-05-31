package pl.lodz.p.it.ssbd2021.ssbd02.web.mop;

import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.CabinDTO;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 * Klasa ziarna CDI o zasięgu żądania.
 * Zawiera metody obsługujące żądania związane z rejsami.
 *
 * @author Kacper Świercz
 */
@RequestScoped
@Path("cabins")
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
public class CabinEndpoint {

    @POST
    @Path("add")
    @RolesAllowed({"EMPLOYEE"})
    public Response addCabin(CabinDTO cabinDTO, @Context SecurityContext securityContext) {
        return null;
    }

    @GET
    @Path("{number}")
    @RolesAllowed({"CLIENT", "EMPLOYEE"})
    public Response getCabin(@PathParam("number") String number) {
        return null;
    }

    @GET
    @Path("ferry/{name}")
    @RolesAllowed({"CLIENT", "EMPLOYEE"})
    public Response getCabinsByFerry(@PathParam("name") String name) {
        return null;
    }

    @DELETE
    @Path("remove/{number}")
    @RolesAllowed({"EMPLOYEE"})
    public Response removeCabin(@PathParam("number") String number, @Context SecurityContext securityContext) {
        return null;
    }

    @PUT
    @Path("update")
    @RolesAllowed({"EMPLOYEE"})
    public Response updateCabin(CabinDTO cabinDTO, @Context SecurityContext securityContext){
        return null;
    }
}
