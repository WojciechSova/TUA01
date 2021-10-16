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
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
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
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBookings() {
        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        List<BookingGeneralDTO> bookingGeneralDTOList = null;
        do {
            try {
                bookingGeneralDTOList = bookingManagerLocal.getAllBookings().stream()
                        .map(BookingMapper::createBookingGeneralDTOFromEntity)
                        .collect(Collectors.toList());
                transactionRollBack = bookingManagerLocal.isTransactionRolledBack();
            } catch (GeneralException generalException) {
                throw generalException;
            } catch (EJBAccessException | AccessLocalException accessExcept) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createForbiddenException();
                }
                transactionRollBack = true;
            } catch (EJBException ejbException) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createUnknownException();
                }
                transactionRollBack = true;
            } catch (Exception e) {
                throw CommonExceptions.createUnknownException();
            }
        } while (transactionRollBack && --transactionRetryCounter > 0);

        return Response.ok()
                .entity(bookingGeneralDTOList)
                .build();
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

        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        BookingDetailsDTO bookingDetailsDTO = null;
        do {
            try {
                bookingDetailsDTO = BookingMapper
                        .createBookingDetailsDTOFromEntity(bookingManagerLocal.getBookingByNumber(number));
                transactionRollBack = bookingManagerLocal.isTransactionRolledBack();
            } catch (GeneralException generalException) {
                throw generalException;
            } catch (EJBAccessException | AccessLocalException accessExcept) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createForbiddenException();
                }
                transactionRollBack = true;
            } catch (EJBException ejbException) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createUnknownException();
                }
                transactionRollBack = true;
            } catch (Exception e) {
                throw CommonExceptions.createUnknownException();
            }
        } while (transactionRollBack && --transactionRetryCounter > 0);

        return Response.ok()
                .entity(bookingDetailsDTO)
                .tag(DTOIdentitySignerVerifier.calculateDTOSignature(bookingDetailsDTO))
                .build();
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

        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        BookingDetailsDTO bookingDetailsDTO = null;
        do {
            try {
                bookingDetailsDTO = BookingMapper
                        .createBookingDetailsDTOFromEntity(bookingManagerLocal
                                .getBookingByAccountAndNumber(securityContext.getUserPrincipal().getName(), number));
                transactionRollBack = bookingManagerLocal.isTransactionRolledBack();
            } catch (GeneralException generalException) {
                throw generalException;
            } catch (EJBAccessException | AccessLocalException accessExcept) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createForbiddenException();
                }
                transactionRollBack = true;
            } catch (EJBException ejbException) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createUnknownException();
                }
                transactionRollBack = true;
            } catch (Exception e) {
                throw CommonExceptions.createUnknownException();
            }
        } while (transactionRollBack && --transactionRetryCounter > 0);

        return Response.ok()
                .entity(bookingDetailsDTO)
                .tag(DTOIdentitySignerVerifier.calculateDTOSignature(bookingDetailsDTO))
                .build();
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
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOwnBookings(@Context SecurityContext securityContext) {
        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        List<BookingGeneralDTO> bookingGeneralDTOList = null;
        do {
            try {
                bookingGeneralDTOList = bookingManagerLocal.getAllBookingsByAccount(
                        securityContext.getUserPrincipal().getName()).stream()
                        .map(BookingMapper::createBookingGeneralDTOFromEntity)
                        .collect(Collectors.toList());
                transactionRollBack = bookingManagerLocal.isTransactionRolledBack();
            } catch (GeneralException generalException) {
                throw generalException;
            } catch (EJBAccessException | AccessLocalException accessExcept) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createForbiddenException();
                }
                transactionRollBack = true;
            } catch (EJBException ejbException) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createUnknownException();
                }
                transactionRollBack = true;
            } catch (Exception e) {
                throw CommonExceptions.createUnknownException();
            }
        } while (transactionRollBack && --transactionRetryCounter > 0);

        return Response.ok()
                .entity(bookingGeneralDTOList)
                .build();
    }

    /**
     * Metoda umożliwiająca klientowi utworzenie rezerwacji
     *
     * @param cruiseNumber    Numer rejsu, na który tworzona jest rezerwacja
     * @param cabinNumber     Numer rezerwowanej kajuty lub pusty string w przypadku rezerwacji miejsca na pokładzie promu
     * @param vehicleTypeName Nazwa typu pojazdu
     * @param peopleNumber    Liczba osób
     * @param securityContext Interfejs wstrzykiwany w celu pozyskania tożsamości aktualnie uwierzytelnionego użytkownika
     * @return Kod 202 w przypadku poprawnej rezerwacji.
     */
    @POST
    @Path("add/{cruise}/{vehicleType}/{cabin:.*}")
    @RolesAllowed({"CLIENT"})
    public Response addBooking(@PathParam("cruise") String cruiseNumber, @PathParam("cabin") String cabinNumber,
                               @PathParam("vehicleType") String vehicleTypeName, int peopleNumber,
                               @Context SecurityContext securityContext) {
        if (!cabinNumber.equals("")) {
            if (!cabinNumber.matches("[A-Z][0-9]{3}")) {
                throw CommonExceptions.createConstraintViolationException();
            }
        }

        if (peopleNumber <= 0 || !cruiseNumber.matches("[A-Z]{6}[0-9]{6}") ||
                !List.of("None", "Motorcycle", "Car", "Bus").contains(vehicleTypeName)) {
            throw CommonExceptions.createConstraintViolationException();
        }
        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        do {
            try {
                bookingManagerLocal.createBooking(peopleNumber, cruiseNumber, cabinNumber,
                        securityContext.getUserPrincipal().getName(), vehicleTypeName);
                transactionRollBack = bookingManagerLocal.isTransactionRolledBack();
            } catch (GeneralException generalException) {
                if (generalException.getMessage().equals(CommonExceptions.createOptimisticLockException().getMessage())) {
                    transactionRollBack = true;
                    if (transactionRetryCounter < 2) {
                        throw generalException;
                    }
                } else {
                    throw generalException;
                }
            } catch (EJBAccessException | AccessLocalException accessExcept) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createForbiddenException();
                }
                transactionRollBack = true;
            } catch (EJBException ejbException) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createUnknownException();
                }
                transactionRollBack = true;
            } catch (Exception e) {
                throw CommonExceptions.createUnknownException();
            }
        } while (transactionRollBack && --transactionRetryCounter > 0);

        return Response.accepted()
                .build();
    }

    /**
     * Metoda pozwalająca anulować rezerwację zalogowanego użytkownika.
     *
     * @param number          Numer rezerwacji
     * @param securityContext Interfejs wstrzykiwany w celu pozyskania tożsamości aktualnie uwierzytelnionego użytkownika
     * @return Kod 200 w przypadku poprawnego anulowania rezerwacji.
     */
    @DELETE
    @Path("cancel/{number}")
    @RolesAllowed({"CLIENT"})
    public Response cancelBooking(@PathParam("number") String number, @Context SecurityContext securityContext) {
        if (number == null || number.isBlank()) {
            throw CommonExceptions.createConstraintViolationException();
        }
        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        do {
            try {
                bookingManagerLocal.removeBooking(securityContext.getUserPrincipal().getName(), number);
                transactionRollBack = bookingManagerLocal.isTransactionRolledBack();
            } catch (GeneralException generalException) {
                if (generalException.getMessage().equals(CommonExceptions.createOptimisticLockException().getMessage())) {
                    transactionRollBack = true;
                    if (transactionRetryCounter < 2) {
                        throw generalException;
                    }
                } else {
                    throw generalException;
                }
            } catch (EJBAccessException | AccessLocalException accessExcept) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createForbiddenException();
                }
                transactionRollBack = true;
            } catch (EJBException ejbException) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createUnknownException();
                }
                transactionRollBack = true;
            } catch (Exception e) {
                throw CommonExceptions.createUnknownException();
            }
        } while (transactionRollBack && --transactionRetryCounter > 0);

        return Response.ok()
                .build();
    }

    /**
     * Metoda pobierająca z właściwości współczynnik określający ilość powtórzeń transakcji.
     *
     * @return Współczynnik powtórzeń transakcji
     */
    private int getTransactionRepetitionCounter() {
        Properties prop = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("system.properties")) {
            prop.load(input);
            return Integer.parseInt(prop.getProperty("system.transaction.repetition"));
        } catch (IOException | NullPointerException | NumberFormatException e) {
            logger.warn(e);
            return 3;
        }
    }
}
