package pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.CabinTypeDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.CabinType;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CabinTypeMapperTest {

    private CabinType cabinType;

    @BeforeEach
    public void setUp() {
        cabinType = new CabinType();
        cabinType.setCabinTypeName("First class");
    }

    @Test
    void createCabinTypeDTOFromEntity() {
        CabinTypeDTO cabinTypeDTO = CabinTypeMapper.createCabinTypeDTOFromEntity(cabinType);
        assertEquals(cabinType.getCabinTypeName(), cabinTypeDTO.getCabinTypeName());
    }
}
