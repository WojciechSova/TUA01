package pl.lodz.p.it.ssbd2021.ssbd02.web.mop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.CruiseDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.CruiseGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.CruiseManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.GeneralException;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.CruiseMapper;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.signing.DTOIdentitySignerVerifier;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.signing.DTOSignatureValidatorFilterBinding;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.AccessLocalException;
import javax.ejb.EJBAccessException;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
 * Zawiera metody obsługujące żądania związane z rejsami.
 *
 * @author Kacper Świercz
 */
@RequestScoped
@Path("cruises")
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
public class CruiseEndpoint {

    private static final Logger logger = LogManager.getLogger();

    @Inject
    private CruiseManagerLocal cruiseManagerLocal;

    /**
     * Metoda udostępniająca szczegółowe informacje dotyczące rejsu o podanym numerze
     *
     * @param number Numer wyszukiwanego rejsu
     * @return Szczegółowe informacje o rejsie
     */
    @GET
    @Path("/{number}")
    @RolesAllowed({"EMPLOYEE", "CLIENT"})
    public Response getCruise(@PathParam("number") String number) {
        if (!number.matches("[A-Z]{6}[0-9]{6}")) {
            throw CommonExceptions.createConstraintViolationException();
        }

        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        CruiseDetailsDTO cruiseDetailsDTO = null;
        do {
            try {
                cruiseDetailsDTO = CruiseMapper
                        .createCruiseDetailsDTOFromEntity(cruiseManagerLocal.getCruiseByNumber(number));
                transactionRollBack = cruiseManagerLocal.isTransactionRolledBack();
            } catch (GeneralException generalException) {
                throw generalException;
            } catch (EJBAccessException | AccessLocalException accessExcept) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createForbiddenException();
                }
            } catch (EJBException ejbException) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createUnknownException();
                }
            } catch (Exception e) {
                throw CommonExceptions.createUnknownException();
            }
        } while (transactionRollBack && --transactionRetryCounter > 0);

        return Response.ok()
                .entity(cruiseDetailsDTO)
                .tag(DTOIdentitySignerVerifier.calculateDTOSignature(cruiseDetailsDTO))
                .build();
    }

    /**
     * Metoda udostępniająca informacje o aktualnych rejsach
     *
     * @return Lista aktualnych rejsów
     */
    @GET
    @Path("current")
    @PermitAll
    public Response getCurrentCruises() {
        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        List<CruiseGeneralDTO> currentCruisesDTOList = null;
        do {
            try {
                currentCruisesDTOList = cruiseManagerLocal.getAllCurrentCruises().stream()
                        .map(CruiseMapper::createCruiseGeneralDTOFromEntity)
                        .collect(Collectors.toList());
                transactionRollBack = cruiseManagerLocal.isTransactionRolledBack();
            } catch (GeneralException generalException) {
                throw generalException;
            } catch (EJBAccessException | AccessLocalException accessExcept) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createForbiddenException();
                }
            } catch (EJBException ejbException) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createUnknownException();
                }
            } catch (Exception e) {
                throw CommonExceptions.createUnknownException();
            }
        } while (transactionRollBack && --transactionRetryCounter > 0);

        return Response.ok()
                .entity(currentCruisesDTOList)
                .build();
    }

    /**
     * Metoda umożliwiająca dodanie nowego rejsu.
     *
     * @param cruiseDetailsDTO Obiekt typu {@link CruiseDetailsDTO} przechowujący szczegóły nowego rejsu
     * @param ferry            Identyfikator biznesowy promu
     * @param route            Identyfikator biznesowy trasy
     * @param securityContext  Interfejs wstrzykiwany w celu pozyskania tożsamości aktualnie uwierzytelnionego użytkownika
     * @return Kod 200 w przypadku poprawnego dodania rejsu
     */
    @POST
    @Path("add/{ferry}/{route}")
    @RolesAllowed({"EMPLOYEE"})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addCruise(@Valid CruiseDetailsDTO cruiseDetailsDTO,
                              @PathParam("ferry") String ferry,
                              @PathParam("route") String route,
                              @Context SecurityContext securityContext) {
        if (ferry.length() > 30 || !route.matches("[A-Z]{6}")) {
            throw CommonExceptions.createConstraintViolationException();
        }

        if (cruiseDetailsDTO.getStartDate() == null || cruiseDetailsDTO.getEndDate() == null
                || cruiseDetailsDTO.getStartDate().after(cruiseDetailsDTO.getEndDate())) {
            throw CommonExceptions.createConstraintViolationException();
        }

        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        do {
            try {
                cruiseManagerLocal.createCruise(CruiseMapper.createCruiseFromCruiseDetailsDTO(cruiseDetailsDTO),
                        ferry, route, securityContext.getUserPrincipal().getName());
                transactionRollBack = cruiseManagerLocal.isTransactionRolledBack();
            } catch (GeneralException generalException) {
                throw generalException;
            } catch (EJBAccessException | AccessLocalException accessExcept) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createForbiddenException();
                }
            } catch (EJBException ejbException) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createUnknownException();
                }
            } catch (Exception e) {
                throw CommonExceptions.createUnknownException();
            }
        } while (transactionRollBack && --transactionRetryCounter > 0);

        return Response.ok()
                .build();
    }

    /**
     * Metoda punktu dostępowego umożliwiająca usunięcie rejsu.
     *
     * @param number          Identyfikator biznesowy usuwanego rejsu
     * @param securityContext Interfejs wstrzykiwany w celu pozyskania tożsamości aktualnie uwierzytelnionego użytkownika
     * @return Kod 200 w przypadku poprawnego usunięcia rejsu
     */
    @DELETE
    @Path("remove/{number}")
    @RolesAllowed({"EMPLOYEE"})
    public Response removeCruise(@PathParam("number") String number, @Context SecurityContext securityContext) {
        if (!number.matches("[A-Z]{6}[0-9]{6}")) {
            throw CommonExceptions.createConstraintViolationException();
        }

        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        do {
            try {
                String userLogin = securityContext.getUserPrincipal().getName();
                cruiseManagerLocal.removeCruise(number, userLogin);
                transactionRollBack = cruiseManagerLocal.isTransactionRolledBack();
            } catch (GeneralException generalException) {
                throw generalException;
            } catch (EJBAccessException | AccessLocalException accessExcept) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createForbiddenException();
                }
            } catch (EJBException ejbException) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createUnknownException();
                }
            } catch (Exception e) {
                throw CommonExceptions.createUnknownException();
            }
        } while (transactionRollBack && --transactionRetryCounter > 0);

        return Response.ok()
                .build();
    }

    /**
     * Metoda umożliwiajaca edycję rejsu.
     *
     * @param cruiseDetailsDTO Obiekt typu {@link CruiseDetailsDTO} przechowujący szczegóły edytowanego rejsu
     * @param securityContext  Interfejs wstrzykiwany w celu pozyskania tożsamości aktualnie uwierzytelnionego użytkownika
     * @param eTag             ETag podawany w zawartości nagłówka "If-Match"
     * @return Kod 200 w przypadku poprawnego usunięcia rejsu
     */
    @PUT
    @Path("update")
    @RolesAllowed({"EMPLOYEE"})
    @DTOSignatureValidatorFilterBinding
    public Response updateCruise(@Valid CruiseDetailsDTO cruiseDetailsDTO, @Context SecurityContext securityContext,
                                 @HeaderParam("If-Match") @NotNull @NotEmpty String eTag) {
        if (cruiseDetailsDTO.getStartDate() == null || cruiseDetailsDTO.getEndDate() == null
                || cruiseDetailsDTO.getStartDate().after(cruiseDetailsDTO.getEndDate())
                || cruiseDetailsDTO.getVersion() == null) {
            throw CommonExceptions.createConstraintViolationException();
        }

        if (!DTOIdentitySignerVerifier.verifyDTOIntegrity(eTag, cruiseDetailsDTO)) {
            throw CommonExceptions.createPreconditionFailedException();
        }

        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        do {
            try {
                cruiseManagerLocal.updateCruise(CruiseMapper.createCruiseFromCruiseDetailsDTO(cruiseDetailsDTO),
                        securityContext.getUserPrincipal().getName());
                transactionRollBack = cruiseManagerLocal.isTransactionRolledBack();
            } catch (GeneralException generalException) {
                throw generalException;
            } catch (EJBAccessException | AccessLocalException accessExcept) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createForbiddenException();
                }
            } catch (EJBException ejbException) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createUnknownException();
                }
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
