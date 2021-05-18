package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Route;

import javax.ejb.Local;
import java.util.List;

/**
 * Lokalny interfejs managera tras
 *
 * @author Wojciech Sowa
 */
@Local
public interface RouteManagerLocal {

    List<Route> getAllRoutes();

    Route getRouteByCode(String code);

    void createRoute(Route route);

    void updateRoute(Route route, String modifiedBy);

    void removeRoute(Route route);
}
