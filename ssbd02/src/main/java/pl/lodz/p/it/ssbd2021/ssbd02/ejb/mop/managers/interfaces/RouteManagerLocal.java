package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Ferry;
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
     * Metoda wyszukująca trasę o podanym kodzie
     *
     * @param code Kod trasy, którą chcemy wyszukać
     * @return Encja typu {@link Route}
     */
    Route getRouteByCode(String code);

    /**
     * Metoda tworząca trasę
     *
     * @param route Encja typu {@link Route}
     */
    void createRoute(Route route);
//
//    /**
//     * Metoda aktualizuje trasę o kodzie zawartym w encji {@link Route} oraz ustawia konto w polu modifiedBy na konto
//     * użytkownika dokonującego zmiany
//     *
//     * @param route    Encja typu {@link Route}
//     * @param modifiedBy Login użytkownika, który edytuje encje
//     */
//    void updateRoute(Route route, String modifiedBy);

    /**
     * Metoda usuwa trasę o kodzie zawartym w encji {@link Route}
     *
     * @param route Encja typu {@link Route}
     */
    void removeRoute(Route route);
}
