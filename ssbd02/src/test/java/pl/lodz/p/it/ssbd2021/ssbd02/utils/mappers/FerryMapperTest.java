package pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.FerryGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Ferry;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FerryMapperTest {

    private Ferry ferry;
    private FerryGeneralDTO ferryGeneralDTO;

    @BeforeEach
    public void setUp() {
        ferry = createFerry();
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

    private Ferry createFerry() {
        Ferry ferry = new Ferry();
        ferry.setName("Prom");
        ferry.setOnDeckCapacity(50);
        ferry.setVehicleCapacity(20);
        ferry.setVersion(1L);
        return ferry;
    }
}