package pl.lodz.p.it.ssbd2021.ssbd02.web.mop;

import com.sun.xml.bind.v2.TODO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.RouteAndCruisesDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.RouteDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.RouteManagerLocal;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * Klasa ziarna CDI o zasięgu żądania.
 * Zawiera metody obsługujące żądania związane z trasami.
 *
 * @author Kacper Świercz
 */
@RequestScoped
@Path("routes")
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
public class RouteEndpoint {

    @Inject
    private RouteManagerLocal routeManager;

    @GET
    @RolesAllowed({"EMPLOYEE"})
    public Response getAllRoutes() {
        return null;
    }

    @GET
    @Path("route/{code}")
    @RolesAllowed({"EMPLOYEE"})
    public Response getRouteAndCruisesForRoute(@PathParam("code") String code) {
        try {
            //TODO Dodanie mappera
            RouteAndCruisesDTO routeAndCruisesDTO =  routeManager.getRouteAndCruisesByRouteCode(code);

            return Response.ok()
                    .entity(routeAndCruisesDTO)
                    .tag(DTOIdentitySignerVerifier.calculateDTOSignature(routeAndCruisesDTO.getRoute()))
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
    public Response addRoute(RouteDTO routeDTO, @Context SecurityContext securityContext) {
        return null;
    }

    @DELETE
    @Path("remove/{code}")
    @RolesAllowed({"EMPLOYEE"})
    public Response removeRoute(@PathParam("code") String code, @Context SecurityContext securityContext) {
        return null;
    }
}
