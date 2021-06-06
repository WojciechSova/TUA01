package pl.lodz.p.it.ssbd2021.ssbd02.web.mop;

import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.FerryDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.FerryGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.FerryManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.GeneralException;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.FerryMapper;

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
 * Zawiera metody obsługujące żądania związane z promami.
 *
 * @author Kacper Świercz
 */
@RequestScoped
@Path("ferries")
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
public class FerryEndpoint {

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
        try {
            List<FerryGeneralDTO> ferryGeneralDTOList = ferryManagerLocal.getAllFerries().stream()
                    .map(FerryMapper::createFerryGeneralDTOFromEntity)
                    .collect(Collectors.toList());

            return Response.ok()
                    .entity(ferryGeneralDTOList)
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
     * Metoda udostępniająca szczegółowe informacje dotyczące promu o podanej nazwie
     *
     * @param name Nazwa wyszukiwanego promu
     * @return Szczegółowe informacje o promie
     */
    @GET
    @Path("{name}")
    @RolesAllowed({"EMPLOYEE", "CLIENT"})
    public Response getFerry(@PathParam("name") String name) {
        try {
            FerryDetailsDTO ferryDetailsDTO = FerryMapper
                    .createFerryDetailsDTOFromEntities(ferryManagerLocal.getFerryAndCabinsByFerryName(name));

            return Response.ok()
                    .entity(ferryDetailsDTO)
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
    public Response addFerry(FerryDetailsDTO ferryDetailsDTO, @Context SecurityContext securityContext) {
        return null;
    }

    @DELETE
    @Path("remove/{name}")
    @RolesAllowed({"EMPLOYEE"})
    public Response removeFerry(@PathParam("name") String name, @Context SecurityContext securityContext) {
        return null;
    }

    @PUT
    @Path("update")
    @RolesAllowed({"EMPLOYEE"})
    public Response updateFerry(FerryDetailsDTO ferryDetailsDTO, @Context SecurityContext securityContext) {
        return null;
    }
}
