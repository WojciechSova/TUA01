package pl.lodz.p.it.ssbd2021.ssbd02.web.mop;

import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.CabinDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.CabinManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.GeneralException;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.CabinMapper;
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
 * Zawiera metody obsługujące żądania związane z rejsami.
 *
 * @author Kacper Świercz
 */
@RequestScoped
@Path("cabins")
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
public class CabinEndpoint {

    @Inject
    private CabinManagerLocal cabinManagerLocal;

    @POST
    @Path("add")
    @RolesAllowed({"EMPLOYEE"})
    public Response addCabin(CabinDetailsDTO cabinDTO, @Context SecurityContext securityContext) {
        return null;
    }

    /**
     * Metoda udostępniająca szczegółowe informacje dotyczące kajuty o podanym numerze
     *
     * @param number Numer wyszukiwanej kajuty
     * @return Szczegółowe informacje o kajucie
     */
    @GET
    @Path("{number}")
    @RolesAllowed({"EMPLOYEE"})
    public Response getCabin(@PathParam("number") String number) {
        try {
            CabinDetailsDTO cabinDetailsDTO = CabinMapper
                    .createCabinDetailsDTOFromEntity(cabinManagerLocal.getCabinByNumber(number));

            return Response.ok()
                    .entity(cabinDetailsDTO)
                    .tag(DTOIdentitySignerVerifier.calculateDTOSignature(cabinDetailsDTO))
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
    @Path("ferry/{name}")
    @RolesAllowed({"CLIENT", "EMPLOYEE"})
    public Response getCabinsByFerry(@PathParam("name") String name) {
        return null;
    }

    @DELETE
    @Path("remove/{number}")
    @RolesAllowed({"EMPLOYEE"})
    public Response removeCabin(@PathParam("number") String number, @Context SecurityContext securityContext) {
        return null;
    }

    @PUT
    @Path("update")
    @RolesAllowed({"EMPLOYEE"})
    public Response updateCabin(CabinDetailsDTO cabinDTO, @Context SecurityContext securityContext) {
        return null;
    }
}
