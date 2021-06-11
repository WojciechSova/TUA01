package pl.lodz.p.it.ssbd2021.ssbd02.web.mop;

import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.CabinDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.CabinManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.CabinTypeManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.CabinType;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.GeneralException;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mop.CabinTypeExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.AccountMapper;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.CabinMapper;
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
 * Zawiera metody obsługujące żądania związane z rejsami.
 *
 * @author Kacper Świercz
 */
@RequestScoped
@Path("cabins")
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
public class CabinEndpoint {

    @Inject
    private CabinManagerLocal cabinManager;
    @Inject
    private CabinTypeManagerLocal cabinTypeManager;

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
                    .createCabinDetailsDTOFromEntity(cabinManager.getCabinByNumber(number));

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
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed({"EMPLOYEE"})
    public Response updateCabin(@Valid CabinDetailsDTO cabinDTO, @Context SecurityContext securityContext,
                                @HeaderParam("If-Match") @NotNull @NotEmpty String eTag) {

        if (cabinDTO.getNumber() == null || cabinDTO.getVersion() == null) {
            throw CommonExceptions.createPreconditionFailedException();
        }
        if (!DTOIdentitySignerVerifier.verifyDTOIntegrity(eTag, cabinDTO)) {
            throw CommonExceptions.createPreconditionFailedException();
        }
        try {
            CabinType cabinType = cabinTypeManager.getCabinTypeByName(cabinDTO.getCabinType());
            cabinManager.updateCabin(CabinMapper.createEntityFromCabinDetailsDTO(cabinDTO, cabinType),
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
}
