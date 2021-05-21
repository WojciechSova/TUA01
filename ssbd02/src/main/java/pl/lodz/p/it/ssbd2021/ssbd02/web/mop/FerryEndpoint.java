package pl.lodz.p.it.ssbd2021.ssbd02.web.mop;

import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.FerryDTO;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 * Klasa ziarna CDI o zasięgu żądania.
 * Zawiera metody obsługujące żądania związane z promami.
 *
 * @author Kacper Świercz
 */
@RequestScoped
@Path("ferries")
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
public class FerryEndpoint {

    @GET
    @RolesAllowed({"EMPLOYEE"})
    public Response getAllFerries() {
        return null;
    }

    @GET
    @Path("{name}")
    @RolesAllowed({"EMPLOYEE"})
    public Response getFerry(@PathParam("name") String name) {
        return null;
    }

    @POST
    @Path("add")
    @RolesAllowed({"EMPLOYEE"})
    public Response addFerry(FerryDTO ferryDTO, @Context SecurityContext securityContext) {
        return null;
    }

    @DELETE
    @Path("remove/{name}")
    @RolesAllowed({"EMPLOYEE"})
    public Response removeFerry(@PathParam("name") String name, @Context SecurityContext securityContext) {
        return null;
    }

    @PUT
    @Path("update")
    @RolesAllowed({"EMPLOYEE"})
    public Response updateFerry(FerryDTO ferryDTO, @Context SecurityContext securityContext){
        return null;
    }

}
