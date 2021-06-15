package pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.VehicleTypeDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.VehicleType;

import static org.junit.jupiter.api.Assertions.*;

class VehicleTypeMapperTest {

    private VehicleType vehicleType;

    @BeforeEach
    void setUp() {
        vehicleType = new VehicleType();
    }

    @Test
    void createVehicleTypeDTOFromEntity() {
        VehicleTypeDTO vehicleTypeDTO = VehicleTypeMapper.createVehicleTypeDTOFromEntity(vehicleType);

        assertEquals(vehicleTypeDTO.getVehicleTypeName(), vehicleType.getVehicleTypeName());
        assertEquals(vehicleTypeDTO.getRequiredSpace(), vehicleType.getRequiredSpace());
    }
}
