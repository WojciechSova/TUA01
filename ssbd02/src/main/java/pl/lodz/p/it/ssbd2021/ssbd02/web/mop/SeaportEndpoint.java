package pl.lodz.p.it.ssbd2021.ssbd02.web.mop;

import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.SeaportDetailsDTO;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 * Klasa ziarna CDI o zasięgu żądania.
 * Zawiera metody obsługujące żądania związane z portami.
 *
 * @author Kacper Świercz
 */
@RequestScoped
@Path("seaports")
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
public class SeaportEndpoint {

    @GET
    @RolesAllowed({"EMPLOYEE"})
    public Response getAllSeaports() {
        return null;
    }

    @GET
    @Path("{code}")
    @RolesAllowed({"EMPLOYEE", "CLIENT"})
    public Response getSeaport(@PathParam("code") String code) {
        return null;
    }

    @POST
    @Path("add")
    @RolesAllowed({"EMPLOYEE"})
    public Response addSeaport(SeaportDetailsDTO seaportDTO, @Context SecurityContext securityContext) {
        return null;
    }

    @PUT
    @Path("update")
    @RolesAllowed({"EMPLOYEE"})
    public Response updateSeaport(SeaportDetailsDTO seaportDTO, @Context SecurityContext securityContext){
        return null;
    }

    @DELETE
    @Path("remove/{code}")
    @RolesAllowed({"EMPLOYEE"})
    public Response removeSeaport(@PathParam("code") String code, @Context SecurityContext securityContext) {
        return null;
    }
}
