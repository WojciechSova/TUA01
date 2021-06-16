package pl.lodz.p.it.ssbd2021.ssbd02.web.mop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.BookingDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.BookingGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.BookingManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.GeneralException;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.BookingMapper;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.signing.DTOIdentitySignerVerifier;

import javax.annotation.security.RolesAllowed;
import javax.ejb.AccessLocalException;
import javax.ejb.EJBAccessException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
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

    private static final Logger logger = LogManager.getLogger();

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

    /**
     * Metoda udostępniająca informacje o rezerwacji po podaniu numeru rezerwacji.
     *
     * @param number Numer rezerwacji
     * @return Informacje o rezerwacji
     */
    @GET
    @Path("{number}")
    @RolesAllowed({"EMPLOYEE"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooking(@PathParam("number") String number) {
        if (number == null || !number.matches("[0-9]{10}")) {
            throw CommonExceptions.createConstraintViolationException();
        }

        try {
            BookingDetailsDTO bookingDetailsDTO = BookingMapper
                    .createBookingDetailsDTOFromEntity(bookingManagerLocal.getBookingByNumber(number));

            return Response.ok()
                    .entity(bookingDetailsDTO)
                    .tag(DTOIdentitySignerVerifier.calculateDTOSignature(bookingDetailsDTO))
                    .build();
        } catch (GeneralException generalException) {
            throw generalException;
        } catch (EJBAccessException | AccessLocalException accessExcept) {
            throw CommonExceptions.createForbiddenException();
        } catch (Exception e) {
            throw CommonExceptions.createUnknownException();
        }
    }

    /**
     * Metoda udostępniająca informacje o własnej rezerwacji po podaniu numeru rezerwacji.
     *
     * @param securityContext Interfejs wstrzykiwany w celu pozyskania tożsamości aktualnie uwierzytelnionego użytkownika
     * @param number          Numer rezerwacji
     * @return Informacje o rezerwacji
     */
    @GET
    @Path("own/{number}")
    @RolesAllowed({"CLIENT"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOwnBooking(@Context SecurityContext securityContext, @PathParam("number") String number) {
        if (number == null || !number.matches("[0-9]{10}")) {
            throw CommonExceptions.createConstraintViolationException();
        }

        try {
            BookingDetailsDTO bookingDetailsDTO = BookingMapper
                    .createBookingDetailsDTOFromEntity(bookingManagerLocal
                            .getBookingByAccountAndNumber(securityContext.getUserPrincipal().getName(), number));

            return Response.ok()
                    .entity(bookingDetailsDTO)
                    .tag(DTOIdentitySignerVerifier.calculateDTOSignature(bookingDetailsDTO))
                    .build();
        } catch (GeneralException generalException) {
            throw generalException;
        } catch (EJBAccessException | AccessLocalException accessExcept) {
            throw CommonExceptions.createForbiddenException();
        } catch (Exception e) {
            throw CommonExceptions.createUnknownException();
        }
    }

    /**
     * Metoda udostępniająca ogólne informacje o rezerwacjach zalogowanego użytkownika.
     *
     * @param securityContext Interfejs wstrzykiwany w celu pozyskania tożsamości aktualnie uwierzytelnionego użytkownika.
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
    public Response addBooking(BookingDetailsDTO bookingDetailsDTO, @Context SecurityContext securityContext) {
        return null;
    }

    @DELETE
    @Path("cancel/{number}")
    @RolesAllowed({"CLIENT"})
    public Response cancelBooking(@PathParam("number") String number, @Context SecurityContext securityContext) {
        if (number == null || number.isBlank()) {
            throw CommonExceptions.createConstraintViolationException();
        }
        try {
            bookingManagerLocal.removeBooking(securityContext.getUserPrincipal().getName(), number);
            return Response.ok().build();
        } catch (GeneralException generalException) {
            throw generalException;
        } catch (EJBAccessException | AccessLocalException accessExcept) {
            throw CommonExceptions.createForbiddenException();
        } catch (Exception e) {
            throw CommonExceptions.createUnknownException();
        }
    }

}
