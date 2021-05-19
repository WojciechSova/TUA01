package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.RouteManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Route;

import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.List;

/**
 * Manager tras
 *
 * @author Wojciech Sowa
 */
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class RouteManager implements RouteManagerLocal {
    @Override
    public List<Route> getAllRoutes() {
        return null;
    }

    @Override
    public Route getRouteByCode(String code) {
        return null;
    }

    @Override
    public List<Route> getRoutesByStart(String city) {
        return null;
    }

    @Override
    public List<Route> getRoutesByDestination(String city) {
        return null;
    }

    @Override
    public void createRoute(Route route) {

    }

    @Override
    public void removeRoute(Route route) {

    }
}
