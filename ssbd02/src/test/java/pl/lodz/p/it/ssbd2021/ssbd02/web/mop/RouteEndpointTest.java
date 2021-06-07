package pl.lodz.p.it.ssbd2021.ssbd02.web.mop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.RouteGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.RouteManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Route;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Seaport;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.RouteMapper;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class RouteEndpointTest {

    @InjectMocks
    private RouteEndpoint routeEndpoint;

    @Mock
    private RouteManagerLocal routeManagerLocal;

    private Route route1;
    private Route route2;
    private Seaport seaport1;
    private Seaport seaport2;
    private List<Route> routes = new ArrayList<>();
    private List<RouteGeneralDTO> routeGeneralDTOList = new ArrayList<>();

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
        seaport1 = new Seaport();
        seaport2 = new Seaport();

        route1 = new Route();
        route1.setStart(seaport1);
        route1.setDestination(seaport2);

        route2 = new Route();
        route2.setStart(seaport2);
        route2.setDestination(seaport1);

        routes.addAll(Arrays.asList(route1, route2));
    }

    @Test
    void getAllRoutes() {
        when(routeManagerLocal.getAllRoutes()).thenReturn(routes);
        routeGeneralDTOList.addAll(routes.stream()
                .map(RouteMapper::createRouteGeneralDTOFromEntity)
                .collect(Collectors.toList()));

        Response response = routeEndpoint.getAllRoutes();

        assertEquals(routeGeneralDTOList, response.getEntity());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
}
