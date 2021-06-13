package pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.RouteDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.RouteGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cruise;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Route;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Seaport;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RouteMapperTest {

    Account accountModifiedBy;
    Account accountCreatedBy;

    private Route route1;

    private Seaport seaport1;
    private Seaport seaport2;

    private final String code = "ABCDEF";

    @BeforeEach
    void setUp() {
        accountModifiedBy = new Account();
        accountModifiedBy.setLogin("ModifiedLogin");
        accountCreatedBy = new Account();
        accountCreatedBy.setLogin("CreatedLogin");
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
        route.setCreationDate(Timestamp.valueOf("2020-03-25 11:21:15"));
        route.setCreatedBy(accountCreatedBy);
        return route;
    }


    @Test
    void createRouteGeneralDTOFromEntity() {
        RouteGeneralDTO routeGeneralDTO = RouteMapper.createRouteGeneralDTOFromEntity(route1);

        assertEquals(route1.getVersion(), routeGeneralDTO.getVersion());
        assertEquals(SeaportMapper.createSeaportGeneralDTOFromEntities(route1.getStart()), routeGeneralDTO.getStart());
        assertEquals(SeaportMapper.createSeaportGeneralDTOFromEntities(route1.getDestination()), routeGeneralDTO.getDestination());
        assertEquals(route1.getCode(), routeGeneralDTO.getCode());
    }

    @Test
    void createRouteDetailsDTOFromEntity() {
        Cruise cruise = createCruise();
        RouteDetailsDTO routeDetailsDTO = RouteMapper.createRouteDetailsDTOFromEntity(route1, List.of(cruise));

        assertEquals(route1.getVersion(), routeDetailsDTO.getVersion());
        assertEquals(SeaportMapper.createSeaportGeneralDTOFromEntities(route1.getStart()), routeDetailsDTO.getStart());
        assertEquals(SeaportMapper.createSeaportGeneralDTOFromEntities(route1.getDestination()), routeDetailsDTO.getDestination());
        assertEquals(route1.getCode(), routeDetailsDTO.getCode());
        assertEquals(route1.getCreationDate(), routeDetailsDTO.getCreationDate());
        assertEquals(AccountMapper.createAccountGeneralDTOFromEntity(route1.getCreatedBy()), routeDetailsDTO.getCreatedBy());
        assertEquals(CruiseMapper.createCruiseGeneralDTOFromEntity(cruise), routeDetailsDTO.getCruises().get(0));
    }

    private Cruise createCruise() {
        Cruise cruise = new Cruise();
        cruise.setStartDate(Timestamp.valueOf("2020-09-23 10:10:10.0"));
        cruise.setEndDate(Timestamp.valueOf("2020-09-24 10:10:10.0"));
        cruise.setNumber("BARVEN000002");
        cruise.setModificationDate(Timestamp.from(Instant.now()));
        cruise.setModifiedBy(accountModifiedBy);
        cruise.setCreationDate(Timestamp.valueOf("2020-03-25 11:21:15"));
        cruise.setCreatedBy(accountCreatedBy);
        cruise.setVersion(1L);
        return cruise;
    }

    @Test
    void createRouteFromRouteGeneralDTO() {
    }
}
