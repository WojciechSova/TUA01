package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces;

import org.apache.commons.lang3.tuple.Pair;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cruise;
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

    /**
     * Metoda wyszukująca wszystkie trasy.
     *
     * @return Lista tras {@link Route}
     */
    List<Route> getAllRoutes();

    /**
     * Metoda wyszukująca trasę o podanym kodzie.
     *
     * @param code Kod trasy, którą chcemy wyszukać
     * @return Encja typu {@link Route}
     */
    Route getRouteByCode(String code);

    /**
     * Metoda wyszukująca wszystkie trasy, które rozpoczynają się w podanym mieście.
     *
     * @param city Nazwa miasta, po którym chcemy wyszukać
     * @return Lista tras {@link Route}, które rozpoczynają się w podanym mieście
     */
    List<Route> getRoutesByStart(String city);

    /**
     * Metoda wyszukująca wszystkie trasy, które kończą się w podanym mieście.
     *
     * @param city Nazwa miasta, po którym chcemy wyszukać
     * @return Lista tras {@link Route}, które kończą się w podanym mieście
     */
    List<Route> getRoutesByDestination(String city);

    /**
     * Metoda wyszukująca trasę oraz wszystkie rejsy na trasie o podanym kodzie.
     *
     * @param code Kod trasy, po której chcemy wyszukać
     * @return Para składająca się z trasy {@link Route} oraz listy rejsów {@link Cruise}, które zawierają trasę o podanym kodzie.
     */
    Pair<Route, List<Cruise>> getRouteAndCruisesByRouteCode(String code);

    /**
     * Metoda tworząca trasę.
     *
     * @param route Encja typu {@link Route}
     */
    void createRoute(Route route);

    /**
     * Metoda usuwa trasę o kodzie zawartym w encji {@link Route}.
     *
     * @param route Encja typu {@link Route}
     */
    void removeRoute(Route route);
}
