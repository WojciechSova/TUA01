package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import org.apache.commons.lang3.tuple.Pair;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractManager;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.CruiseFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.RouteFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.RouteManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cruise;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Route;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.TrackerInterceptor;

import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.util.List;
import java.util.Optional;

/**
 * Manager tras
 *
 * @author Wojciech Sowa
 */
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
@Interceptors(TrackerInterceptor.class)
public class RouteManager extends AbstractManager implements RouteManagerLocal, SessionSynchronization {

    @Inject
    RouteFacadeLocal routeFacadeLocal;

    @Inject
    CruiseFacadeLocal cruiseFacadeLocal;

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public List<Route> getAllRoutes() {
        return Optional.ofNullable(routeFacadeLocal.findAll()).orElseThrow(CommonExceptions::createNoResultException);
    }

    @Override
    @RolesAllowed({"EMPLOYEE", "CLIENT"})
    public Route getRouteByCode(String code) {
        return null;
    }

    @Override
    @RolesAllowed({"EMPLOYEE", "CLIENT"})
    public List<Route> getRoutesByStart(String city) {
        return null;
    }

    @Override
    @RolesAllowed({"EMPLOYEE", "CLIENT"})
    public List<Route> getRoutesByDestination(String city) {
        return null;
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public Pair<Route, List<Cruise>> getRouteAndCruisesByRouteCode(String code) {
        Route route = routeFacadeLocal.findByCode(code);
        List<Cruise> cruiseList = cruiseFacadeLocal.findAllByRoute(route);
        return Pair.of(route, cruiseList);
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void createRoute(Route route) {

    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void removeRoute(Route route) {

    }
}
