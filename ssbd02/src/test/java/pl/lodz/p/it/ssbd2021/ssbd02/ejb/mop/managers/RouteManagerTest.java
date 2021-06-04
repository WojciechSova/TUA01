package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.RouteFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Route;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.GeneralException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class RouteManagerTest {

    @Mock
    RouteFacadeLocal routeFacadeLocal;
    @InjectMocks
    RouteManager routeManager;

    @Spy
    Route route1;
    @Spy
    Route route2;

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
}
