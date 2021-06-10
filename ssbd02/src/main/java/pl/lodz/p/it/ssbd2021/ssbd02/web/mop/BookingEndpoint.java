package pl.lodz.p.it.ssbd2021.ssbd02.web.mop;

import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.BookingGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.BookingManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.GeneralException;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.BookingMapper;

import javax.annotation.security.RolesAllowed;
import javax.ejb.AccessLocalException;
import javax.ejb.EJBAccessException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.websocket.server.PathParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.stream.Collectors;

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

    @Inject
    private BookingManagerLocal bookingManagerLocal;

    /**
     * Metoda udostępniająca ogólne informacje o rezerwacjach.
     *
     * @return Lista rezerwacji zawierających zestaw ogólnych informacji.
     */
    @GET
    @RolesAllowed({"EMPLOYEE"})
    public Response getAllBookings() {
        try {
            List<BookingGeneralDTO> bookingGeneralDTOList = bookingManagerLocal.getAllBookings().stream()
                    .map(BookingMapper::createBookingGeneralDTOFromEntity)
                    .collect(Collectors.toList());

            return Response.ok()
                    .entity(bookingGeneralDTOList)
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
    @Path("{number}")
    @RolesAllowed({"CLIENT", "EMPLOYEE"})
    public Response getBooking(@PathParam("number") String number) {
        return null;
    }

    /**
     * Metoda udostępniająca ogólne informacje o rezerwacjach zalogowanego użytkownika.
     * @param securityContext Interfejs wstrzykiwany w celu pozyskania tożsamości aktualnie uwierzytelnionego użytkownika.
     *
     * @return Lista rezerwacji zalogowanego użytkownika, zawierających zestaw ogólnych informacji.
     */
    @GET
    @Path("own")
    @RolesAllowed({"CLIENT"})
    public Response getOwnBookings(@Context SecurityContext securityContext) {
        try {
            List<BookingGeneralDTO> bookingGeneralDTOList = bookingManagerLocal.getAllBookingsByAccount(
                    securityContext.getUserPrincipal().getName()).stream()
                    .map(BookingMapper::createBookingGeneralDTOFromEntity)
                    .collect(Collectors.toList());

            return Response.ok()
                    .entity(bookingGeneralDTOList)
                    .build();
        } catch (GeneralException generalException) {
            throw generalException;
        } catch (EJBAccessException | AccessLocalException accessExcept) {
            throw CommonExceptions.createForbiddenException();
        } catch (Exception e) {
            throw CommonExceptions.createUnknownException();
        }
    }

    @POST
    @Path("add")
    @RolesAllowed({"CLIENT"})
    public Response addBooking(BookingGeneralDTO bookingGeneralDTO, @Context SecurityContext securityContext) {
        return null;
    }

    @DELETE
    @Path("cancel")
    @RolesAllowed({"CLIENT"})
    public Response cancelBooking(BookingGeneralDTO bookingGeneralDTO, @Context SecurityContext securityContext) {
        return null;
    }

}
