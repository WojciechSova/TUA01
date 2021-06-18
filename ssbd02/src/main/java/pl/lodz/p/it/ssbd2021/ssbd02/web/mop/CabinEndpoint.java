package pl.lodz.p.it.ssbd2021.ssbd02.web.mop;

import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.CabinDetailsDTO;
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
        try {
            cabinManager.createCabin(
                    CabinMapper.createEntityFromCabinDetailsDTO(cabinDTO, cabinTypeManager.getCabinTypeByName(cabinDTO.getCabinType())),
                    securityContext.getUserPrincipal().getName(),
                    ferryName);
            return Response.accepted()
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
     * Metoda udostępniająca szczegółowe informacje dotyczące kajuty o podanym numerze i znajdującej się na podanym promie.
     *
     * @param ferryName Nazwa promu, na którym znajduje się kajuta
     * @param cabinNumber Numer wyszukiwanej kajuty
     * @return Szczegółowe informacje o kajucie
     */
    @GET
    @Path("details/{ferry}/{number}")
    @RolesAllowed({"EMPLOYEE"})
    public Response getCabin(@PathParam("ferry") String ferryName, @PathParam("number") String cabinNumber) {
        try {
            CabinDetailsDTO cabinDetailsDTO = CabinMapper
                    .createCabinDetailsDTOFromEntity(cabinManager.getCabinByFerryAndNumber(ferryName, cabinNumber));

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
        try {
            CabinType cabinType = cabinTypeManager.getCabinTypeByName(cabinDTO.getCabinType());
            cabinManager.updateCabin(CabinMapper.createEntityFromCabinDetailsDTO(cabinDTO, cabinType),
                    securityContext.getUserPrincipal().getName(), ferryName);
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
