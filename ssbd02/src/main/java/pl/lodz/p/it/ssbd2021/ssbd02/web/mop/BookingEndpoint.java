package pl.lodz.p.it.ssbd2021.ssbd02.web.mop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.BookingDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.BookingManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.GeneralException;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.BookingMapper;

import javax.annotation.security.RolesAllowed;
import javax.ejb.AccessLocalException;
import javax.ejb.EJBAccessException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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

    private static final Logger logger = LogManager.getLogger();

    @Inject
    private BookingManagerLocal bookingManager;

    @GET
    @RolesAllowed({"EMPLOYEE"})
    public Response getAllBookings() {
        return null;
    }

    /**
     * Metoda udostępniająca informacje o rezerwacji po podaniu numeru rezerwacji.
     *
     * @param number Numer rezerwacji
     * @return Informacje o rezerwacji
     */
    @GET
    @Path("{number}")
    @RolesAllowed({"CLIENT", "EMPLOYEE"})
    public Response getBooking(@PathParam("number") String number) {
        if (number == null || number.length() != 10) {
            throw CommonExceptions.createConstraintViolationException();
        }

        try {
            BookingDetailsDTO bookingDetailsDTO = BookingMapper
                    .createBookingDetailsDTOFromEntity(bookingManager.getBookingByNumber(number));

            return Response.ok()
                    .entity(bookingDetailsDTO)
                    .build();
        } catch (GeneralException generalException) {
            throw generalException;
        } catch (EJBAccessException | AccessLocalException accessExcept) {
            throw CommonExceptions.createForbiddenException();
        } catch (Exception e) {
            throw CommonExceptions.createUnknownException();
        }
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
    public Response addBooking(BookingDetailsDTO bookingDetailsDTO, @Context SecurityContext securityContext) {
        return null;
    }

    @DELETE
    @Path("cancel")
    @RolesAllowed({"CLIENT"})
    public Response cancelBooking(BookingDetailsDTO bookingDetailsDTO, @Context SecurityContext securityContext) {
        return null;
    }

}
