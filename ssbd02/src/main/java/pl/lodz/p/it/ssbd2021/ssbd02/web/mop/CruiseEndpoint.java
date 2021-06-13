package pl.lodz.p.it.ssbd2021.ssbd02.web.mop;

import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.CruiseDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.CruiseGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.CruiseManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.GeneralException;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.CruiseMapper;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.signing.DTOIdentitySignerVerifier;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.AccessLocalException;
import javax.ejb.EJBAccessException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
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

    @Inject
    private CruiseManagerLocal cruiseManagerLocal;

    @GET
    @RolesAllowed({"EMPLOYEE"})
    public Response getAllCruises() {
        return null;
    }

    /**
     * Metoda udostępniająca szczegółowe informacje dotyczące rejsu o podanym numerze
     *
     * @param number Numer wyszukiwanego rejsu
     * @return Szczegółowe informacje o rejsie
     */
    @GET
    @Path("/{number}")
    @RolesAllowed({"EMPLOYEE"})
    public Response getCruise(@PathParam("number") String number) {
        try {
            CruiseDetailsDTO cruiseDetailsDTO = CruiseMapper
                    .createCruiseDetailsDTOFromEntity(cruiseManagerLocal.getCruiseByNumber(number));

            return Response.ok()
                    .entity(cruiseDetailsDTO)
                    .tag(DTOIdentitySignerVerifier.calculateDTOSignature(cruiseDetailsDTO))
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
     * Metoda udostępniająca informacje o aktualnych rejsach
     *
     * @return List aktualnych rejsów
     */
    @GET
    @Path("current")
    @PermitAll
    public Response getCurrentCruises() {
        try {
            List<CruiseGeneralDTO> currentCruisesDTOList = cruiseManagerLocal.getAllCurrentCruises().stream()
                    .map(CruiseMapper::createCruiseGeneralDTOFromEntity)
                    .collect(Collectors.toList());

            return Response.ok()
                    .entity(currentCruisesDTOList)
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
    @Path("add/{ferry}/{route}")
    @RolesAllowed({"EMPLOYEE"})
    public Response addCruise(@Valid CruiseDetailsDTO cruiseDetailsDTO,
                              @PathParam("ferry") String ferry,
                              @PathParam("route") String route,
                              @Context SecurityContext securityContext) {
        cruiseManagerLocal.createCruise(CruiseMapper.createCruiseFromCruiseDetailsDTO(cruiseDetailsDTO),
                ferry, route, securityContext.getUserPrincipal().getName());
        return null;
    }

    @DELETE
    @Path("remove/{number}")
    @RolesAllowed({"EMPLOYEE"})
    public Response removeCruise(@PathParam("number") String number, @Context SecurityContext securityContext) {
        return null;
    }

    @PUT
    @Path("update")
    @RolesAllowed({"EMPLOYEE"})
    public Response updateCruise(CruiseDetailsDTO cruiseDetailsDTO, @Context SecurityContext securityContext) {
        return null;
    }

}
