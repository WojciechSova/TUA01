package pl.lodz.p.it.ssbd2021.ssbd02.web.mop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.FerryDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.FerryGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.FerryManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.GeneralException;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.FerryMapper;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.signing.DTOIdentitySignerVerifier;

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
 * Zawiera metody obsługujące żądania związane z promami.
 *
 * @author Kacper Świercz
 */
@RequestScoped
@Path("ferries")
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
public class FerryEndpoint {

    private static final Logger logger = LogManager.getLogger();

    @Inject
    private FerryManagerLocal ferryManagerLocal;

    /**
     * Metoda udostępniająca ogólne informacje o promach.
     *
     * @return Lista promów zawierających zestaw ogólnych o nich informacji.
     */
    @GET
    @RolesAllowed({"EMPLOYEE"})
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllFerries() {
        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        List<FerryGeneralDTO> ferryGeneralDTOList = null;
        do {
            try {
                ferryGeneralDTOList = ferryManagerLocal.getAllFerries().stream()
                        .map(FerryMapper::createFerryGeneralDTOFromEntity)
                        .collect(Collectors.toList());
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
                .entity(ferryGeneralDTOList)
                .build();
    }

    /**
     * Metoda udostępniająca szczegółowe informacje dotyczące promu o podanej nazwie
     *
     * @param name Nazwa wyszukiwanego promu
     * @return Szczegółowe informacje o promie
     */
    @GET
    @Path("{name}")
    @RolesAllowed({"EMPLOYEE"})
    public Response getFerry(@PathParam("name") String name) {
        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        FerryDetailsDTO ferryDetailsDTO = null;
        do {
            try {
                ferryDetailsDTO = FerryMapper
                        .createFerryDetailsDTOFromEntities(ferryManagerLocal.getFerryAndCabinsByFerryName(name));
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
                .entity(ferryDetailsDTO)
                .tag(DTOIdentitySignerVerifier.calculateDTOSignature(ferryDetailsDTO))
                .build();
    }

    /**
     * Metoda dodająca nowy prom.
     *
     * @param ferryDetailsDTO Tworzony prom
     * @param securityContext Interfejs wstrzykiwany w celu pozyskania tożsamości aktualnie uwierzytelnionego użytkownika
     * @return Kod 200 w przypadku poprawnego utworzenia promu
     */
    @POST
    @Path("add")
    @RolesAllowed({"EMPLOYEE"})
    public Response addFerry(@Valid FerryDetailsDTO ferryDetailsDTO, @Context SecurityContext securityContext) {
        if (ferryDetailsDTO.getName() == null || ferryDetailsDTO.getVehicleCapacity() == null
                || ferryDetailsDTO.getOnDeckCapacity() == null) {
            throw CommonExceptions.createConstraintViolationException();
        }
        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        do {
            try {
                ferryManagerLocal.createFerry(securityContext.getUserPrincipal().getName(),
                        FerryMapper.createFerryFromFerryDetailsDTO(ferryDetailsDTO));
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
     * Metoda edytująca prom.
     *
     * @param ferryDetailsDTO Obiekt typu {@link FerryDetailsDTO} zawierający zaktualizowane pola promu.
     * @param securityContext Interfejs wstrzykiwany w celu pozyskania tożsamości aktualnie uwierzytelnionego użytkownika
     * @param eTag            ETag podawany w zawartości nagłówka "If-Match"
     * @return Kod 200 w przypadku poprawnej aktualizacji.
     */
    @PUT
    @Path("update")
    @RolesAllowed({"EMPLOYEE"})
    public Response updateFerry(@Valid FerryDetailsDTO ferryDetailsDTO, @Context SecurityContext securityContext,
                                @HeaderParam("If-Match") @NotNull @NotEmpty String eTag) {
        if (ferryDetailsDTO.getName() == null || ferryDetailsDTO.getName().isBlank()
                || ferryDetailsDTO.getVersion() == null) {
            throw CommonExceptions.createConstraintViolationException();
        }
        if (!DTOIdentitySignerVerifier.verifyDTOIntegrity(eTag, ferryDetailsDTO)) {
            throw CommonExceptions.createPreconditionFailedException();
        }
        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        do {
            try {
                ferryManagerLocal.updateFerry(FerryMapper.createFerryFromFerryDetailsDTO(ferryDetailsDTO),
                        securityContext.getUserPrincipal().getName());
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
     * Metoda usuwająca prom.
     *
     * @param name            Nazwa promu, który ma zostać usunięty
     * @param securityContext Interfejs wstrzykiwany w celu pozyskania tożsamości aktualnie uwierzytelnionego użytkownika
     * @return Kod 200 w przypadku poprawnego usunięcia promu.
     */
    @DELETE
    @Path("remove/{name}")
    @RolesAllowed({"EMPLOYEE"})
    public Response removeFerry(@PathParam("name") String name, @Context SecurityContext securityContext) {
        if (name == null || name.isBlank() || name.length() > 30) {
            throw CommonExceptions.createConstraintViolationException();
        }

        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        do {
            try {
                ferryManagerLocal.removeFerry(name, securityContext.getUserPrincipal().getName());
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
