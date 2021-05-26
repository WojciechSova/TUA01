package pl.lodz.p.it.ssbd2021.ssbd02.web.mop;

import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.BookingDTO;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.websocket.server.PathParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 * Klasa ziarna CDI o zasięgu żądania.
 * Zawiera metody obsługujące żądania związane z rezerwacjami.
 *
 * @author Kacper Świercz
 */
@RequestScoped
@Path("bookings")
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
public class BookingEndpoint {

    @GET
    @RolesAllowed({"EMPLOYEE"})
    public Response getAllBookings() {
        return null;
    }

    @GET
    @Path("{number}")
    @RolesAllowed({"CLIENT", "EMPLOYEE"})
    public Response getBooking(@PathParam("number") String number) {
        return null;
    }

    @GET
    @Path("own")
    @RolesAllowed({"CLIENT"})
    public Response getOwnBookings(@Context SecurityContext securityContext) {
        return null;
    }

    @POST
    @Path("add")
    @RolesAllowed({"CLIENT"})
    public Response addBooking(BookingDTO bookingDTO, @Context SecurityContext securityContext) {
        return null;
    }

    @DELETE
    @Path("cancel")
    @RolesAllowed({"CLIENT"})
    public Response cancelBooking(BookingDTO bookingDTO, @Context SecurityContext securityContext) {
        return null;
    }

}
