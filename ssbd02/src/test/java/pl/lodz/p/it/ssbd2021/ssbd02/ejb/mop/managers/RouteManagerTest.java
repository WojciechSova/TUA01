package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.CruiseFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.RouteFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cruise;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Route;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.GeneralException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class RouteManagerTest {

    @Mock
    RouteFacadeLocal routeFacadeLocal;
    @Mock
    CruiseFacadeLocal cruiseFacadeLocal;
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
}
