package pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.RouteGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Route;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Seaport;

import static org.junit.jupiter.api.Assertions.*;

class RouteMapperTest {

    private Route route1;

    private Seaport seaport1;
    private Seaport seaport2;

    private final String code = "ABCDEF";

    @BeforeEach
    void setUp() {
        seaport1 = new Seaport();
        seaport2 = new Seaport();

        route1 = createRoute();
    }

    private Route createRoute() {
        Route route = new Route();

        route.setVersion(0L);
        route.setStart(seaport1);
        route.setDestination(seaport2);
        route.setCode(code);

        return route;
    }


    @Test
    void createRouteGeneralDTOFromEntity() {
        RouteGeneralDTO routeGeneralDTO = RouteMapper.createRouteGeneralDTOFromEntity(route1);

        assertEquals(route1.getVersion(), routeGeneralDTO.getVersion());
        assertEquals(SeaportMapper.createSeaportGeneralDTOFromEntities(route1.getStart()),routeGeneralDTO.getStart());
        assertEquals(SeaportMapper.createSeaportGeneralDTOFromEntities(route1.getDestination()), routeGeneralDTO.getDestination());
        assertEquals(route1.getCode(), routeGeneralDTO.getCode());
    }
}
