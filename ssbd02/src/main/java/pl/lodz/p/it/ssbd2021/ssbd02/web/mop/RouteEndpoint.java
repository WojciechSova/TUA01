package pl.lodz.p.it.ssbd2021.ssbd02.web.mop;

import org.apache.commons.lang3.tuple.Pair;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.RouteDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.RouteGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.RouteManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cruise;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Route;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.GeneralException;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.RouteMapper;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.signing.DTOIdentitySignerVerifier;

import javax.annotation.security.RolesAllowed;
import javax.ejb.AccessLocalException;
import javax.ejb.EJBAccessException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
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

    /**
     * Metoda udostępniająca ogólne informacje o trasach.
     *
     * @return Lista ogólnych informacji o trasach
     */
    @GET
    @RolesAllowed({"EMPLOYEE"})
    public Response getAllRoutes() {
        try {
            List<RouteGeneralDTO> routeDTOList = routeManager.getAllRoutes().stream()
                    .map(RouteMapper::createRouteGeneralDTOFromEntity)
                    .collect(Collectors.toList());

            return Response.ok()
                    .entity(routeDTOList)
                    .build();
        } catch (GeneralException generalException) {
            throw generalException;
        } catch (EJBAccessException | AccessLocalException accessException) {
            throw CommonExceptions.createForbiddenException();
        } catch (Exception e) {
            throw CommonExceptions.createUnknownException();
        }
    }

    /**
     * Metoda udostępniająca szczegółowe informacje o trasie oraz listę ogólnych informacji o rejsach na tej trasie.
     *
     * @param code Kod trasy
     * @return Szczegółowe informacje o trasie oraz lista ogólnych informacji o rejsach na tej trasie
     */
    @GET
    @Path("/{code}")
    @RolesAllowed({"EMPLOYEE"})
    public Response getRouteAndCruisesForRoute(@PathParam("code") String code) {
        try {
            Pair<Route, List<Cruise>> pair = routeManager.getRouteAndCruisesByRouteCode(code);
            RouteDetailsDTO routeDetailsDTO = RouteMapper.createRouteDetailsDTOFromEntity(pair.getLeft(), pair.getRight());

            return Response.ok()
                    .entity(routeDetailsDTO)
                    .tag(DTOIdentitySignerVerifier.calculateDTOSignature(routeDetailsDTO))
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
     * Metoda umożliwiająca dodanie nowej trasy.
     *
     * @param start           Identyfikator biznesowy portu startowego
     * @param dest            Identyfikator biznesowy portu docelowego
     * @param routeDetailsDTO Obiekt typu {@link RouteDetailsDTO} przechowujący szczegóły nowej trasy
     * @param securityContext Interfejs wstrzykiwany w celu pozyskania tożsamości aktualnie uwierzytelnionego użytkownika
     * @return Kod 200 w przypadku poprawnego dodania trasy
     */
    @POST
    @Path("add/from/{start}/to/{dest}")
    @RolesAllowed({"EMPLOYEE"})
    public Response addRoute(@PathParam("start") String start, @PathParam("dest") String dest,
                             @Valid RouteDetailsDTO routeDetailsDTO, @Context SecurityContext securityContext) {
        if (!start.matches("[A-Z]{3}") || !dest.matches("[A-Z]{3}") || start.equals(dest)) {
            throw CommonExceptions.createConstraintViolationException();
        }

        try {
            routeManager.createRoute(RouteMapper.createRouteFromRouteDetailsDTO(routeDetailsDTO),
                    start, dest, securityContext.getUserPrincipal().getName());

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
     * Metoda umożliwiająca usunięcie trasy.
     *
     * @param code            Kod trasy, którą chcemy usunąć
     * @param securityContext Interfejs wstrzykiwany w celu pozyskania tożsamości aktualnie uwierzytelnionego użytkownika
     * @return Kod odpowiedzi 200 w przypadku poprawnego usunięcia trasy
     */
    @DELETE
    @Path("remove/{code}")
    @RolesAllowed({"EMPLOYEE"})
    public Response removeRoute(@PathParam("code") String code, @Context SecurityContext securityContext) {
        try {
            Route route = routeManager.getRouteByCode(code);
            routeManager.removeRoute(route, route.getStart().getCity(), route.getDestination().getCity(), securityContext.getUserPrincipal().getName());
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
