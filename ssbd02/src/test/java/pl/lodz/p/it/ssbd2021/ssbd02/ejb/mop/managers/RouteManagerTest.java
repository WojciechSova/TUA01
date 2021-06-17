package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.AccountMopFacade;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.CruiseFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.RouteFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.SeaportFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cruise;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Route;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Seaport;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.GeneralException;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mop.RouteExceptions;

import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RouteManagerTest {

    private final String code1 = "Code1";
    private final String code2 = "Code2";
    private final String city = "City";

    @Mock
    RouteFacadeLocal routeFacadeLocal;
    @Mock
    CruiseFacadeLocal cruiseFacadeLocal;
    @Mock
    AccountMopFacade accountMopFacade;
    @Mock
    SeaportFacadeLocal seaportFacadeLocal;
    @InjectMocks
    RouteManager routeManager;

    @Spy
    Route route1;
    @Spy
    Route route2;


    @Spy
    Cruise cruise1;
    @Spy
    Cruise cruise2;

    @Spy
    Seaport startSeaport;
    @Spy
    Seaport destinationSeaport;

    private List<Route> routes;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
        routes = new ArrayList<>();
        routes.addAll(Arrays.asList(route1, route2));
    }

    @Test
    void getAllRoutes() {
        when(routeFacadeLocal.findAll()).thenReturn(routes);

        assertEquals(routes.hashCode(), routeManager.getAllRoutes().hashCode());
    }

    @Test
    void getAllRoutesException() {
        when(routeFacadeLocal.findAll()).thenReturn(null);

        GeneralException exception = assertThrows(CommonExceptions.class, () -> routeManager.getAllRoutes());

        assertEquals(CommonExceptions.createNoResultException().getResponse().getStatus(),
                exception.getResponse().getStatus());
        assertEquals(CommonExceptions.createNoResultException().getMessage(), exception.getMessage());
    }

    @Test
    void getRouteAndCruisesByRouteCode() {
        String code = "VENVAL";
        when(routeFacadeLocal.findByCode(code)).thenReturn(route1);
        when(cruiseFacadeLocal.findAllByRoute(route1)).thenReturn(Arrays.asList(cruise1, cruise2));

        assertEquals(Pair.of(route1, Arrays.asList(cruise1, cruise2)), routeManager.getRouteAndCruisesByRouteCode(code));
    }

    @Test
    void createRoute() {
        Seaport start = new Seaport();
        Seaport destination = new Seaport();
        start.setCode("ABC");
        start.setCity("Abc");
        destination.setCode("DEF");
        destination.setCity("Def");
        Account account = new Account();
        account.setLogin("login");
        when(seaportFacadeLocal.findByCode("ABC")).thenReturn(start);
        when(seaportFacadeLocal.findByCode("DEF")).thenReturn(destination);
        when(accountMopFacade.findByLogin("login")).thenReturn(account);

        Route route = new Route();
        route.setVersion(20L);
        route.setCreationDate(Timestamp.from(Instant.now()));
        route.setCode("ABCABC");

        Timestamp before = Timestamp.from(Instant.now().minus(1L, ChronoUnit.MILLIS));
        routeManager.createRoute(route, "ABC", "DEF", "login");
        Timestamp after = Timestamp.from(Instant.now().plus(1L, ChronoUnit.MILLIS));

        assertEquals(start, route.getStart());
        assertEquals(destination, route.getDestination());
        assertEquals(account, route.getCreatedBy());
        assertTrue(route.getCreationDate().after(before) && route.getCreationDate().before(after));
        assertEquals(0L, route.getVersion());

        verify(routeFacadeLocal).create(route);
    }

    @Test
    void getRouteByCode() {
        when(routeFacadeLocal.findByCode(code1)).thenReturn(route1);
        assertDoesNotThrow(() -> routeManager.getRouteByCode(code1));
        assertEquals(route1.hashCode(), routeManager.getRouteByCode(code1).hashCode());
        verify(routeFacadeLocal, times(2)).findByCode(code1);
    }

    @Test
    void removeRoute() {
        doAnswer(invocationOnMock -> {
            routes.remove(route2);
            return null;
        }).when(routeFacadeLocal).remove(route2);

        when(routeFacadeLocal.findByCode(code1)).thenReturn(route1);
        when(route1.getStart()).thenReturn(startSeaport);
        when(route1.getDestination()).thenReturn(destinationSeaport);
        when(routeFacadeLocal.findByCode(code2)).thenReturn(route2);
        when(route2.getStart()).thenReturn(startSeaport);
        when(route2.getDestination()).thenReturn(destinationSeaport);
        when(startSeaport.getCity()).thenReturn(city);
        when(destinationSeaport.getCity()).thenReturn(city);

        assertEquals(2, routes.size());
        assertDoesNotThrow(() -> routeManager.removeRoute(code2, "Login"));
        assertEquals(1, routes.size());

        doAnswer(invocationOnMock -> {
            throw CommonExceptions.createConstraintViolationException();
        }).when(routeFacadeLocal).remove(route1);

        RouteExceptions ex = assertThrows(RouteExceptions.class,
                () -> routeManager.removeRoute(code1, "Login"));

        assertEquals(Response.Status.CONFLICT.getStatusCode(), ex.getResponse().getStatus());
        assertEquals(RouteExceptions.ERROR_ROUTE_USED_BY_CRUISE, ex.getResponse().getEntity());

        verify(routeFacadeLocal).remove(route1);
        verify(routeFacadeLocal).remove(route2);
    }
}
