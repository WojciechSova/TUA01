package pl.lodz.p.it.ssbd2021.ssbd02.web.mop;

import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.SeaportDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.SeaportGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.SeaportManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.GeneralException;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.SeaportMapper;

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
 * Zawiera metody obsługujące żądania związane z portami.
 *
 * @author Kacper Świercz
 */
@RequestScoped
@Path("seaports")
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
public class SeaportEndpoint {

    @Inject
    SeaportManagerLocal seaportManager;

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

    @GET
    @Path("{code}")
    @RolesAllowed({"EMPLOYEE", "CLIENT"})
    public Response getSeaport(@PathParam("code") String code) {
        return null;
    }

    @POST
    @Path("add")
    @RolesAllowed({"EMPLOYEE"})
    public Response addSeaport(SeaportDetailsDTO seaportDTO, @Context SecurityContext securityContext) {
        return null;
    }

    @PUT
    @Path("update")
    @RolesAllowed({"EMPLOYEE"})
    public Response updateSeaport(SeaportDetailsDTO seaportDTO, @Context SecurityContext securityContext){
        return null;
    }

    @DELETE
    @Path("remove/{code}")
    @RolesAllowed({"EMPLOYEE"})
    public Response removeSeaport(@PathParam("code") String code, @Context SecurityContext securityContext) {
        return null;
    }
}
