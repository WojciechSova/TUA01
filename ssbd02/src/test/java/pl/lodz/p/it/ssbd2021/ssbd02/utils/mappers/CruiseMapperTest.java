package pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.CruiseDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.CruiseGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cruise;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Ferry;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Route;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Seaport;

import java.sql.Timestamp;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CruiseMapperTest {

    Account accountModifiedBy;
    Account accountCreatedBy;
    Cruise cruise;
    CruiseDetailsDTO cruiseDetailsDTO;

    @BeforeEach
    void setUp() {
        accountModifiedBy = new Account();
        accountModifiedBy.setLogin("ModifiedLogin");
        accountCreatedBy = new Account();
        accountCreatedBy.setLogin("CreatedLogin");
        cruise = createCruise();
        cruiseDetailsDTO = createCruiseDetails();
    }

    @Test
    void createCruiseGeneralDTOFromEntity() {
        CruiseGeneralDTO cruiseGeneralDTO = CruiseMapper.createCruiseGeneralDTOFromEntity(cruise);

        assertAll(
                () -> assertEquals(cruise.getStartDate(), cruiseGeneralDTO.getStartDate()),
                () -> assertEquals(cruise.getEndDate(), cruiseGeneralDTO.getEndDate()),
                () -> assertEquals(FerryMapper.createFerryGeneralDTOFromEntity(cruise.getFerry()), cruiseGeneralDTO.getFerry()),
                () -> assertEquals(cruise.getNumber(), cruiseGeneralDTO.getNumber()),
                () -> assertEquals(cruise.getPopularity(), cruiseGeneralDTO.getPopularity()),
                () -> assertEquals(cruise.getVersion(), cruiseGeneralDTO.getVersion())
        );
    }

    private Ferry createFerry() {
        Ferry ferry = new Ferry();
        ferry.setName("Prom");
        ferry.setOnDeckCapacity(50);
        ferry.setVehicleCapacity(20);
        ferry.setVersion(1L);
        return ferry;
    }

    private Cruise createCruise() {
        Cruise cruise = new Cruise();
        cruise.setStartDate(Timestamp.valueOf("2020-09-23 10:10:10.0"));
        cruise.setEndDate(Timestamp.valueOf("2020-09-24 10:10:10.0"));
        cruise.setFerry(createFerry());
        cruise.setRoute(createRoute());
        cruise.setNumber("BARVEN000002");
        cruise.setPopularity(50D);
        cruise.setModificationDate(Timestamp.from(Instant.now()));
        cruise.setModifiedBy(accountModifiedBy);
        cruise.setCreationDate(Timestamp.valueOf("2020-03-25 11:21:15"));
        cruise.setCreatedBy(accountCreatedBy);
        cruise.setVersion(1L);
        return cruise;
    }

    private CruiseDetailsDTO createCruiseDetails() {
        CruiseDetailsDTO cruiseDetailsDTO = new CruiseDetailsDTO();
        cruiseDetailsDTO.setStartDate(Timestamp.valueOf("2020-09-23 10:10:10.0"));
        cruiseDetailsDTO.setEndDate(Timestamp.valueOf("2020-09-24 10:10:10.0"));
        cruiseDetailsDTO.setFerry(FerryMapper.createFerryGeneralDTOFromEntity(createFerry()));
        cruiseDetailsDTO.setRoute(RouteMapper.createRouteGeneralDTOFromEntity(createRoute()));
        cruiseDetailsDTO.setNumber("BARVEN000002");
        cruiseDetailsDTO.setPopularity(13D);
        cruiseDetailsDTO.setModificationDate(Timestamp.from(Instant.now()));
        cruiseDetailsDTO.setModifiedBy(AccountMapper.createAccountGeneralDTOFromEntity(accountModifiedBy));
        cruiseDetailsDTO.setCreationDate(Timestamp.valueOf("2020-03-25 11:21:15"));
        cruiseDetailsDTO.setCreatedBy(AccountMapper.createAccountGeneralDTOFromEntity(accountCreatedBy));
        cruiseDetailsDTO.setVersion(1L);
        return cruiseDetailsDTO;
    }

    private Route createRoute() {
        Route route = new Route();
        Seaport start = new Seaport();
        start.setCity("Start");
        start.setCode("ST");
        route.setStart(start);
        Seaport end = new Seaport();
        end.setCity("Destination");
        end.setCode("DEST");
        route.setDestination(end);
        route.setCode("RCODE");
        return route;
    }

    @Test
    void createCruiseDetailsDTOFromEntity() {
        CruiseDetailsDTO cruiseDetailsDTO = CruiseMapper.createCruiseDetailsDTOFromEntity(cruise);

        assertEquals(cruise.getVersion(), cruiseDetailsDTO.getVersion());
        assertEquals(cruise.getStartDate(), cruiseDetailsDTO.getStartDate());
        assertEquals(cruise.getEndDate(), cruiseDetailsDTO.getEndDate());
        assertEquals(FerryMapper.createFerryGeneralDTOFromEntity(cruise.getFerry()), cruiseDetailsDTO.getFerry());
        assertEquals(RouteMapper.createRouteGeneralDTOFromEntity(cruise.getRoute()), cruiseDetailsDTO.getRoute());
        assertEquals(cruise.getNumber(), cruiseDetailsDTO.getNumber());
        assertEquals(cruise.getPopularity(), cruiseDetailsDTO.getPopularity());
        assertEquals(cruise.getModificationDate(), cruiseDetailsDTO.getModificationDate());
        assertEquals(cruise.getCreationDate(), cruiseDetailsDTO.getCreationDate());
        assertEquals(AccountMapper.createAccountGeneralDTOFromEntity(cruise.getModifiedBy()), cruiseDetailsDTO.getModifiedBy());
        assertEquals(AccountMapper.createAccountGeneralDTOFromEntity(cruise.getCreatedBy()), cruiseDetailsDTO.getCreatedBy());
    }

    @Test
    void createCruiseFromCruiseDetailsDTO() {
        Cruise cruise = CruiseMapper.createCruiseFromCruiseDetailsDTO(this.cruiseDetailsDTO);

        assertEquals(cruise.getVersion(), this.cruiseDetailsDTO.getVersion());
        assertEquals(cruise.getStartDate(), this.cruiseDetailsDTO.getStartDate());
        assertEquals(cruise.getEndDate(), this.cruiseDetailsDTO.getEndDate());
        assertEquals(FerryMapper.createFerryGeneralDTOFromEntity(cruise.getFerry()), this.cruiseDetailsDTO.getFerry());
        assertEquals(RouteMapper.createRouteGeneralDTOFromEntity(cruise.getRoute()), this.cruiseDetailsDTO.getRoute());
        assertEquals(cruise.getNumber(), this.cruiseDetailsDTO.getNumber());
        assertEquals(cruise.getPopularity(), this.cruiseDetailsDTO.getPopularity());
        assertEquals(cruise.getModificationDate(), this.cruiseDetailsDTO.getModificationDate());
        assertEquals(cruise.getCreationDate(), this.cruiseDetailsDTO.getCreationDate());
        assertEquals(AccountMapper.createAccountGeneralDTOFromEntity(cruise.getModifiedBy()), this.cruiseDetailsDTO.getModifiedBy());
        assertEquals(AccountMapper.createAccountGeneralDTOFromEntity(cruise.getCreatedBy()), this.cruiseDetailsDTO.getCreatedBy());
    }
}
