package pl.lodz.p.it.ssbd2021.ssbd02.web.mop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.SeaportDetailsDTO;
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
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

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

    @GET
    @RolesAllowed({"EMPLOYEE"})
    public Response getAllSeaports() {
        return null;
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

    @POST
    @Path("add")
    @RolesAllowed({"EMPLOYEE"})
    public Response addSeaport(SeaportDetailsDTO seaportDetailsDTO, @Context SecurityContext securityContext) {
        return null;
    }

    @PUT
    @Path("update")
    @RolesAllowed({"EMPLOYEE"})
    public Response updateSeaport(SeaportDetailsDTO seaportDetailsDTO, @Context SecurityContext securityContext){
        return null;
    }

    @DELETE
    @Path("remove/{code}")
    @RolesAllowed({"EMPLOYEE"})
    public Response removeSeaport(@PathParam("code") String code, @Context SecurityContext securityContext) {
        return null;
    }
}
