package pl.lodz.p.it.ssbd2021.ssbd02.web.mop;

import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.CruiseDTO;

import javax.annotation.security.PermitAll;
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
@Path("cruises")
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
public class CruiseEndpoint {

    @GET
    @RolesAllowed({"EMPLOYEE"})
    public Response getAllCruises() {
        return null;
    }

    @GET
    @Path("route/{code}")
    @RolesAllowed({"EMPLOYEE"})
    public Response getCruisesForRoute(@PathParam("code") String code) {
        return null;
    }

    @GET
    @Path("{number}")
    @RolesAllowed({"CLIENT", "EMPLOYEE"})
    public Response getCruise(@PathParam("number") String number) {
        return null;
    }

    @GET
    @Path("current")
    @PermitAll
    public Response getCurrentCruises() {
        return null;
    }

    @POST
    @Path("add")
    @RolesAllowed({"EMPLOYEE"})
    public Response addCruise(CruiseDTO cruiseDTO, @Context SecurityContext securityContext) {
        return null;
    }

    @DELETE
    @Path("remove/{number}")
    @RolesAllowed({"EMPLOYEE"})
    public Response removeCruise(@PathParam("number") String number, @Context SecurityContext securityContext) {
        return null;
    }

    @PUT
    @Path("update")
    @RolesAllowed({"EMPLOYEE"})
    public Response updateCruise(CruiseDTO cruiseDTO, @Context SecurityContext securityContext) {
        return null;
    }

}
