package pl.lodz.p.it.ssbd2021.ssbd02.web.mop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.SeaportDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.SeaportGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.SeaportManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.GeneralException;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.SeaportMapper;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.signing.DTOIdentitySignerVerifier;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.signing.DTOSignatureValidatorFilterBinding;

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
 * Zawiera metody obsługujące żądania związane z portami.
 *
 * @author Kacper Świercz
 */
@RequestScoped
@Path("seaports")
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
public class SeaportEndpoint {

    private static final Logger logger = LogManager.getLogger();

    @Inject
    private SeaportManagerLocal seaportManager;

    /**
     * Metoda udostępniająca ogólne informacje o portach.
     *
     * @return Lista portów zawierających zestaw ogólnych informacji.
     */
    @GET
    @RolesAllowed({"EMPLOYEE"})
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllSeaports() {
        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        List<SeaportGeneralDTO> seaportGeneralDTOList = null;
        do {
            try {
                seaportGeneralDTOList = seaportManager.getAllSeaports().stream()
                        .map(SeaportMapper::createSeaportGeneralDTOFromEntities)
                        .collect(Collectors.toList());
                transactionRollBack = seaportManager.isTransactionRolledBack();
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
                .entity(seaportGeneralDTOList)
                .build();
    }

    /**
     * Metoda udostępniająca informacje o porcie po podaniu kodu portu.
     *
     * @param code Kod portu
     * @return Informacje o porcie
     */
    @GET
    @Path("{code}")
    @RolesAllowed({"EMPLOYEE"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSeaport(@PathParam("code") String code) {
        if (code == null || !code.matches("[A-Z]{3}")) {
            throw CommonExceptions.createConstraintViolationException();
        }

        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        SeaportDetailsDTO seaportDetailsDTO = null;
        do {
            try {
                seaportDetailsDTO = SeaportMapper
                        .createSeaportDetailsDTOFromEntity(seaportManager.getSeaportByCode(code));
                transactionRollBack = seaportManager.isTransactionRolledBack();
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
                .entity(seaportDetailsDTO)
                .tag(DTOIdentitySignerVerifier.calculateDTOSignature(seaportDetailsDTO))
                .build();
    }

    /**
     * Metoda umożliwiająca dodanie nowego portu.
     *
     * @param seaportDetailsDTO Obiekt typu {@link SeaportDetailsDTO} przechowujący szczegóły nowego portu
     * @param securityContext   Interfejs wstrzykiwany w celu pozyskania tożsamości aktualnie uwierzytelnionego użytkownika
     * @return Kod 200 w przypadku poprawnego dodania portu
     */
    @POST
    @Path("add")
    @RolesAllowed({"EMPLOYEE"})
    public Response addSeaport(@Valid SeaportDetailsDTO seaportDetailsDTO, @Context SecurityContext securityContext) {
        if (seaportDetailsDTO.getCity() == null || seaportDetailsDTO.getCode() == null) {
            throw CommonExceptions.createConstraintViolationException();
        }
        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        do {
            try {
                seaportManager.createSeaport(securityContext.getUserPrincipal().getName(), SeaportMapper.createSeaportFromSeaportDetailsDTO(seaportDetailsDTO));
                transactionRollBack = seaportManager.isTransactionRolledBack();
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
                .build();
    }

    /**
     * Metoda umożliwiająca edycję portu.
     *
     * @param seaportDetailsDTO Obiekt typu {@link SeaportDetailsDTO} przechowujący sczegóły edytowanego portu
     * @param securityContext   Interfejs wstrzykiwany w celu pozyskania tożsamości aktualnie uwierzytelnionego użytkownika
     * @param eTag              ETag podawany w zawartości nagłówka "If-Match"
     * @return Kod 200 w przypadku poprawnej edycji portu
     */
    @PUT
    @Path("update")
    @RolesAllowed({"EMPLOYEE"})
    @DTOSignatureValidatorFilterBinding
    public Response updateSeaport(@Valid SeaportDetailsDTO seaportDetailsDTO, @Context SecurityContext securityContext,
                                  @HeaderParam("If-Match") @NotNull @NotEmpty String eTag) {
        if (seaportDetailsDTO.getCity() == null || seaportDetailsDTO.getCity().isBlank()
                || seaportDetailsDTO.getVersion() == null) {
            throw CommonExceptions.createConstraintViolationException();
        }
        if (!DTOIdentitySignerVerifier.verifyDTOIntegrity(eTag, seaportDetailsDTO)) {
            throw CommonExceptions.createPreconditionFailedException();
        }
        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        do {
            try {
                seaportManager.updateSeaport(SeaportMapper.createSeaportFromSeaportDetailsDTO(seaportDetailsDTO),
                        securityContext.getUserPrincipal().getName());
                transactionRollBack = seaportManager.isTransactionRolledBack();
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
                .build();
    }

    /**
     * Metoda umożliwiająca usunięcie portu.
     *
     * @param code            kod identyfikujący port, który chcemy usunąć
     * @param securityContext Interfejs wstrzykiwany w celu pozyskania tożsamości aktualnie uwierzytelnionego użytkownika
     * @return Kod 200 w przypadku udanego usunięcia portu
     */
    @DELETE
    @Path("remove/{code}")
    @RolesAllowed({"EMPLOYEE"})
    public Response removeSeaport(@PathParam("code") String code, @Context SecurityContext securityContext) {
        if (!code.matches("[A-Z]{3}")) {
            throw CommonExceptions.createConstraintViolationException();
        }

        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        do {
            try {
                String userLogin = securityContext.getUserPrincipal().getName();
                seaportManager.removeSeaport(code, userLogin);
                transactionRollBack = seaportManager.isTransactionRolledBack();
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
                .build();
    }

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
