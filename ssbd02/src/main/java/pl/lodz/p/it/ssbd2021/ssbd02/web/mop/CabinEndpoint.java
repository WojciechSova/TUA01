package pl.lodz.p.it.ssbd2021.ssbd02.web.mop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.CabinDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.CabinGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.CabinManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.CabinTypeManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.CabinType;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.GeneralException;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.CabinMapper;
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
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
 * Zawiera metody obsługujące żądania związane z kajutami.
 *
 * @author Kacper Świercz
 */
@RequestScoped
@Path("cabins")
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
public class CabinEndpoint {

    private static final Logger logger = LogManager.getLogger();

    @Inject
    private CabinManagerLocal cabinManager;

    @Inject
    private CabinTypeManagerLocal cabinTypeManager;

    /**
     * Metoda dodająca nową kajutę.
     *
     * @param cabinDTO        Tworzona kajuta
     * @param securityContext Interfejs wstrzykiwany w celu pozyskania tożsamości aktualnie uwierzytelnionego użytkownika
     * @param ferryName       Nazwa promu, do którego zostanie przypisana kajuta
     * @return Kod 202 w przypadku poprawnego dodania
     */
    @POST
    @Path("{ferryName}/add")
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed({"EMPLOYEE"})
    public Response addCabin(@Valid CabinDetailsDTO cabinDTO, @Context SecurityContext securityContext, @PathParam("ferryName") String ferryName) {
        if (cabinDTO.getCapacity() == null || cabinDTO.getCabinType() == null || cabinDTO.getNumber() == null) {
            throw CommonExceptions.createConstraintViolationException();
        }

        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        do {
            try {
                cabinManager.createCabin(
                        CabinMapper.createEntityFromCabinDetailsDTO(cabinDTO, cabinTypeManager.getCabinTypeByName(cabinDTO.getCabinType())),
                        securityContext.getUserPrincipal().getName(),
                        ferryName);
                transactionRollBack = cabinManager.isTransactionRolledBack();
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

        return Response.accepted()
                .build();
    }

    /**
     * Metoda udostępniająca szczegółowe informacje dotyczące kajuty o podanym numerze i znajdującej się na podanym promie.
     *
     * @param ferryName   Nazwa promu, na którym znajduje się kajuta
     * @param cabinNumber Numer wyszukiwanej kajuty
     * @return Szczegółowe informacje o kajucie
     */
    @GET
    @Path("details/{ferry}/{number}")
    @RolesAllowed({"EMPLOYEE"})
    public Response getCabin(@PathParam("ferry") String ferryName, @PathParam("number") String cabinNumber) {
        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        CabinDetailsDTO cabinDetailsDTO = null;
        do {
            try {
                cabinDetailsDTO = CabinMapper
                        .createCabinDetailsDTOFromEntity(cabinManager.getCabinByFerryAndNumber(ferryName, cabinNumber));
                transactionRollBack = cabinManager.isTransactionRolledBack();
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
                .entity(cabinDetailsDTO)
                .tag(DTOIdentitySignerVerifier.calculateDTOSignature(cabinDetailsDTO))
                .build();
    }

    /**
     * Metoda udostępniająca listę ogólnych informacji o wolnych kajutach dla danego rejsu
     *
     * @param cruiseNumber Rejs, dla którego wyszukiwane są kajuty
     * @return Lista ogólnych informacji o kajutach
     */
    @GET
    @Path("cruise/free/{number}")
    @RolesAllowed({"CLIENT"})
    public Response getFreeCabinsOnCruise(@PathParam("number") String cruiseNumber) {
        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        List<CabinGeneralDTO> cabinGeneralDTOList = null;
        do {
            try {
                cabinGeneralDTOList = cabinManager.getFreeCabinsOnCruise(cruiseNumber).stream()
                        .map(CabinMapper::createCabinGeneralDTOFromEntity).collect(Collectors.toList());
                transactionRollBack = cabinManager.isTransactionRolledBack();
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
                .entity(cabinGeneralDTOList)
                .build();
    }

    /**
     * Metoda usuwająca kajutę.
     *
     * @param number          Numer identyfikujący kajutę, która będzie usuwana
     * @param securityContext Interfejs wstrzykiwany w celu pozyskania tożsamości aktualnie uwierzytelnionego użytkownika
     * @return Kod 200 w przypadku udanego usunięcia kajuty
     */
    @DELETE
    @Path("remove/{number}")
    @RolesAllowed({"EMPLOYEE"})
    public Response removeCabin(@PathParam("number") String number, @Context SecurityContext securityContext) {
        if (number == null || number.isBlank() || !number.matches("[A-Z][0-9]{3}")) {
            throw CommonExceptions.createConstraintViolationException();
        }

        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        do {
            try {
                cabinManager.removeCabin(number, securityContext.getUserPrincipal().getName());
                transactionRollBack = cabinManager.isTransactionRolledBack();
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
     * Metoda edytująca kajutę.
     *
     * @param cabinDTO        Kajuta, która ma być edytowana
     * @param ferryName       Nazwa promu
     * @param securityContext Interfejs wstrzykiwany w celu pozyskania tożsamości aktualnie uwierzytelnionego użytkownika
     * @param eTag            ETag podawany w zawartości nagłówka "If-Match"
     * @return Kod 200 w przypadku poprawnej modyfikacji kajuty
     */
    @PUT
    @Path("update/{ferry}")
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed({"EMPLOYEE"})
    public Response updateCabin(@Valid CabinDetailsDTO cabinDTO, @PathParam("ferry") String ferryName,
                                @Context SecurityContext securityContext, @HeaderParam("If-Match") @NotNull @NotEmpty String eTag) {

        if (cabinDTO.getNumber() == null || cabinDTO.getVersion() == null) {
            throw CommonExceptions.createPreconditionFailedException();
        }
        if (!DTOIdentitySignerVerifier.verifyDTOIntegrity(eTag, cabinDTO)) {
            throw CommonExceptions.createPreconditionFailedException();
        }
        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        do {
            try {
                CabinType cabinType = cabinTypeManager.getCabinTypeByName(cabinDTO.getCabinType());
                cabinManager.updateCabin(CabinMapper.createEntityFromCabinDetailsDTO(cabinDTO, cabinType),
                        securityContext.getUserPrincipal().getName(), ferryName);
                transactionRollBack = cabinManager.isTransactionRolledBack();
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
