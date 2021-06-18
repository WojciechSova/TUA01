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

import javax.annotation.security.RolesAllowed;
import javax.ejb.AccessLocalException;
import javax.ejb.EJBAccessException;
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
import java.util.List;
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
        try {
            List<SeaportGeneralDTO> seaportGeneralDTOList = seaportManager.getAllSeaports().stream()
                    .map(SeaportMapper::createSeaportGeneralDTOFromEntities)
                    .collect(Collectors.toList());

            return Response.ok()
                    .entity(seaportGeneralDTOList)
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
     * Metoda udostępniająca informacje o porcie po podaniu kodu portu.
     *
     * @param code Kod portu
     * @return Informacje o porcie
     */
    @GET
    @Path("{code}")
    @RolesAllowed({"EMPLOYEE", "CLIENT"})
    public Response getSeaport(@PathParam("code") String code) {
        if (code == null || code.length() != 3) {
            throw CommonExceptions.createConstraintViolationException();
        }

        try {
            SeaportDetailsDTO seaportDetailsDTO = SeaportMapper
                    .createSeaportDetailsDTOFromEntity(seaportManager.getSeaportByCode(code));

            return Response.ok()
                    .entity(seaportDetailsDTO)
                    .tag(DTOIdentitySignerVerifier.calculateDTOSignature(seaportDetailsDTO))
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
        try {
            seaportManager.createSeaport(securityContext.getUserPrincipal().getName(), SeaportMapper.createSeaportFromSeaportDetailsDTO(seaportDetailsDTO));
            return Response.ok()
                    .build();
        } catch (GeneralException generalException) {
            throw generalException;
        } catch (EJBAccessException | AccessLocalException accessExcept) {
            throw CommonExceptions.createForbiddenException();
        } catch (Exception e) {
            throw CommonExceptions.createUnknownException();
        }
    }

    @PUT
    @Path("update")
    @RolesAllowed({"EMPLOYEE"})
    public Response updateSeaport(@Valid SeaportDetailsDTO seaportDetailsDTO, @Context SecurityContext securityContext,
                                  @HeaderParam("If-Match") @NotNull @NotEmpty String eTag) {
        if (seaportDetailsDTO.getCity() == null || seaportDetailsDTO.getCity().isBlank()
                || seaportDetailsDTO.getVersion() == null) {
            throw CommonExceptions.createConstraintViolationException();
        }
        if (!DTOIdentitySignerVerifier.verifyDTOIntegrity(eTag, seaportDetailsDTO)) {
            throw CommonExceptions.createPreconditionFailedException();
        }
        try {
            seaportManager.updateSeaport(SeaportMapper.createSeaportFromSeaportDetailsDTO(seaportDetailsDTO),
                    securityContext.getUserPrincipal().getName());
            return Response.ok()
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
     * Metoda umożliwiająca usunięcie portu.
     *
     * @param code            kod identyfikujący port który chcemy usunąć
     * @param securityContext Interfejs wstrzykiwany w celu pozyskania tożsamości aktualnie uwierzytelnionego użytkownika
     * @return Kod 200 w przypadku udanego usunięcia portu
     */
    @DELETE
    @Path("remove/{code}")
    @RolesAllowed({"EMPLOYEE"})
    public Response removeSeaport(@PathParam("code") String code, @Context SecurityContext securityContext) {
        try {
            String userLogin = securityContext.getUserPrincipal().getName();
            seaportManager.removeSeaport(code, userLogin);
            return Response.ok()
                    .build();
        } catch (GeneralException generalException) {
            throw generalException;
        } catch (EJBAccessException | AccessLocalException accessExcept) {
            throw CommonExceptions.createForbiddenException();
        } catch (Exception e) {
            throw CommonExceptions.createUnknownException();
        }
    }
}
