package pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.FerryDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.FerryGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cabin;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.CabinType;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Ferry;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FerryMapperTest {

    private Ferry ferry;
    private Cabin cabin;
    private CabinType cabinType;
    private Account accountModifiedBy;
    private Account accountCreatedBy;
    private List<Cabin> cabins;
    private FerryGeneralDTO ferryGeneralDTO;
    private FerryDetailsDTO ferryDetailsDTO;

    @BeforeEach
    public void setUp() {
        ferry = createFerry();
        ferryGeneralDTO = createFerryGeneral();
        accountCreatedBy = new Account();
        accountCreatedBy.setLogin("CreatedByLogin");
        accountModifiedBy = new Account();
        accountModifiedBy.setLogin("ModifiedByLogin");
        cabinType = createCabinType();
        cabin = createCabin();
        cabins = Collections.singletonList(cabin);
    }

    @Test
    public void createFerryGeneralDTOFromEntityTest() {
        ferryGeneralDTO = FerryMapper.createFerryGeneralDTOFromEntity(ferry);

        assertAll(
                () -> assertEquals(ferry.getVersion(), ferryGeneralDTO.getVersion()),
                () -> assertEquals(ferry.getName(), ferryGeneralDTO.getName()),
                () -> assertEquals(ferry.getOnDeckCapacity(), ferryGeneralDTO.getOnDeckCapacity()),
                () -> assertEquals(ferry.getVehicleCapacity(), ferryGeneralDTO.getVehicleCapacity())
        );
    }

    @Test
    void createFerryDetailsDTOFromEntities() {
        ferryDetailsDTO = FerryMapper
                .createFerryDetailsDTOFromEntities(Pair.of(ferry, cabins));

        assertAll(
                () -> assertEquals(ferry.getVersion(), ferryDetailsDTO.getVersion()),
                () -> assertEquals(ferry.getName(), ferryDetailsDTO.getName()),
                () -> assertEquals(cabins.stream().map(CabinMapper::createCabinGeneralDTOFromEntity).collect(Collectors.toList()).hashCode(),
                        ferryDetailsDTO.getCabins().hashCode()),
                () -> assertEquals(ferry.getVehicleCapacity(), ferryDetailsDTO.getVehicleCapacity()),
                () -> assertEquals(ferry.getOnDeckCapacity(), ferryDetailsDTO.getOnDeckCapacity()),
                () -> assertEquals(ferry.getModificationDate(), ferryDetailsDTO.getModificationDate()),
                () -> assertEquals(AccountMapper.createAccountGeneralDTOFromEntity(ferry.getModifiedBy()), ferryDetailsDTO.getModifiedBy()),
                () -> assertEquals(ferry.getCreationDate(), ferryDetailsDTO.getCreationDate()),
                () -> assertEquals(AccountMapper.createAccountGeneralDTOFromEntity(ferry.getCreatedBy()), ferryDetailsDTO.getCreatedBy())
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

    private FerryGeneralDTO createFerryGeneral() {
        FerryGeneralDTO ferryGeneralDTO = new FerryGeneralDTO();
        ferryGeneralDTO.setName("Prom");
        ferryGeneralDTO.setOnDeckCapacity(50);
        ferryGeneralDTO.setVehicleCapacity(20);
        ferryGeneralDTO.setVersion(1L);
        return ferryGeneralDTO;
    }

    private CabinType createCabinType() {
        CabinType cabinType = new CabinType();
        cabinType.setCabinTypeName("First class");
        return cabinType;
    }

    private Cabin createCabin() {
        Cabin cabin = new Cabin();
        cabin.setFerry(ferry);
        cabin.setCapacity(100);
        cabin.setCabinType(cabinType);
        cabin.setNumber("J123");
        cabin.setModificationDate(Timestamp.from(Instant.now()));
        cabin.setModifiedBy(accountModifiedBy);
        cabin.setCreationDate(Timestamp.valueOf("2021-06-01 11:11:11"));
        cabin.setCreatedBy(accountCreatedBy);
        cabin.setVersion(1L);
        return cabin;
    }

    @Test
    void createFerryFromFerryGeneralDTO() {
        ferry = FerryMapper.createFerryFromFerryGeneralDTO(ferryGeneralDTO);

        assertAll(
                () -> assertEquals(ferryGeneralDTO.getVersion(), ferry.getVersion()),
                () -> assertEquals(ferryGeneralDTO.getName(), ferry.getName()),
                () -> assertEquals(ferryGeneralDTO.getOnDeckCapacity(), ferry.getOnDeckCapacity()),
                () -> assertEquals(ferryGeneralDTO.getVehicleCapacity(), ferry.getVehicleCapacity())
        );
    }
}
